package com.beengineer.common.data;

/********************************************************************************
 * データ削除用 DBアクセス実行クラス
 *
 * 本クラスは、指定されたSQLとフィールド情報を元に
 * DELETE処理を実行するための実装クラスです。
 *
 * IDbAccessBean インタフェースを実装し、DbAccessFactory から呼び出されます。
 *
 * 履歴:
 *   V1.0  R00  2025/07/15  Bepro  新規開発
 *******************************************************************************/

// JDBC APIをインポート
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.beengineer.common.exception.SystemException;
import com.beengineer.common.log.Logger;

class DbAccessDelete implements IDbAccessBean {

	// プリコンパイル済みのSQL文を保持するPreparedStatement
	PreparedStatement pstmt = null;

	// 主キー識別用のID名（例：ROWIDやOID）
	String object_id;

	/**
	 * コンストラクタ
	 *
	 * @param object_id 主キー識別用のID名（ROWIDやOIDなど）
	 */
	protected DbAccessDelete(String object_id) {
		this.object_id = object_id;
	}

	/**
	 * SystemExceptionを生成してスローするメソッド
	 *
	 * <ul>
	 *   <li>SQLException を受け取り、ログ出力後に SystemException を送出</li>
	 *   <li>システム全体に例外を通知するための共通処理</li>
	 * </ul>
	 *
	 * @param name 処理名（メソッド識別用）
	 * @param e 発生したSQLException
	 * @return 送出される SystemException
	 * @throws SystemException SQL実行エラーとして通知
	 */
	public SystemException doException(String name, SQLException e)
			throws SystemException {
		this.abendLog(name, e);
		throw new SystemException("DbAccessDelete#execute() " + name + "()", "unknown", "11004",
				"SQL実行エラー:" + e.getErrorCode());
	}

	/**
	 * 例外情報のログ出力処理（メソッド名指定付き）
	 *
	 * @param name 呼び出し元メソッド名
	 * @param e 発生したSQLException
	 */
	public void abendLog(String name, SQLException e) {
		while (e != null) {
			Logger.out(Logger.ERROR, "DbAccessDelete#abendLog() " + name + "()", "sys", "mes=" + e.getMessage());
			Logger.out(Logger.ERROR, "DbAccessDelete#abendLog() " + name + "()", "sys", "stat=" + e.getSQLState());
			Logger.out(Logger.ERROR, "DbAccessDelete#abendLog() " + name + "()", "sys",
					"code=" + String.valueOf(e.getErrorCode()));
			e = e.getNextException();
		}
	}

	/**
	 * 例外情報のログ出力処理（メソッド名省略版）
	 *
	 * @param e 発生したSQLException
	 */
	public void abendLog(SQLException e) {
		while (e != null) {
			Logger.out(Logger.ERROR, "DbAccessDelete#abendLog() ", "system", e.getMessage());
			Logger.out(Logger.ERROR, "DbAccessDelete#abendLog() ", "system", e.getSQLState());
			Logger.out(Logger.ERROR, "DbAccessDelete#abendLog() ", "system", String.valueOf(e.getErrorCode()));
			e = e.getNextException();
		}
	}

	/**
	 * DELETE処理の実行メソッド
	 *
	 * <ul>
	 *   <li>指定されたSQL文を用いて、DbTableEntityに格納された削除対象データを削除します</li>
	 *   <li>削除対象は配列の editFlg / delFlg により制御されます</li>
	 * </ul>
	 *
	 * @param connection DBコネクションオブジェクト
	 * @param strSql 実行するDELETE文（プレースホルダ付き）
	 * @param dte 削除対象フィールドを保持するテーブルエンティティ
	 * @param list_keys 削除条件に使うフィールドのキー一覧
	 * @return 削除件数の合計
	 * @throws SystemException SQLエラー発生時にスローされるシステム例外
	 */
	public int execute(Connection connection, String strSql, DbTableEntity dte,
			ArrayList list_keys) throws SystemException {
		pstmt = null;
		int totalCnt = 0;
		try {
			// -------------------------------------------------
			// 削除処理
			// -------------------------------------------------

			// ステートメントを準備
			pstmt = connection.prepareStatement(strSql);

			// 編集フラグ（配列）の件数分ループ
			for (int i = 0; i < dte.getArrayEditFlg().length; i++) {
				Logger.out(Logger.DEBUG3, "DbAccessDelete#execute()", "sys",
						"削除フラグ配列の件数：" + dte.getArrayEditFlg().length);
				Logger.out(Logger.DEBUG3, "DbAccessDelete#execute()", "sys", "削除フラグの値：" + dte.getDelFlg(i));

				// 該当行が削除対象であれば処理を実行
				if (dte.getDelFlg(i)) {

					// 主キーの値をパラメータにセット
					for (int k = 0; k < list_keys.size(); k++) {
						if ((list_keys.get(k).toString().toUpperCase()).equals(object_id)) {
							// PostgreSQLのバージョン対応（int変換）
							Logger.out(Logger.DEBUG3, "DbAccessDelete#execute()", "sys", k + " 番目データは："
									+ list_keys.get(k) + " = " + (String) dte.getField(i).get(list_keys.get(k)));
							pstmt.setString(1, String.valueOf(dte.getField(i).get(list_keys.get(k))));
						}
					}

					// ステートメント実行（削除）
					totalCnt += pstmt.executeUpdate();
					Logger.out(Logger.DEBUG3, "DbAccessDelete#execute()", "sys", totalCnt + "件 Deleted！：");
				}
			}

			// ステートメントを閉じる
			pstmt.close();

		} catch (SQLException se) {
			this.doException("delete", se);

		} finally {
			try {
				// クローズ処理（念のため2重実施）
				if (pstmt != null)
					pstmt.close();
				pstmt = null;
			} catch (SQLException ignore) {
				this.doException("delete", ignore);
			}
		}

		return totalCnt;
	}

	/**
	 * 未使用の execute メソッド（インターフェース実装義務のため定義）
	 *
	 * @param connection DBコネクション
	 * @param strSql 実行SQL（未使用）
	 * @param dte テーブルエンティティ（未使用）
	 * @return 常に 0 を返却
	 * @throws SystemException 常に発生しないが構文上 throws
	 */
	public int execute(Connection connection, String strSql, DbTableEntity dte)
			throws SystemException {
		return 0;
	}
}
