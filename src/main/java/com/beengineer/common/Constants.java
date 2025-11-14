package com.beengineer.common;

/********************************************************************************
 * 定数インターフェース
 *
 * アプリケーション内で共通して使用する各種定数を定義しています。
 *
 * 履歴:
 *   V1.0  2025/07/15  Bepro  新規開発
 *******************************************************************************/
public interface Constants {

	/************************************
	 * 各種定数データ
	 ************************************/
	// CONTENT_TYPE
	static final String CONTENT_TYPE = "text/html;charset=" + "UTF-8";
	// JOB_ID
	static final String JOB_ID = "job_id";
	// LOGIN_ID
	static final String LOGIN_ID = "login_id";
	// LOGIN_NAME
	static final String LOGIN_NAME = "login_name";
	// LOGIN_CLIENT_ID
	static final String LOGIN_CLIENT_ID = "login_client_id";
	// LOGIN_NAME
	static final String LOGIN_AUTH_ID = "login_auth_id";
	// ACT_FLG
	static final String ACT_FLG = "act_flg";
	// ACTION
	static final String ACTION = "action";
	// ZONE
	static final String DISPLAY = "display";
	// ZONE_INFO
	static final String DISPLAY_NUMBER = "display_number";
	// 会社
	static final String CORP = "corp";
	// 会社取得キー
	static final String CORP_NAME = "corp_name";
	// 空文字
	static final String EMPTY = "";
	// 未選択
	static final String NO_CHOICE = "-1";
	// ページカウント
	static final String PAGE_COUNT = "pageCount";
	// メールアドレス
	static final String E_MAIL_ADD = "e_mail_add";
	// 消費税率
	static final String TAX = "tax";
	// 消費税率１
	static final String TAX1 = "tax1";
	// 消費税率２
	static final String TAX2 = "tax2";
	// 消費税率１適用日
	static final String TAX1_EFFECTIVE_DT = "tax1_effective_dt";
	// 消費税率２適用日
	static final String TAX2_EFFECTIVE_DT = "tax2_effective_dt";
	// 管理費
	static final String EXPENCES = "expences";
	// 間接費
	static final String OVERHEAD_EXPENCES = "overhead_expences";
	// 販管費
	static final String GENERAL_EXPENCES = "general_expences";
	// 属性データ
	public static final String ATT_MST = "att_mst";
	// 前後区分データ
	public static final String F_B_MST = "f_b_mst";

	/************************************
	 * 文字列定数
	 ************************************/
	static final String ZERO = "0"; // ゼロ

	/************************************
	 * 整数定数
	 ************************************/
	static final int N_Zero = 0;
	static final int N_One = 1;
	static final int N_Two = 2;
	static final int N_Three = 3;
	static final int N_Four = 4;
	static final int N_Five = 5;
	static final int N_Six = 6;
	static final int N_Seven = 7;
	static final int N_Eight = 8;
	static final int N_Nine = 9;
	static final int N_Ten = 10;

	/************************************
	 * パラメータ
	 ************************************/
	// 「サブミット」パラメータ
	static final String P_SUBMIT = "SUBMIT";
	// 「リセット」パラメータ
	static final String P_RESET = "RESET";
	// 「戻り」パラメータ
	static final String P_BACK = "BACK";
	// 要件管理からの「戻り」パラメータ
	static final String P_BACK_FROM_REQ_TO_JOB = "BACK_FROM_REQ_TO_JOB";
	// スキル管理からの「戻り」パラメータ
	static final String P_BACK_FROM_SKILL = "BACK_FROM_SKILL";
	// フェーズ表示順制御からのパラメータ
	static final String P_BACK_FROM_ORDER = "BACK_FROM_ORDER";
	// アサイン一覧からの「戻り」パラメータ
	static final String P_BACK_FROM_REQ_TO_ASSIGN = "BACK_FROM_REQ_TO_ASSIGN";
	// 企業詳細からの「戻り」パラメータ
	static final String P_BACK_FROM_CLIENT_TO_STRUCT = "BACK_FROM_CLIENT_TO_STRUCT";
	// 第一階層詳細からの「戻り」パラメータ
	static final String P_BACK_FROM_DIVISION_TO_STRUCT = "BACK_FROM_DIVISION_TO_STRUCT";
	// 第二階層詳細からの「戻り」パラメータ
	static final String P_BACK_FROM_DEPT_TO_STRUCT = "BACK_FROM_DEPT_TO_STRUCT";
	// 第三階層詳細からの「戻り」パラメータ
	static final String P_BACK_FROM_SECT_TO_STRUCT = "BACK_FROM_SECT_TO_STRUCT";
	// 削除メソッド分岐識別子（企業）
	static final String P_CLIENT = "CLIENT";
	// 削除メソッド分岐識別子（第一階層）
	static final String P_DIVISION = "DIVISION";
	// 削除メソッド分岐識別子（第二階層）
	static final String P_DEPT = "DEPT";
	// 削除メソッド分岐識別子（第三階層）
	static final String P_SECT = "SECT";

	/************************************
	 * 分岐定数
	 ************************************/
	static final int N_LIST = 0; // 一覧
	static final int N_INST = 1; // 詳細（新規）
	static final int N_EDIT = 2; // 詳細（編集）
	static final int N_EXEC_INST = 3; // 登録処理
	static final int N_EXEC_EDIT = 4; // 更新処理
	static final int N_EXEC_DEL = 5; // 削除処理
	static final int N_PAGING = 6; // 改ページ処理

	/************************************
	 * アクション定数
	 ************************************/
	static final String LIST = "LIST";
	static final String SEARCH = "SEARCH";
	static final String VIEW = "VIEW";
	static final String INST = "INST";
	static final String EDIT = "EDIT";
	static final String EXEC_INST = "EXEC_INST";
	static final String EXEC_EDIT = "EXEC_EDIT";
	static final String DELETE = "DELETE";
	static final String ONCHANGE = "ONCHANGE";

	/************************************
	 * データベース名
	 ************************************/
	// アプリケーションマスタ
	static final String APP_MST = "app_mst";

	/************************************
	 * 共通モジュール名
	 ************************************/
	static final String CONSTANTS = "Constants";
	static final String DATABEAN = "DataBean";
	static final String INI_FILE_READ = "IniFileRead";
	static final String I_TABLE_ENTITY = "ITableEntity";
	static final String SQL_CONSTANTS = "SqlConstants";
	static final String SYSTEM_INFO = "SystemInfo";
	static final String FIELDS_CONSTANTS = "FieldsConstants";

	/************************************
	 * 共通データ操作モジュール
	 ************************************/
	static final String DBACCESSCONTROLLER = "DbAccessController";
	static final String DBACCESSDELETE = "DbAccessDelete";
	static final String DBACCESSFACTORY = "DbAccessFactory";
	static final String DBACCESSINSERT = "DbAccessInsert";
	static final String DBACCESSSELECT = "DbAccessSelect";
	static final String DBACCESSSELECTTYPE = "DbAccessSelectType";
	static final String DBACCESSUPDATE = "DbAccessUpdate";
	static final String DBCONNECTIONPOOL = "DbConnectionPool";
	static final String DBTABLEENTITY = "DbTableEntity";
	static final String IDBACCESSBEAN = "IDbAccessBean";

	/************************************
	 * 検索条件定数（サイドメニューからの検索時）
	 ************************************/
	//アプリケーション名
	static final String PART_APP_NAME = "part_app_name";

	/************************************
	 * 共通例外クラス
	 ************************************/
	static final String SYSTEM_EXCEPTION = "SystemException";

	/************************************
	 * 共通ログクラス
	 ************************************/
	static final String LOGGER = "Logger";
	static final String LOG_WRITER = "LogWriter";

	/************************************
	 * 共通ユーティリティ
	 ************************************/
	// レジスターユーティリティ
	static final String REG_UTIL = "RegsUtil";
	// ダウンロードユーティリティ
	static final String TEXT_OUT = "TextOut";
	// HRM専用ユーティリティ
	static final String HRM_UTIL = "HrmUtil";
	// GPM専用ユーティリティ
	static final String GPM_UTIL = "HrmUtil";
	// チェックユーティリティ
	static final String CHECK_UTIL = "CheckUtil";
	// ランダムパスワード生成ユーティリティ
	static final String RANDOM_PASSWORD = "RandomPassword";
	// 文字列ユーティリティ
	static final String STRING＿UTIL = "StringUtil";

	/************************************
	 * コントローラ
	 ************************************/
	static final String ADM_LOGIN_CTL = "ADM_LOGIN_CTL";
	static final String ADM_APP_CTL = "Adm_App_Ctl";

	/************************************
	 * プロジェクト名
	 ************************************/
	// ヒューマンリソースマネジメントプロジェクト
	static final String HRM = "	Hrm	";
	// グロスプロフィットマネジメントプロジェクト
	static final String GPM = "	Gpm	";

	/************************************
	 * 成否判定
	 ************************************/
	static final String W_SUCCESS = "	成功";
	static final String W_FAIL = "	失敗	";
	static final String W_SUCCESS_INS = "msg_010";
	static final String W_SUCCESS_UPD = "msg_011";
	static final String W_SUCCESS_DEL = "msg_012";

	/************************************
	 * 真偽判定
	 ************************************/
	static final boolean W_TRUE = true;
	static final boolean W_FALSE = false;

	/************************************
	 * メッセージ
	 ************************************/
	static final String M_ROLLBACK_FAIL = " rollbackに失敗しました！！ ";
	static final String M_SYSTEM_EXCEPTION_OCCUR = " SystemExceptionが発生しました！！ ";
	static final String M_SQL_EXCEPTION_OCCUR = " SQLExceptionが発生しました！！ ";
	static final String M_SQL_PARSE_EXCEPTION_OCCUR = " ParseLExceptionが発生しました！！ ";
	// アカウント、またはパスワードが違います。
	static final String M_ERR_000 = "err_000";
	// この企業に紐も付く第一階層、またはスタッフが存在するため、削除できません。
	static final String M_ERR_001 = "err_001";
	// この第一階層に紐も付く第二階層、またはスタッフが存在するため、削除できません。
	static final String M_ERR_002 = "err_002";
	// この第二階層に紐も付く第三階層、またはスタッフが存在するため、削除できません。
	static final String M_ERR_003 = "err_003";
	// この第三階層に紐も付くスタッフが存在するため、削除できません。
	static final String M_ERR_004 = "err_004";
	// 一意制約違反が発生しました。
	static final String M_ERR_005 = "err_005";
	// 他者によりデータが変更（更新 or 削除）されています。
	static final String M_ERR_006 = "err_006";
	// 使用可能なIDの上限を超えました。
	static final String M_ERR_007 = "err_007";
	// 希望就業期間が他の登録情報と重なっています。
	static final String M_ERR_008 = "err_008";
	// 実行処理に失敗しました。
	static final String M_ERR_009 = "err_009";
	// 該当案件へのアサイン担当者が存在しません
	static final String M_ERR_010 = "err_010";
	// 実績データが存在しません。
	static final String M_ERR_011 = "err_011";
	// 指定した階層値に誤りがあります。
	static final String M_ERR_012 = "err_012";
	// この企業に紐も付く案件が存在するため削除できません。
	static final String M_ERR_013 = "err_013";
	// 当階層（第一階層）に紐も付く案件が存在するため削除できません。
	static final String M_ERR_014 = "err_014";
	// 当階層（第二階層）に紐も付く案件が存在するため削除できません。
	static final String M_ERR_015 = "err_015";
	// 当階層（第三階層）に紐も付く案件が存在するため、削除できません。
	static final String M_ERR_016 = "err_016";
	// メンバ情報を更新しました。
	static final String M_ERR_023 = "err_023";
	// 更新対象情報がありません。
	static final String M_ERR_024 = "err_024";
	//この募集用件に紐も付くスタッフが存在するため、削除できません 。
	static final String M_ERR_027 = "err_027";
	//この案件に紐も付く担当者が存在するため、削除できません。
	static final String M_ERR_028 = "err_028";
	// この案件に紐も付く要件情報が存在するため、削除できません。
	static final String M_ERR_029 = "err_029";
	// forward faild :
	static final String M_ERR_030 = "err_030";
	// get Request dispatchr faild........
	static final String M_ERR_031 = "err_031";
	// get UrlName faild........
	static final String M_ERR_032 = "err_032";
	// セッションとの同期に失敗しました。データが取得できません。
	static final String M_ERR_040 = "err_040";
	// この項目を使用しているスタッフが存在するため削除できません。
	static final String M_ERR_050 = "err_050";
	// この項目を使用している企業が存在するため削除できません。
	static final String M_ERR_051 = "err_051";
	// この項目を使用している案件が存在するため削除できません。
	static final String M_ERR_052 = "err_052";
	// この項目を使用している要件が存在するため削除できません。
	static final String M_ERR_053 = "err_053";
	// この項目を使用している支出データが存在するため削除できません。
	static final String M_ERR_054 = "err_054";

	/************************************
	 * ログパラメータ
	 ************************************/
	// スタート
	static final String LOG_START = "☆ START！！ ";
	// エンド
	static final String LOG_END = "☆ END！！ ";
	// 機能間のエラーメッセージ用キー
	static final String ERR_MASSAGE = "err_Massage";
	// CHECK_ID
	static final String CHECK_ID = "check_id";
	// Homeへの戻り遷移
	static final String BACK_HOME = "/Adm_Login_Ctl?job_id=1";
	// ファイル確認
	static final String FILE_EXISTS = "FileExists!!";
	// ファイル削除
	static final String FILE_DELETED = "FileDeleted!!";
	// 改行
	static final String NEW_LINE = "\n";

}
