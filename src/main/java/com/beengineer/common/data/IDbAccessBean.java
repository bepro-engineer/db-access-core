package com.beengineer.common.data;

/********************************************************************************
 * DataAccessBean実行時のクラス生成インターフェース
 *
 * 各種データアクセス処理を実装するためのインターフェース定義です。
 * 実行メソッドやエラーログ出力、例外処理メソッドを規定します。
 *
 * 履歴:
 *   V1.0  2025/07/15  Bepro  新規開発
 *******************************************************************************/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.beengineer.common.exception.SystemException;

public interface IDbAccessBean {

    /**
     * オブジェクトID
     */
    String object_id = "";

    /**
     * プリコンパイル済みSQLステートメント
     */
    PreparedStatement pstmt = null;

    /**
     * SQL実行メソッド（キー配列あり）
     *
     * @param connection DBコネクション
     * @param strSql SQL文
     * @param dte テーブルエンティティ
     * @param list_keys バインド対象キー配列
     * @return 実行結果件数
     * @throws SystemException 実行時例外
     */
    int execute(Connection connection, String strSql, DbTableEntity dte, ArrayList list_keys) throws SystemException;

    /**
     * SQL実行メソッド（キー配列なし）
     *
     * @param connection DBコネクション
     * @param strSql SQL文
     * @param dte テーブルエンティティ
     * @return 実行結果件数
     * @throws SystemException 実行時例外
     */
    int execute(Connection connection, String strSql, DbTableEntity dte) throws SystemException;

    /**
     * SQLException発生時のエラーログ出力
     *
     * @param e 発生したSQLException
     */
    void abendLog(SQLException e);

    /**
     * SQLException発生時のエラーログ出力（メソッド名付き）
     *
     * @param name 呼び出し元メソッド名
     * @param e 発生したSQLException
     */
    void abendLog(String name, SQLException e);

    /**
     * SQLExceptionからSystemExceptionへの変換・スロー
     *
     * @param name 呼び出し元メソッド名
     * @param e 発生したSQLException
     * @return 変換されたSystemException
     * @throws SystemException 常に例外をスロー
     */
    SystemException doException(String name, SQLException e) throws SystemException;
}
