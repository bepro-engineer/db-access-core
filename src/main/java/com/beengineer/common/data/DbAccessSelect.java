package com.beengineer.common.data;

/********************************************************************************
 * データ検索用 DBアクセス実行クラス
 *
 * 本クラスは、指定されたSQLを実行し、検索結果を DbTableEntity に格納する
 * SELECT処理の実装クラスです。
 *
 * IDbAccessBean インターフェースを実装し、DbAccessFactory から呼び出されます。
 *
 * 履歴:
 *   V1.0  R00  2025/07/15  Bepro  新規開発
 *******************************************************************************/

// JDBC APIをインポート
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.beengineer.common.exception.SystemException;
import com.beengineer.common.log.Logger;

class DbAccessSelect implements IDbAccessBean {

    // プリコンパイル済みSQLを保持するPreparedStatement
    PreparedStatement pstmt = null;

    // 主キー識別子（例：ROWID、OID）
    String object_id;

    /**
     * コンストラクタ
     *
     * @param object_id 主キータイプ識別用ID
     */
    protected DbAccessSelect(String object_id) {
        this.object_id = object_id;
    }

    /**
     * SystemExceptionの生成・スロー処理
     *
     * <ul>
     *   <li>SQLExceptionをログに出力し、SystemExceptionとして再送出</li>
     * </ul>
     *
     * @param name 呼び出し元メソッド名
     * @param e 発生したSQLException
     * @return SystemException ラップされた例外
     * @throws SystemException 常にスローされる
     */
    public SystemException doException(String name, SQLException e)
            throws SystemException {
        this.abendLog(name, e);
        throw new SystemException("DbAccessSelect#doException() " + name + "()", "unknown", "11004", "SQL実行エラー:" + e.getErrorCode());
    }

    /**
     * エラーログ出力処理（メソッド名あり）
     *
     * @param name 呼び出し元名
     * @param e 発生したSQLException
     */
    public void abendLog(String name, SQLException e) {
        while (e != null) {
            Logger.out(Logger.ERROR, "DbAccessSelect#abendLog() " + name + "()", "sys", "mes=" + e.getMessage());
            Logger.out(Logger.ERROR, "DbAccessSelect#abendLog() " + name + "()", "sys", "stat=" + e.getSQLState());
            Logger.out(Logger.ERROR, "DbAccessSelect#abendLog() " + name + "()", "sys", "code=" + String.valueOf(e.getErrorCode()));
            e = e.getNextException();
        }
    }

    /**
     * エラーログ出力処理（メソッド名なし）
     *
     * @param e 発生したSQLException
     */
    public void abendLog(SQLException e) {
        while (e != null) {
            Logger.out(Logger.ERROR, "DbAccessSelect#abendLog()", "system", e.getMessage());
            Logger.out(Logger.ERROR, "DbAccessSelect#abendLog()", "system", e.getSQLState());
            Logger.out(Logger.ERROR, "DbAccessSelect#abendLog()", "system", String.valueOf(e.getErrorCode()));
            e = e.getNextException();
        }
    }
    
    /**
     * 検索SQLの実行メソッド
     *
     * <ul>
     *   <li>指定されたSQLを実行し、検索結果を DbTableEntity に格納します</li>
     *   <li>ResultSetMetaData からカラム情報（名称・型）を取得し、フィールド型リストに登録</li>
     *   <li>検索結果は HashMap に格納し、DbTableEntity のテーブルリストへ追加</li>
     * </ul>
     *
     * @param connection DBコネクション
     * @param strSql 実行するSELECT文
     * @param dte 結果格納用テーブルエンティティ
     * @return 検索結果件数
     * @throws SystemException SQL実行時の例外
     */
    public int execute(Connection connection, String strSql,
            DbTableEntity dte) throws SystemException {
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int iResult = 0;
        int count = 0;

        try {
            //-------------------------------------------------
            // 検索処理開始
            //-------------------------------------------------
            // PreparedStatementを準備
            pstmt = connection.prepareStatement(strSql);
            // SQL実行しResultSetを取得
            rs = pstmt.executeQuery();
            // ResultSetのメタデータ取得
            rsmd = rs.getMetaData();

            //-------------------------------------------------
            // カラム名称と型の取得と格納
            //-------------------------------------------------
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                dte.getFieldTypeList().put(rsmd.getColumnLabel(i), Integer.toString(rsmd.getColumnType(i)));
                Logger.out(Logger.DEBUG3, "DbAccessSelectType#execute()", "sys", "データ型: " + rsmd.getColumnLabel(i) + " = " + rsmd.getColumnType(i));
            }

            //-------------------------------------------------
            // 検索結果の行単位取得と格納
            //-------------------------------------------------
            for (count = 0; rs.next(); count++) {
                HashMap tmpMap = new HashMap();

                // 各カラムの値を取得しHashMapに格納
                for (int k = 1; k <= rsmd.getColumnCount(); k++) {
                    try {
                        String strTmp = rs.getString(k);
                        Logger.out(Logger.DEBUG3, "DbAccessSelect#execute()", "sys", count + "件目のデータ " + rsmd.getColumnLabel(k) + " = " + strTmp);

                        if (strTmp == null || strTmp.length() == 0) {
                            strTmp = "";
                        }
                        tmpMap.put(rsmd.getColumnLabel(k), strTmp);

                    } catch (NullPointerException npe) {
                        String strTmp = "";
                        Logger.out(Logger.DEBUG3, "DbAccessSelect#execute()", "sys", count + "件目のデータ " + rsmd.getColumnLabel(k) + " = (NULL)" + strTmp);
                        tmpMap.put(rsmd.getColumnLabel(k), strTmp);
                    }
                }
                // 行データをテーブルエンティティに追加
                dte.getTbl().add(tmpMap);
            }
            // 件数セット
            dte.setArray(count);

            // リソースクローズ
            pstmt.close();
            rs.close();

            iResult = count;

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
                iResult = 0;
                this.doException("execute", ignore);
            }
        }

        // 検索フラグセット
        dte.setSelectFlg(true);
        return iResult;
    }
    
    /**
     * 未使用の execute メソッド（インターフェース実装要件のため定義）
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
