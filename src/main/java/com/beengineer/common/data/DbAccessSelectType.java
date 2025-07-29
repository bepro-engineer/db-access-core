package com.beengineer.common.data;

/********************************************************************************
 * データアクセス型検索実行クラス
 *
 * 本クラスは、指定されたSQLを実行し、データ型の検索結果を
 * DbTableEntity のフィールド型リストに格納する処理を行います。
 *
 * IDbAccessBean インターフェースを実装し、DbAccessFactory から呼び出されます。
 *
 * 履歴:
 *   V1.0  R00  2025/07/15  Bepro  新規開発
 *******************************************************************************/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import com.beengineer.common.exception.SystemException;
import com.beengineer.common.log.Logger;

class DbAccessSelectType implements IDbAccessBean {

    // プリコンパイル済みSQL文を保持するPreparedStatement
    PreparedStatement pstmt = null;

    // 主キー識別子（例：ROWID、OID）
    String object_id;

    /**
     * コンストラクタ
     *
     * @param object_id 主キー種別を識別するID
     */
    protected DbAccessSelectType(String object_id) {
        this.object_id = object_id;
    }

    /**
     * SystemExceptionの生成およびスロー処理
     *
     * <ul>
     *   <li>SQLExceptionをログに出力し、SystemExceptionとして再スローします</li>
     * </ul>
     *
     * @param name 呼び出し元メソッド名
     * @param e 発生したSQLException
     * @return SystemException ラップされた例外
     * @throws SystemException 常に例外をスローします
     */
    public SystemException doException(String name, SQLException e)
            throws SystemException {
        this.abendLog(name, e);
        throw new SystemException("DbAccessSelectType#bendLog() " + name + "()", "unknown", "11004", "SQL実行エラー:" + e.getErrorCode());
    }

    /**
     * エラーログ出力処理（メソッド名あり）
     *
     * @param name 呼び出し元メソッド名
     * @param e 発生したSQLException
     */
    public void abendLog(String name, SQLException e) {
        while (e != null) {
            Logger.out(Logger.ERROR, "DbAccessSelectType#bendLog() " + name + "()", "sys", "mes=" + e.getMessage());
            Logger.out(Logger.ERROR, "DbAccessSelectType#bendLog() " + name + "()", "sys", "stat=" + e.getSQLState());
            Logger.out(Logger.ERROR, "DbAccessSelectType#bendLog() " + name + "()", "sys", "code=" + String.valueOf(e.getErrorCode()));
            e = e.getNextException();
        }
    }

    /**
     * エラーログ出力処理（メソッド名なし簡易版）
     *
     * @param e 発生したSQLException
     */
    public void abendLog(SQLException e) {
        while (e != null) {
            Logger.out(Logger.ERROR, "DbAccessSelectType#abendLog() ", "system", e.getMessage());
            Logger.out(Logger.ERROR, "DbAccessSelectType#abendLog() ", "system", e.getSQLState());
            Logger.out(Logger.ERROR, "DbAccessSelectType#abendLog() ", "system", String.valueOf(e.getErrorCode()));
            e = e.getNextException();
        }
    }

    /**
     * データ型検索SQLの実行メソッド
     *
     * <ul>
     *   <li>指定されたSQL文を実行し、結果の項目名称と型を DbTableEntity に格納します</li>
     * </ul>
     *
     * @param connection DBコネクション
     * @param strSql 実行するSQL文
     * @param dte フィールド型を格納するテーブルエンティティ
     * @return 検索結果件数（本処理では常に0）
     * @throws SystemException SQL実行時の例外
     */
    public int execute(Connection connection, String strSql,
            DbTableEntity dte) throws SystemException {
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int iResult = 0;
        try {
            //-------------------------------------------------
            // 項目タイプ検索処理
            //-------------------------------------------------
            pstmt = connection.prepareStatement(strSql);
            rs = pstmt.executeQuery();
            rsmd = rs.getMetaData();

            //-------------------------------------------------
            // 項目名称とデータ型の取得・格納
            //-------------------------------------------------
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                dte.getFieldTypeList().put(rsmd.getColumnLabel(i), Integer.toString(rsmd.getColumnType(i)));
            }

            pstmt.close();
            rs.close();

        } catch (SQLException se) {
            iResult = 0;
            this.doException("execute", se);

        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                pstmt = null;

                if (rs != null)
                    rs.close();
                rs = null;

            } catch (SQLException ignore) {
                this.doException("execute", ignore);
            }
        }
        return iResult;
    }

    /**
     * 未使用の execute メソッド（インターフェース実装のため定義）
     *
     * @param connection DBコネクション
     * @param strSql SQL文
     * @param dte テーブルエンティティ
     * @param list_keys キー配列（未使用）
     * @return 常に0を返す
     * @throws SystemException 構文上必要な例外定義
     */
    public int execute(Connection connection, String strSql, DbTableEntity dte, ArrayList list_keys) throws SystemException {
        return 0;
    }
}
