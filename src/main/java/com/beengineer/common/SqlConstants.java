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
	static final String SQL_USER_SELECT_BY_ID = "SELECT "
			+ " ctid, "
			+ " user_id,"
			+ " user_name,"
			+ " company_id,"
			+ " create_dt, "
			+ " create_user, "
			+ " update_dt, "
			+ " update_user, "
			+ " del_flg, "
			+ " remark "
			+ " FROM "
			+ " user_mst"
			+ " Where user_id= ";

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
