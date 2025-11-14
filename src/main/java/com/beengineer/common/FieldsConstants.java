package com.beengineer.common;

public interface FieldsConstants extends Constants {

	/************************************
	 * MST_USER
	 ************************************/
	static final String FIELDS_MST_USER_ID = "user_id";
	static final String FIELDS_MST_USER_NAME = "user_name";
	static final String FIELDS_MST_USER_EMAIL = "e_mail";
	static final String FIELDS_MST_USER_CREATE_DT = "create_dt";
	static final String FIELDS_MST_USER_CREATE_USER = "create_user";
	static final String FIELDS_MST_USER_UPDATE_DT = "update_dt";
	static final String FIELDS_MST_USER_UPDATE_USER = "update_user";
	static final String FIELDS_MST_USER_DEL_FLG = "del_flg";
	static final String FIELDS_MST_USER_REMARK = "remark";

	static final String[] FIELDS_USER_MST = { FIELDS_MST_USER_ID,
			FIELDS_MST_USER_NAME, FIELDS_MST_USER_EMAIL,
			FIELDS_MST_USER_CREATE_DT, FIELDS_MST_USER_CREATE_USER,
			FIELDS_MST_USER_UPDATE_DT, FIELDS_MST_USER_UPDATE_USER,
			FIELDS_MST_USER_DEL_FLG, FIELDS_MST_USER_REMARK
	};

	static final String[] FIELDS_USER_MST_JP = {
			"ユーザーID",
			"ユーザー名",
			"メールアドレス",
			"登録日",
			"登録者",
			"更新日",
			"更新者",
			"論理削除フラグ",
			"備考" };

	/************************************
	 * APP_MST
	 ************************************/
	static final String FIELDS_APP_MST_APP_ID = "app_id";
	static final String FIELDS_APP_MST_APP_NAME = "app_name";
	static final String FIELDS_APP_MST_APP_KANA = "app_kana";
	static final String FIELDS_APP_MST_CREATE_DT = "create_dt";
	static final String FIELDS_APP_MST_CREATE_USER = "create_user";
	static final String FIELDS_APP_MST_UPDATE_DT = "update_dt";
	static final String FIELDS_APP_MST_UPDATE_USER = "update_user";
	static final String FIELDS_APP_MST_REMARK = "remark";
	static final String FIELDS_APP_MST_DEL_FLG = "del_flg";
	static final String[] FIELDS_APP_MST = { FIELDS_APP_MST_APP_ID,
			FIELDS_APP_MST_APP_NAME, FIELDS_APP_MST_APP_KANA,
			FIELDS_APP_MST_CREATE_DT, FIELDS_APP_MST_CREATE_USER,
			FIELDS_APP_MST_UPDATE_DT, FIELDS_APP_MST_UPDATE_USER,
			FIELDS_APP_MST_REMARK, FIELDS_APP_MST_DEL_FLG };

	static final String[] FIELDS_APP_MST_JP = {
			"アプリケーションID",
			"アプリケーション名",
			"アプリケーション名（カナ）",
			"登録日",
			"登録者",
			"更新日",
			"更新者",
			"論理削除フラグ",
			"備考" };
}