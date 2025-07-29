package com.beengineer.common;

/********************************************************************************
 * Dbアクセス用テーブルエンティティインターフェース
 *
 * アプリケーション側からDbアクセスの際に利用される
 * テーブル情報管理のためのインターフェース定義です。
 *
 * このインターフェースを実装するクラスは、
 * テーブルのデータ格納・取得および、
 * 行単位での削除フラグ設定や特定項目への値設定が可能でなければなりません。
 *
 * 履歴:
 *   V1.0  2005/05/01  Bepro  新規開発
 *******************************************************************************/

import java.util.ArrayList;

import com.beengineer.common.exception.SystemException;

public interface ITableEntity {

    /**
     * テーブルの全データリストを取得します。
     *
     * @return ArrayList テーブルデータのリスト
     */
    public ArrayList getTbl();

    /**
     * 指定したインデックスの行に対して削除フラグを設定します。
     *
     * @param idx 削除対象の行番号（配列番号）
     */
    public void setDelete(int idx);

    /**
     * 指定したインデックスの行の指定項目に値を設定します。
     *
     * @param key 項目キー
     * @param data 設定する値
     * @param index 行番号（配列番号）
     * @return int 処理結果コード（成功/失敗など）
     * @throws SystemException 設定時に発生する例外
     */
    public int setValue(String key, String data, int index) throws SystemException;

    /**
     * テーブルの全データリストを設定します。
     *
     * @param tbl 設定するテーブルデータリスト
     */
    public void setTbl(ArrayList tbl);
}
