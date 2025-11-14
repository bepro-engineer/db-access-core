package com.sample;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.beengineer.common.data.DbAccessController;
import com.beengineer.common.data.DbConnectionPool;
import com.beengineer.common.data.DbTableEntity;
import com.beengineer.common.log.Logger;

public class Sample1 {

	private static final boolean W_FALSE = false;
	private static final int N_Zero = 0;
	private static final String USER_TBL = "user_mst";

	public static void main(String[] args) {

		boolean bResult = W_FALSE;
		DbAccessController dac = new DbAccessController(USER_TBL);
		DbConnectionPool pool = DbConnectionPool.getInstance();
		Connection con = null;

		Logger.init();

		try {
			con = pool.getConnection();
			con.setAutoCommit(W_FALSE);

			Map<String, String> params = new HashMap<>();
			params.put("user_id", "test001");
			params.put("user_name", "山田太郎");
			params.put("e_mail", "test@example.co.jp");
			params.put("password", "P@ssW0rd");

			DbTableEntity ite = dac.getDbTableEntity();
			//			ite.resetAllFlg();

			ite.setValue("user_id", params.get("user_id"), N_Zero);
			ite.setValue("user_name", params.get("user_name"), N_Zero);
			ite.setValue("e_mail", params.get("e_mail"), N_Zero);
			ite.setValue("password", params.get("password"), N_Zero);

			int iResult = dac.doExec(con, USER_TBL);

			if (iResult > N_Zero) {
				con.commit();
				System.out.println("INSERT結果: " + iResult);
			} else {
				con.rollback();
				System.out.println("INSERT失敗");
			}

			// ----------------------------------------------------------
			// SQL構築（Adm_App_Ctlの規約に準拠）
			// ----------------------------------------------------------
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM ").append(USER_TBL);
			sql.append(" WHERE user_id='test001'");

			// ----------------------------------------------------------
			// 実行％
			// ----------------------------------------------------------
			iResult = dac.doSelect(con, sql.toString());
			if (iResult >= 1) {
				bResult = true;
			}
			// ----------------------------------------------------------
			// 結果処理
			// ----------------------------------------------------------
			if (bResult) {
				List<Map<String, Object>> rows = ite.getTbl();
				for (Map<String, Object> row : rows) {
					System.out.println(row);
				}
			} else {
				System.out.println("データ取得に失敗しました。");
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (con != null)
					con.rollback();
			} catch (Exception ignore) {
			}
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (Exception ignore) {
			}
		}
	}
}
