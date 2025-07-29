package com.beengineer.common.data;

/********************************************************************************
 * データベースアクセスコントローラクラス
 *
 * DBアクセス制御のための中核クラスです。
 * DbAccessBean を通じて、DB操作（SELECT/INSERT/UPDATE/DELETE）を実行します。
 *
 * 利用者はこのクラスを通じてSQL実行・エンティティ取得を統一的に扱えます。
 *
 * 履歴:
 *   V1.0  R00  2025/07/15  Bepro  新規開発
 *******************************************************************************/

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

import com.beengineer.common.SystemInfo;
import com.beengineer.common.exception.SystemException;
import com.beengineer.common.log.Logger;

public class DbAccessController {

	/**
	 * DBアクセス実行クラス（ファクトリから取得）
	 */
	private IDbAccessBean idab = null;

	/**
	 * テーブル情報を保持するエンティティクラス
	 */
	private DbTableEntity dte;

	/**
	 * DBの主キー（ROWID, OID など）
	 */
	private String object_id;

	/**
	 * コンストラクタ
	 *
	 * @param table テーブル名
	 */
	public DbAccessController(String table) {
		Map<String, String> sysinfo = SystemInfo.getKeyValueHash("database");
		System.out.println("[AFTER DB init] logLevel = " + Logger.getLogLevel());
		this.object_id = sysinfo.get("object_id");
		this.dte = new DbTableEntity(table);
	}

	/**
	 * SELECT実行メソッド
	 *
	 * <ul>
	 *   <li>SQL文を実行して、結果を dte に格納します</li>
	 *   <li>将来的なスレッド対応を見据え synchronized 化</li>
	 * </ul>
	 *
	 * @param connection DBコネクション
	 * @param strSql 実行するSQL文
	 * @return 取得件数
	 * @throws SystemException SQL実行時のシステム例外
	 */
	public synchronized int doSelect(Connection connection, String strSql)
			throws SystemException {
		Logger.out(Logger.DEBUG3, "DbAccessController#doSelect()", "sys", " STARTED ");

		int result = 0;
		idab = DbAccessFactory.getInstance(object_id, "SELECT");
		try {
			Logger.out(Logger.DEBUG3, "DbAccessController#doSelect()", "sys", strSql);

			// フィールド情報を初期化
			dte.resetAllFlg();
			Logger.out(Logger.DEBUG3, "DbAccessBean#doSelect()", "sys", " resetAllFlg Clear！");

			dte.clear();
			Logger.out(Logger.DEBUG3, "DbAccessBean#doSelect()", "sys", " AllField Clear！");

			result = idab.execute(connection, strSql, dte);
		} catch (SystemException se) {
			throw new SystemException("DbAccessController#makeInsertSql()", "unknown", "11003", "検索実行処理に失敗");
		}

		Logger.out(Logger.DEBUG3, "DbAccessController#doSelect()", "sys", " ENDED ");
		if (result == 0) {
			Logger.out(Logger.WARN, "DbAccessBean#executeSelect()", "sys", "0件取得");
		}
		return result;
	}

	/**
	 * INSERT / UPDATE / DELETE 実行メソッド
	 *
	 * <ul>
	 *   <li>DbTableEntity の状態を見て、各種SQLを自動生成</li>
	 *   <li>必要に応じて SELECT でフィールド情報を補完</li>
	 * </ul>
	 *
	 * @param connection DBコネクション
	 * @param strTbl 対象テーブル名
	 * @return 実行件数
	 * @throws SystemException 処理中のシステム例外
	 */
	public synchronized int doExec(Connection connection, String strTbl) throws SystemException {
		Logger.out(Logger.DEBUG3, "DbAccessController#doExec()", "sys", " STARTED ");

		int result = 0;
		String updSql = "";
		String insSql = "";
		String delSql = "";
		ArrayList list_keys = null;

		// テーブルフィールド一覧を取得
		list_keys = dte.getFieldKeys();

		// 処理対象フラグを確認し、該当する処理を順次実行
		if (dte.flgCheck("edit")) {
			if (dte.flgCheck("upd")) {
				idab = DbAccessFactory.getInstance(object_id, "UPDATE");
				updSql = makeUpdateSql(list_keys, strTbl);
				Logger.out(Logger.DEBUG3, "DbAccessController#doExec()", "sys", "executeUpdate :" + updSql);
				result = idab.execute(connection, updSql, dte, list_keys);
			}
			if (dte.flgCheck("del")) {
				idab = DbAccessFactory.getInstance(object_id, "DELETE");
				delSql = makeDeleteSql(strTbl);
				Logger.out(Logger.DEBUG3, "DbAccessController#doExec()", "sys", "executeDelete :" + delSql);
				result = idab.execute(connection, delSql, dte, list_keys);
			}
			if (dte.flgCheck("ins")) {
				String tmpSql = "SELECT * FROM " + strTbl;
				if (object_id.equals("ROWID")) {
					tmpSql += " WHERE ROWNUM = 1";
				} else {
					tmpSql += " LIMIT 1";
				}

				// フィールド定義を取得（未取得時のみ）
				if (!dte.getSelectFlg()) {
					idab = DbAccessFactory.getInstance(object_id, "SELECT_TYPE");
					Logger.out(Logger.DEBUG3, "DbAccessController#doExec()", "sys", "executeSelectType :" + tmpSql);
					result = idab.execute(connection, tmpSql, dte);
				}

				// ObjectIDを除いたキー一覧を取得
				list_keys = hidRow(dte.getFieldKeys());

				idab = DbAccessFactory.getInstance(object_id, "INSERT");
				insSql = makeInsertSql(list_keys, strTbl);
				Logger.out(Logger.DEBUG3, "DbAccessController#doExec()", "sys", "executeInsert :" + insSql);
				result = idab.execute(connection, insSql, dte, list_keys);
			}
		}

		Logger.out(Logger.DEBUG3, "DbAccessController#doExec()", "sys", " ENDED ");
		return result;
	}

	/**
	 * INSERT文のSQL構文を生成するメソッド
	 *
	 * @param list_keys 挿入対象のフィールド名一覧
	 * @param strTbl 対象テーブル名
	 * @return INSERT文文字列
	 * @throws SystemException SQL構文組み立て時の例外
	 */
	private synchronized String makeInsertSql(ArrayList list_keys, String strTbl)
			throws SystemException {

		String strSql = "INSERT INTO " + strTbl + " (";

		// フィールド名の列挙部を構築
		for (int i = 0; i < list_keys.size(); i++) {
			strSql += list_keys.get(i);
			if (i != list_keys.size() - 1) {
				strSql += ",";
			} else {
				strSql += ") VALUES( ";
			}
		}

		// VALUES部（プレースホルダ）を構築
		for (int i = 0; i < list_keys.size(); i++) {
			strSql += "?";
			if (i != list_keys.size() - 1) {
				strSql += ",";
			} else {
				strSql += " ) ";
			}
		}

		return strSql;
	}

	/**
	 * UPDATE文のSQL構文を生成するメソッド8
	 *
	 * @param list_keys 更新対象のフィールド名一覧
	 * @param strTbl 対象テーブル名
	 * @return UPDATE文文字列
	 * @throws SystemException SQL構文組み立て時の例外
	 */
	private synchronized String makeUpdateSql(ArrayList list_keys, String strTbl)
			throws SystemException {
		int count = 0;
		String strSql = "UPDATE " + strTbl + " SET ";
		boolean updDtFlg = false;

		// object_idを先頭に移動し、それ以外を後ろに並べる
		ArrayList<String> tempArray = new ArrayList<>();
		tempArray.add(null); // object_id用の先頭nullを追加

		for (int i = 0; i < list_keys.size(); i++) {
			String tmpStr = list_keys.get(i).toString();
			if (tmpStr.equalsIgnoreCase(object_id)) {
				tempArray.set(0, tmpStr);
			} else {
				tempArray.add(tmpStr);
			}
		}

		// SET句の組み立て
		ArrayList<String> setKeys = new ArrayList<>();
		for (String key : tempArray) {
			if (key == null)
				continue;
			if (key.equalsIgnoreCase("UPDATE_DT")) {
				updDtFlg = true;
			}
			if (!key.equalsIgnoreCase(object_id)) {
				setKeys.add(key);
			}
		}
		for (int i = 0; i < setKeys.size(); i++) {
			strSql += setKeys.get(i) + " = ?";
			if (i != setKeys.size() - 1) {
				strSql += ",";
			}
		}

		// WHERE句の組み立て
		if (object_id.equalsIgnoreCase("ROWID")) {
			strSql += " WHERE ROWIDTOCHAR(ROWID) = ?";
		} else if (object_id.equalsIgnoreCase("ctid")) {
			strSql += " WHERE ctid::text = ?";
		} else {
			strSql += " WHERE stcd = ?";
		}

		// 楽観ロック用のUPDATE_DT条件
		if (updDtFlg) {
			strSql += " AND UPDATE_DT = ?";
		}

		return strSql;
	}

	/**
	 * DELETE文のSQL構文を生成するメソッド
	 *
	 * @param strTbl 対象テーブル名
	 * @return DELETE文文字列
	 * @throws SystemException SQL構文組み立て時の例外
	 */
	private synchronized String makeDeleteSql(String strTbl)
			throws SystemException {
		String strSql = "DELETE FROM " + strTbl;
		if (object_id.equals("ROWID")) {
			strSql += " WHERE ROWIDTOCHAR(ROWID) = ?";
		} else {
			strSql += " WHERE ctid = ?::tid";
		}
		return strSql;
	}

	/**
	 * ObjectID を除外したフィールド名リストを返す
	 *
	 * INSERT時には ObjectID を除く必要があるため、その処理を行います。
	 *
	 * @param list_keys 全フィールドのキー一覧
	 * @return ObjectID 除外済みのフィールド名一覧
	 */
	private synchronized ArrayList hidRow(ArrayList list_keys)
			throws ArrayIndexOutOfBoundsException {
		int count = 0;
		ArrayList tempArray = new ArrayList();
		for (int i = 0; i < list_keys.size(); i++) {
			String tmpStr = list_keys.get(i).toString();
			if (!tmpStr.toUpperCase().equals(object_id)) {
				tempArray.add(count, tmpStr);
				count++;
			}
		}

		return tempArray;
	}

	/**
	 * DbTableEntityのインスタンスを取得する
	 *
	 * @return DbTableEntity オブジェクト
	 */
	public DbTableEntity getDbTableEntity() {
		return dte;
	}
}