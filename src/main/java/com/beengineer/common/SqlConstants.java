package com.beengineer.common;

/********************************************************************************
 * SQL定数インターフェース
 *
 * 各種SQL文を定数として定義しています。
 * アプリケーション内で共通して利用されるSQL文を一元管理します。
 *
 * 履歴:
 *   V1.0  2025/07/15  Bepro  新規開発
 *******************************************************************************/
public interface SqlConstants {

	/************************************
	 * ADM_LOGIN_EVT checkAuth()
	 ************************************/
	static final String SQL_ADM_LOGIN_METHOD_AUTH_01 = "SELECT "
			+ " T1.ctid, "
			+ " T1.auth_id,"
			+ " T1.client_id,"
			+ " T2.charge_id, "
			+ " T2.charge_name, "
			+ " T2.charge_pw, "
			+ " T2.e_mail_add, "
			+ " T1.create_dt, "
			+ " T1.create_user, "
			+ " T1.update_dt, "
			+ " T1.update_user, "
			+ " T1.del_flg, "
			+ " T2.remark "
			+ " FROM "
			+ " join_trn T1 join charge_mst T2 on( T1.charge_id = T2.charge_id)"
			+ " Where T2.e_mail_add = '";

	/************************************
	 * ADM_APP_EVT getList()
	 ************************************/
	static final String SQL_ADM_APP_METHOD_GET_LIST_01 = "SELECT "
			+ " ctid,"
			+ " app_id,"
			+ " app_name,"
			+ " app_kana,"
			+ " create_dt,"
			+ " create_user,"
			+ " update_dt,"
			+ " update_user,"
			+ " del_flg,"
			+ " remark"
			+ " FROM "
			+ " app_mst"
			+ " Where del_flg = '0'";
	
	/************************************
	 * ADM_APP_EVT doDelete()
	 ************************************/
	static final String SQL_ADM_APP_METHOD_DO_DELETE_01 = "SELECT "
			+ " ctid "
			+ " from "
			+ " app_mst"
			+ " Where del_flg = '0'";
    /************************************
     * ここまで
     ************************************/
}
