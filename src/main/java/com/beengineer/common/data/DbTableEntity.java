package com.beengineer.common.data;

/********************************************************************************
 * テーブル情報を格納するクラス
 *
 * DBより取得したテーブルデータをallField（Hashtable）に格納するためのクラスです。
 *
 * 履歴:
 *   V1.0  2025/07/15  Bepro  新規開発
 *******************************************************************************/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.beengineer.common.ITableEntity;
import com.beengineer.common.exception.SystemException;
import com.beengineer.common.log.Logger;

public class DbTableEntity implements ITableEntity {

    /**
     * テーブル名
     */
    private String table = null;

    private Error error = null;

    /**
     * DB実データ取得時の件数
     */
    private int select_idx = (-1);

    /**
     * 編集フラグ
     * 変更が入った場合にフラグを立てる
     */
    private boolean[] edit_flg = null;

    /**
     * 削除フラグ
     */
    private boolean[] del_flg = null;

    /**
     * 登録フラグ
     */
    private boolean[] ins_flg = null;

    /**
     * 更新フラグ
     */
    private boolean[] up_flg = null;

    /**
     * selectシンクロフラグ
     */
    private boolean select_sync_flg = false;

    /**
     * 全項目リストのデータ型を格納
     * HashMap
     */
    private HashMap fieldType = new HashMap();

    /**
     * 項目ゲージ
     * ArrayList
     */
    private ArrayList tbl = new ArrayList();

    /**
     * コンストラクター
     *
     * @param table テーブル名
     */
    public DbTableEntity(String table) {
        this.table = table;
    }

    /**
     * 要素格納ゲージの件数取得
     *
     * @return 要素数
     */
    protected int getTblCount() {
        return (tbl.size());
    }

    /**
     * 指定インデックスの要素（行）を取得
     *
     * @param idx 行番号（配列番号）
     * @return HashMap 項目値
     */
    protected HashMap getField(int idx) {
        HashMap hm = (HashMap) tbl.get(idx);
        return (hm);
    }

    /**
     * 格納項目のキーを取得
     *
     * @return ArrayList 全項目キー
     */
    protected ArrayList getFieldKeys() {
        ArrayList keys = new ArrayList();
        Iterator itr = fieldType.keySet().iterator();
        while (itr.hasNext()) {
            keys.add(itr.next().toString());
        }
        return (keys);
    }

    /**
     * データ型格納項目配列を取得
     *
     * @return HashMap データ型格納項目配列
     */
    protected HashMap getFieldTypeList() {
        return (fieldType);
    }

    /**
     * キーに対応したデータ型を取得
     *
     * @param key 項目キー
     * @return String データ型
     */
    protected String getFieldType(String key) {
        return (fieldType.get(key).toString());
    }

    /**
     * 指定インデックスの変更状態を設定
     *
     * @param e_flg フラグ値
     * @param idx 行番号（配列番号）
     */
    protected void setEditFlg(boolean e_flg, int idx) {
        if ((this.edit_flg == null) || (this.edit_flg.length <= idx)) {
            addArrayEditFlg();
        }
        this.edit_flg[idx] = e_flg;
    }

    /**
     * 指定インデックスの登録状態設定
     *
     * @param i_flg フラグ値
     * @param idx 行番号（配列番号）
     */
    protected void setInsFlg(boolean i_flg, int idx) {
        this.ins_flg[idx] = i_flg;
    }

    /**
     * 指定インデックスの更新状態設定
     *
     * @param u_flg フラグ値
     * @param idx 行番号（配列番号）
     */
    protected void setUpdFlg(boolean u_flg, int idx) {
        this.up_flg[idx] = u_flg;
    }

    /**
     * 指定インデックスの変更状態取得
     *
     * @param idx 行番号（配列番号）
     * @return boolean フラグ値
     */
    protected boolean getEditFlg(int idx) {
        if (this.edit_flg == null)
            return false;
        return this.edit_flg[idx];
    }

    /**
     * 変更状態配列の取得
     *
     * @return boolean[] 編集フラグ配列
     */
    protected boolean[] getArrayEditFlg() {
        return this.edit_flg;
    }

    protected boolean[] getArrayInsFlg() {
        return this.ins_flg;
    }

    protected boolean[] getArrayUpdFlg() {
        return this.up_flg;
    }

    protected boolean[] getArrayDelFlg() {
        return this.del_flg;
    }

    /**
     * 変更状態配列の設定
     *
     * @param edit_flg 編集フラグ配列
     */
    protected void setArrayEditFlg(boolean[] edit_flg) {
        this.edit_flg = edit_flg;
    }

    /**
     * 変更状態配列に要素を追加（編集フラグ）
     */
    private void addArrayEditFlg() {
        if (this.edit_flg == null) {
            this.edit_flg = new boolean[1];
            this.edit_flg[0] = false;
            return;
        }
        int len = this.edit_flg.length;
        boolean[] tmp = new boolean[len];
        for (int i = 0; i < len; i++) {
            tmp[i] = this.edit_flg[i];
        }
        this.edit_flg = new boolean[len + 1];
        for (int i = 0; i < len; i++) {
            this.edit_flg[i] = tmp[i];
        }
        this.edit_flg[len] = false;
    }

    /**
     * 削除フラグを設定
     *
     * @param del_flg フラグ値
     * @param idx 行番号（配列番号）
     */
    protected void setDelFlg(boolean del_flg, int idx) {
        this.del_flg[idx] = del_flg;
    }

    /**
     * 削除フラグの取得
     *
     * @param idx 行番号（配列番号）
     * @return boolean フラグ値
     */
    protected boolean getDelFlg(int idx) {
        boolean ret = false;
        Logger.out(
            Logger.DEBUG3,
            "DmssTableEntity#getDelFlg()",
            "sys",
            "index=" + idx);
        ret = this.del_flg[idx];
        return ret;
    }

    /**
     * 削除フラグ配列に要素を追加
     */
    private void addArrayDelFlg() {
        if (this.del_flg == null) {
            this.del_flg = new boolean[1];
            this.del_flg[0] = false;
            return;
        }
        int len = this.del_flg.length;
        boolean[] tmp = new boolean[len];
        for (int i = 0; i < len; i++) {
            tmp[i] = this.del_flg[i];
        }
        this.del_flg = new boolean[len + 1];
        for (int i = 0; i < len; i++) {
            this.del_flg[i] = tmp[i];
        }
        this.del_flg[len] = false;
    }

    /**
     * 挿入フラグ配列に要素を追加
     */
    private void addArrayInsFlg() {
        if (this.ins_flg == null) {
            this.ins_flg = new boolean[1];
            this.ins_flg[0] = false;
            return;
        }
        int len = this.ins_flg.length;
        boolean[] tmp = new boolean[len];
        for (int i = 0; i < len; i++) {
            tmp[i] = this.ins_flg[i];
        }
        this.ins_flg = new boolean[len + 1];
        for (int i = 0; i < len; i++) {
            this.ins_flg[i] = tmp[i];
        }
        this.ins_flg[len] = false;
    }

    /**
     * 更新フラグ配列に要素を追加
     */
    private void addArrayUpdFlg() {
        if (this.up_flg == null) {
            this.up_flg = new boolean[1];
            this.up_flg[0] = false;
            return;
        }
        int len = this.up_flg.length;
        boolean[] tmp = new boolean[len];
        for (int i = 0; i < len; i++) {
            tmp[i] = this.up_flg[i];
        }
        this.up_flg = new boolean[len + 1];
        for (int i = 0; i < len; i++) {
            this.up_flg[i] = tmp[i];
        }
        this.up_flg[len] = false;
    }
    /**
     * 指定インデックスの登録フラグ取得
     *
     * @param index 行番号（配列番号）
     * @return boolean 登録フラグ
     */
    protected boolean getInsFlg(int index) {
        if (this.ins_flg == null)
            return false;
        return this.ins_flg[index];
    }

    /**
     * 指定インデックスの更新フラグ取得
     *
     * @param index 行番号（配列番号）
     * @return boolean 更新フラグ
     */
    protected boolean getUpFlg(int index) {
        if (this.up_flg == null)
            return false;
        return this.up_flg[index];
    }

    /**
     * セレクトシンクロフラグの取得
     *
     * @return boolean select_sync_flg セレクト同期フラグ
     */
    protected boolean getSelectFlg() {
        return (this.select_sync_flg);
    }

    /**
     * セレクトシンクロフラグの設定
     *
     * @param select_sync_flg セレクト同期フラグ
     */
    protected void setSelectFlg(boolean select_sync_flg) {
        this.select_sync_flg = select_sync_flg;
    }

    /**
     * フラグ判定チェック
     *
     * @param str 判定対象フラグ名（"edit","ins","upd","del"）
     * @return boolean 判定結果
     */
    protected boolean flgCheck(String str) {
        boolean[] tmp_flg = null;

        if (str.equals("edit")) {
            tmp_flg = edit_flg;
        } else if (str.equals("ins")) {
            tmp_flg = ins_flg;
        } else if (str.equals("upd")) {
            tmp_flg = up_flg;
        } else if (str.equals("del")) {
            tmp_flg = del_flg;
        }

        for (int i = 0; i < tmp_flg.length; i++) {
            if (tmp_flg[i]) {
                return true;
            }
        }

        return false;
    }

    /**
     * すべてのフラグを初期化する
     */
    protected void resetAllFlg() {
        setSelectFlg(false);
        clear();
        edit_flg = null;
        del_flg = null;
        ins_flg = null;
        up_flg = null;
    }

    /**
     * 表示オブジェクトを初期化する
     *
     * @param cnt 件数
     * @return int リザルトコード
     */
    protected int setArray(int cnt) {
        setSelectIdx(cnt);
        if (cnt <= 0) {
            this.del_flg = null;
            this.edit_flg = null;
            this.up_flg = null;
            this.ins_flg = null;
        } else {
            this.del_flg = new boolean[cnt];
            this.edit_flg = new boolean[cnt];
            this.up_flg = new boolean[cnt];
            this.ins_flg = new boolean[cnt];
            for (int i = 0; i < cnt; i++) {
                this.del_flg[i] = false;
                this.edit_flg[i] = false;
                this.up_flg[i] = false;
                this.ins_flg[i] = false;
            }
        }
        return 0;
    }

    /**
     * 配列の追加
     *
     * @return int リザルトコード
     */
    protected int addArray() {
        Logger.out(Logger.DEBUG3, "DmssTableEntity#addArray()", "sys", "START");
        tbl.add(new HashMap());
        addArrayEditFlg();
        addArrayInsFlg();
        addArrayDelFlg();
        addArrayUpdFlg();
        Logger.out(Logger.DEBUG3, "DmssTableEntity#addArray()", "sys", "END");
        return 0;
    }

    /**
     * 実体の初期化
     *
     * @return int リザルトコード
     */
    protected int clear() {
        tbl.clear();
        return 0;
    }

    /**
     * 選択インデックスの取得
     *
     * @return int 選択インデックス
     */
    protected int getSelectIdx() {
        return select_idx;
    }

    /**
     * 選択インデックスの設定
     *
     * @param i 設定値
     */
    protected void setSelectIdx(int i) {
        select_idx = i;
    }

    /**
     * 格納項目数の取得
     *
     * @return int 項目数
     */
    protected int getFieldKeysCount() {
        return (fieldType.size());
    }

    ///////////////////////////////////////////////////////////////////////////
    //  Appからのアクセス専用メソッド
    ///////////////////////////////////////////////////////////////////////////

    /**
     * データ保存
     *
     * @param key 項目キー
     * @param data 登録または更新するデータ
     * @param index 行番号（配列番号）
     * @return int リザルトコード（成功:1）
     * @throws SystemException
     */
    public int setValue(String key, String data, int index) throws SystemException {
        Logger.out(Logger.DEBUG3, "DbTableEntity#setValue()", "sys", "START");
        HashMap fields = null;
        int ret = 1;

        if (index < 0 || this.getTblCount() < 0) {
            throw new SystemException("DbTableEntity#setValue()",
                    "sys",
                    "17003",
                    "インデックスが領域外");
        }

        // 登録・変更判定
        if (index > this.getSelectIdx() - 1) {
            // 登録処理
            if (index > getTblCount() - 1) {
                for (int i = getTblCount() - 1; i < index; i++) {
                    addArray();
                }
            }

            fields = this.getField(index);
            synchronized (fields) {
                fields.put(key, data);
                setEditFlg(true, index);
                setInsFlg(true, index);
                setDelFlg(false, index);
                setUpdFlg(false, index);
            }
        } else {
            // 更新処理
            fields = this.getField(index);
            synchronized (fields) {
                fields.put(key, data);
                setEditFlg(true, index);
                setUpdFlg(true, index);
            }
        }

        Logger.out(Logger.DEBUG3, "DbTableEntity#setValue()", "sys", "END ret=" + ret);
        return ret;
    }

    /**
     * 削除フラグを設定する
     *
     * @param idx 行番号（配列番号）
     */
    public void setDelete(int idx) {
        Logger.out(
            Logger.DEBUG3,
            "DbableEntity#setDelete()",
            "sys",
            "START");
        setEditFlg(true, idx);
        setDelFlg(true, idx);
        Logger.out(Logger.DEBUG3, "DbTableEntity#setDelete()", "sys", "END");
    }

    /**
     * 全要素格納ゲージの取得
     *
     * @return ArrayList 全項目値
     */
    public ArrayList getTbl() {
        return (tbl);
    }

    /**
     * 全テーブルデータの設定
     *
     * @param tbl セットするデータリスト
     */
    public void setTbl(ArrayList tbl) {
        this.tbl = tbl;
    }
}