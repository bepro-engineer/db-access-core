package com.sample;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.beengineer.common.Constants;
import com.beengineer.common.FieldsConstants;
import com.beengineer.common.SqlConstants;
import com.beengineer.common.data.DbAccessController;
import com.beengineer.common.data.DbConnectionPool;
import com.beengineer.common.data.DbTableEntity;
import com.beengineer.common.log.Logger;

public class Sample1 implements Constants, SqlConstants, FieldsConstants {

	// 固定値の定義（false と 0 を明示的に扱うため）
	private static final boolean W_FALSE = false;
	private static final int N_Zero = 0;

	// 操作対象となるテーブル名（DbAccessControllerへ渡すキー）
	private static final String USER_TBL = "user_mst";

	public static void main(String[] args) {

		// SELECT結果が1件以上かどうかのフラグ
		boolean bResult = W_FALSE;

		// user_mst を操作する DbAccessController を生成
		DbAccessController dac = new DbAccessController(USER_TBL);

		// コネクションプールの取得
		DbConnectionPool pool = DbConnectionPool.getInstance();
		Connection con = null;

		// ログの初期化
		Logger.init();

		try {
			// DBコネクション取得
			con = pool.getConnection();

			// 自動コミット OFF（INSERT/UPDATE を明示的に commit/rollback したい）
			con.setAutoCommit(W_FALSE);

			// ① INSERT に使用するパラメータを準備
			Map<String, String> params = new HashMap<>();
			params.put("user_id", "test001");
			params.put("user_name", "山田太郎");
			params.put("company_id", "0000000001");
			params.put("e_mail", "test@example.co.jp");
			params.put("password", "P@ssW0rd");

			// INSERT 対象の値を保持する Entity（DbTableEntity）
			DbTableEntity ite = dac.getDbTableEntity();
			// ite.resetAllFlg(); // 必要に応じて更新フラグを初期化

			// ② INSERT する値を Entity へ設定
			ite.setValue("user_id", params.get("user_id"), N_Zero);
			ite.setValue("user_name", params.get("user_name"), N_Zero);
			ite.setValue("company_id", params.get("company_id"), N_Zero);
			ite.setValue("e_mail", params.get("e_mail"), N_Zero);
			ite.setValue("password", params.get("password"), N_Zero);

			// ③ SQL 実行（INSERT）
			int iResult = dac.doExec(con, USER_TBL);

			// ④ INSERT結果に応じて commit / rollback
			if (iResult > N_Zero) {
				con.commit();
				System.out.println("INSERT結果: " + iResult);
			} else {
				con.rollback();
				System.out.println("INSERT失敗");
			}

			// ----------------------------------------------------------
			// ⑤ SELECT 文の構築（SQL_USER_SELECT_BY_ID を使用）
			// ----------------------------------------------------------
			String strSQL_USER_SELECT_BY_ID = EMPTY;
			strSQL_USER_SELECT_BY_ID = SQL_USER_SELECT_BY_ID;
			strSQL_USER_SELECT_BY_ID += "'test001'"; // WHERE user_id='test001'

			// ----------------------------------------------------------
			// ⑥ SELECT 実行
			// ----------------------------------------------------------
			iResult = dac.doSelect(con, strSQL_USER_SELECT_BY_ID);
			if (iResult >= 1) {
				bResult = true;
			}

			// ----------------------------------------------------------
			// ⑦ 結果の取得と表示
			// ----------------------------------------------------------
			if (bResult) {
				// SELECT の結果は DbTableEntity の rows に格納されている
				List<Map<String, Object>> rows = ite.getTbl();
				for (Map<String, Object> row : rows) {
					System.out.println(row); // キーと値の組み合わせを標準出力へ表示
				}
			} else {
				System.out.println("データ取得に失敗しました。");
			}

		} catch (Exception e) {

			// 例外発生時はログ出力＋ロールバック
			e.printStackTrace();
			try {
				if (con != null)
					con.rollback();
			} catch (Exception ignore) {
			}

		} finally {

			// コネクションのクローズ
			try {
				if (con != null)
					con.close();
			} catch (Exception ignore) {
			}
		}
	}
}
