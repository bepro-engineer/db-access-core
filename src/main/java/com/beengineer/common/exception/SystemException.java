package com.beengineer.common.exception;
/********************************************************************************
 * システム例外クラス
 *
 * アプリケーションで発生するシステム例外を表現し、
 * エラーメッセージやエラーコードの管理、ログ出力を行います。
 *
 * 履歴:
 *   V1.0  2025/07/15  Bepro  新規開発
 *******************************************************************************/
import com.beengineer.common.log.Logger;
public class SystemException extends Exception {

    private static String errCode = new String("");

    /**
     * デフォルトコンストラクタ
     *
     * 詳細メッセージを指定せずに SystemException を構築します。
     */
    public SystemException() {
        super();
    }

    /**
     * メッセージ指定コンストラクタ
     *
     * 指定された詳細メッセージを持つ SystemException を構築します。
     *
     * @param strMessage エラーメッセージ
     */
    public SystemException(String strMessage) {
        super(strMessage);
        String str = makeErrorInfo("999", strMessage);
        Logger.out(
            Logger.WARN,
            "Unknown-Class#Unknown-Method",
            "Unknown-User",
            str);
    }

    /**
     * エラーコード・メッセージ指定コンストラクタ
     *
     * 指定されたエラーコードと詳細メッセージを持つ SystemException を構築します。
     *
     * @param code エラーコード
     * @param strMessage エラーメッセージ
     */
    public SystemException(String code, String strMessage) {
        super(strMessage);
        errCode = code;
        String str = makeErrorInfo(code, strMessage);
        Logger.out(
            Logger.WARN,
            "Unknown-Class#Unknown-Method",
            "Unknown-User",
            str);
    }

    /**
     * クラス名・エラーコード・メッセージ指定コンストラクタ
     *
     * 指定されたクラス名（メソッド名）・エラーコード・詳細メッセージを持つ SystemException を構築します。
     *
     * @param className クラス名＃メソッド名
     * @param code エラーコード
     * @param strMessage エラーメッセージ
     */
    public SystemException(String className, String code, String strMessage) {
        super(strMessage);
        errCode = code;
        String str = makeErrorInfo(code, strMessage);
        Logger.out(Logger.WARN, className, "Unknown-User", str);
    }

    /**
     * クラス名・ユーザ名・エラーコード・メッセージ指定コンストラクタ
     *
     * 指定されたクラス名（メソッド名）、ユーザ名、エラーコード、詳細メッセージを持つ SystemException を構築します。
     *
     * @param className クラス名＃メソッド名
     * @param userName ユーザ名
     * @param code エラーコード
     * @param strMessage エラーメッセージ
     */
    public SystemException(
        String className,
        String userName,
        String code,
        String strMessage) {
        super(strMessage);
        errCode = code;
        String str = makeErrorInfo(code, strMessage);
        Logger.out(Logger.WARN, className, userName, str);
    }

    /**
     * エラー情報作成メソッド
     *
     * エラー情報の形式：
     *   エラーコード（固定長） + 半角スペース + エラーメッセージ
     *
     * エラーコードの分類例：
     *   001～100 --- システム系
     *   101～200 --- DBアクセス系
     *   201～300 --- ファイルアクセス系
     *   301～999 --- 予備
     *
     * @param errorCode エラーコード
     * @param strMessage エラーメッセージ
     * @return 生成したエラー情報文字列
     */
    private String makeErrorInfo(String errorCode, String strMessage) {
        StringBuffer strBuff = new StringBuffer();
        strBuff.append(errorCode);
        strBuff.append(" ");
        strBuff.append(strMessage);

        return strBuff.toString();
    }

    /**
     * エラーコード取得メソッド
     *
     * @return エラーコード文字列
     */
    public String getErrCode() {
        return errCode;
    }
}
