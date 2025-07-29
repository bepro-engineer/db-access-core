package com.beengineer.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

/********************************************************************************
 * データ保持用Beanクラス
 *
 * 各種データの取得情報やパラメータ、検索条件、メッセージ等を格納します。
 *
 * 主に画面表示データや処理状態の管理に用いられます。
 *
 * 履歴:
 *   V1.0  2025/07/15  Bepro  新規開発
 *******************************************************************************/
public class DataBean implements Constants {

    /************************************
     * 各データ取得情報格納用マップ
     ************************************/
    private HashMap tbls = new HashMap();

    /**
     * キーに対応するデータリストを取得します。
     * キーが存在しない場合は空のArrayListを返します。
     *
     * @param key データの識別キー
     * @return Object データリスト（ArrayList）
     */
    public Object getTbl(String key) {
        ArrayList tmpList;
        if (tbls.get(key) == null) {
            tmpList = new ArrayList();
        } else {
            tmpList = (ArrayList) tbls.get(key);
        }
        return (tmpList);
    }

    /**
     * キーに対応するデータリストを設定します。
     *
     * @param key データの識別キー
     * @param obj 設定するデータオブジェクト
     */
    public void setTbl(String key, Object obj) {
        this.tbls.put(key, obj);
    }

    /************************************
     * 管理系項目
     ************************************/
    // メッセージ
    private String message = EMPTY;

    public String getMessage() {
        return message;
    }

    public void setMessage(String Value) {
        if (Value != null)
            message = Value;
    }

    // アクション
    private String action = EMPTY;

    public String getAction() {
        return action;
    }

    public void setAction(String Value) {
        if (Value != null)
            action = Value;
    }

    // ログインID
    private String login_id = EMPTY;

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String strValue) {
        login_id = strValue;
    }

    /************************************
     * 検索条件項目（1～8）
     ************************************/
    private String where1 = EMPTY;
    public String getWhere1() { return where1; }
    public void setWhere1(String where1) { this.where1 = where1; }

    private String where2 = EMPTY;
    public String getWhere2() { return where2; }
    public void setWhere2(String where2) { this.where2 = where2; }

    private String where3 = EMPTY;
    public String getWhere3() { return where3; }
    public void setWhere3(String where3) { this.where3 = where3; }

    private String where4 = EMPTY;
    public String getWhere4() { return where4; }
    public void setWhere4(String where4) { this.where4 = where4; }

    private String where5 = EMPTY;
    public String getWhere5() { return where5; }
    public void setWhere5(String where5) { this.where5 = where5; }

    private String where6 = EMPTY;
    public String getWhere6() { return where6; }
    public void setWhere6(String where6) { this.where6 = where6; }

    private String where7 = EMPTY;
    public String getWhere7() { return where7; }
    public void setWhere7(String where7) { this.where7 = where7; }

    private String where8 = EMPTY;
    public String getWhere8() { return where8; }
    public void setWhere8(String where8) { this.where8 = where8; }

    /************************************
     * パラメータ項目
     ************************************/
    /**
     * パラメータマップ
     */
    private HashMap params = null;

    public HashMap getParams() {
        return params;
    }

    public void setParams(HashMap params) {
        this.params = params;
    }

    // 検索条件パラメータ
    private HashMap param_value = null;

    public HashMap getParamValue() {
        return param_value;
    }

    public void setParamValue(HashMap param_value) {
        this.param_value = param_value;
    }

    /************************************
     * その他パラメータ
     ************************************/
    private Hashtable zone_info = null;

    public Hashtable getZone() {
        return zone_info;
    }

    public void setZone(Hashtable zone_info) {
        this.zone_info = zone_info;
    }

    // カウント情報
    private String count = "";

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    // エラーメッセージ
    private String err_message = EMPTY;

    public String getErr_Message() {
        return err_message;
    }

    public void setErr_Message(String Value) {
        if (Value != null)
            err_message = Value;
    }
}
