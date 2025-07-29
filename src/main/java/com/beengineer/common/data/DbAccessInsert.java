package com.beengineer.common.data;

/********************************************************************************
 * データ登録用 DBアクセス実行クラス
 *
 * このクラスは、指定されたSQL文とテーブルエンティティを用いて
 * INSERT 処理を実行するための実装クラスです。
 *
 * IDbAccessBean インターフェースを実装し、DbAccessFactory から呼び出されます。
 *
 * 履歴:
 *   V1.0  R00  2025/07/15  Bepro  新規開発
 *******************************************************************************/

// JDBC APIをインポート
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.util.ArrayList;

import com.beengineer.common.exception.SystemException;
import com.beengineer.common.log.Logger;

class DbAccessInsert implements IDbAccessBean {

    // プリコンパイル済みSQLを保持するPreparedStatement
    PreparedStatement pstmt = null;

    // 主キー種別（例：ROWIDやOID）
    String object_id;

    /**
     * コンストラクタ
     *
     * @param object_id 主キーの識別子（ROWIDやOID）
     */
    protected DbAccessInsert(String object_id) {
        this.object_id = object_id;
    }

    /**
     * INSERT処理の実行メソッド
     *
     * <ul>
     *   <li>指定されたSQL文とDbTableEntityのデータを使ってレコードをINSERT</li>
     *   <li>型に応じたバインド処理を行い、PreparedStatementを実行</li>
     *   <li>nullや日付フィールドへの特殊対応あり</li>
     * </ul>
     *
     * @param connection DBコネクション
     * @param strSql 実行するINSERT SQL文（?付き）
     * @param dte 挿入対象のテーブルエンティティ
     * @param list_keys バインド対象の項目キー一覧
     * @return 挿入されたレコード件数
     * @throws SystemException SQL実行時の例外
     */
    public int execute(Connection connection, String strSql, DbTableEntity dte, ArrayList list_keys) throws SystemException {
        SystemException exp = null;
        pstmt = null;
        Timestamp sysdate = new Timestamp(System.currentTimeMillis());
        // PreparedStatement用のバインド位置カウント
        int count = 0;
        int totalCnt = 0;
        int i = 0;
        DateFormat df = DateFormat.getDateInstance();

        try {
            //-------------------------------------------------
            // 挿入処理の本体
            //-------------------------------------------------
            pstmt = connection.prepareStatement(strSql);

            // 登録フラグ配列のサイズ分ループ
            Logger.out(Logger.DEBUG3, "DbAccessInsert#execute()", "sys", "変更フラグ配列の件数: " + dte.getArrayEditFlg().length);
            for (i = 0; i < dte.getArrayEditFlg().length; i++) {

                // 登録対象行かをチェック
                Logger.out(Logger.DEBUG3, "DbAccessInsert#execute()", "sys", "登録フラグの値 : " + i + " : " + dte.getInsFlg(i));
                if (dte.getInsFlg(i)) {

                    // 項目数分ループしてプレースホルダをセット
                    Logger.out(Logger.DEBUG3, "DbAccessInsert#execute()", "sys", "項目配列の件数 : " + dte.getFieldKeysCount());
                    for (int j = 0; j < list_keys.size(); j++) {
                        count++;

                        //-------------------------------------------------
                        // フィールド値を取得（null対策あり）
                        //-------------------------------------------------
                        String strTmp = "";
                        try {
                            strTmp = ((String) dte.getField(i).get(list_keys.get(j)));
                        } catch (Exception e) {
                            Logger.out(Logger.DEBUG3, "DbAccessInsert#execute()", "sys", "1例外発生!! : " + e + " : " + e.getMessage());
                            strTmp = null;
                        }

                        if (strTmp == null || strTmp.length() == 0) {
                            // null時：CREATE_DT / UPDATE_DT はシステム日付を挿入
                            if (((list_keys.get(j).toString().toUpperCase()).equals("UPDATE_DT")) ||
                                (list_keys.get(j).toString().toUpperCase()).equals("CREATE_DT")) {
                                Logger.out(Logger.DEBUG3, "DbAccessInsert#execute()", "sys", count + " 番目データは(NULLブロック:日付) ：" + list_keys.get(j) + " = " + sysdate);
                                pstmt.setTimestamp(count, sysdate);
                            } else {
                                Logger.out(Logger.DEBUG3, "DbAccessInsert#execute()", "sys", count + " 番目データは(NULLブロック:日付以外) ：" + list_keys.get(j) + " = " + dte.getFieldType((String) list_keys.get(j)));
                                pstmt.setNull(count, Integer.parseInt(dte.getFieldType(list_keys.get(j).toString())));
                            }
                        } else {
                            //-------------------------------------------------
                            // データ型別の判定とバインド処理
                            //-------------------------------------------------
                            switch (Integer.parseInt(dte.getFieldType((String) list_keys.get(j)))) {
                            case Types.CHAR:
                                Logger.out(Logger.DEBUG3, "DbAccessInsert#execute()", "sys", count + " 番目データは(CHAR)： " + list_keys.get(j) + " = " + dte.getField(i).get((String) list_keys.get(j)));
                                pstmt.setString(count, ((String) dte.getField(i).get((String) list_keys.get(j))));
                                break;
                            case Types.VARCHAR:
                                Logger.out(Logger.DEBUG3, "DbAccessInsert#execute()", "sys", count + " 番目データは(VARCHAR)： " + list_keys.get(j) + " = " + dte.getField(i).get((String) list_keys.get(j)));
                                pstmt.setString(count, ((String) dte.getField(i).get((String) list_keys.get(j))));
                                break;
                            case Types.LONGVARCHAR:
                                Logger.out(Logger.DEBUG3, "DbAccessInsert#execute()", "sys", count + " 番目データは(LONGVARCHAR)： " + list_keys.get(j) + " = " + dte.getField(i).get((String) list_keys.get(j)));
                                pstmt.setString(count, ((String) dte.getField(i).get((String) list_keys.get(j))));
                                break;
                            case Types.NUMERIC:
                                Logger.out(Logger.DEBUG3, "DbAccessInsert#execute()", "sys", count + " 番目データは(NUMERIC)： " + list_keys.get(j) + " = " + dte.getField(i).get((String) list_keys.get(j)));
                                pstmt.setObject(count, new java.math.BigDecimal((String) dte.getField(i).get((String) list_keys.get(j))));
                                break;
                            case Types.DECIMAL:
                                Logger.out(Logger.DEBUG3, "DbAccessInsert#execute()", "sys", count + " 番目データは(DECIMAL)： " + list_keys.get(j) + " = " + dte.getField(i).get((String) list_keys.get(j)));
                                pstmt.setObject(count, new java.math.BigDecimal((String) dte.getField(i).get((String) list_keys.get(j))));
                                break;
                            // 以下の型は未使用または対応保留（コメント済）
                            case Types.BIT:
                                // pstmt.setBoolean(count, ...);
                                break;
                            case Types.TINYINT:
                                // pstmt.setByte(count, ...);
                                break;
                            case Types.SMALLINT:
                                // pstmt.setShort(count, ...);
                                break;
                            case Types.INTEGER:
                                Logger.out(Logger.DEBUG3, "DbAccessInsert#execute()", "sys", count + " 番目データは(INTEGER)： " + list_keys.get(j) + " = " + dte.getField(i).get((String) list_keys.get(j)));
                                pstmt.setInt(count, Integer.parseInt((String) dte.getField(i).get((String) list_keys.get(j))));
                                break;
                            case Types.BIGINT:
                                // pstmt.setLong(count, ...);
                                break;
                            case Types.REAL:
                                // pstmt.setObject(count, ...);
                                break;
                            case Types.FLOAT:
                                // pstmt.setFloat(count, ...);
                                break;
                            case Types.DOUBLE:
                                // pstmt.setDouble(count, ...);
                                break;
                            case Types.BINARY:
                                // pstmt.setBytes(count, ...);
                                break;
                            case Types.VARBINARY:
                                // pstmt.setBytes(count, ...);
                                break;
                            case Types.LONGVARBINARY:
                                // pstmt.setObject(count, ...);
                                break;
                            case Types.DATE:
                                if ("UPDATE_DT".equals(((String) list_keys.get(j)).toUpperCase())) {
                                    Logger.out(Logger.DEBUG3, "DbAccessInsert#execute()", "sys", count + " 番目データは(DATE:UPDATE_DT)： " + list_keys.get(j) + " = " + sysdate);
                                    pstmt.setTimestamp(count, sysdate);
                                } else if ("CREATE_DT".equals(((String) list_keys.get(j)).toUpperCase())) {
                                    Logger.out(Logger.DEBUG3, "DbAccessInsert#execute()", "sys", count + " 番目データは(DATE:CREATE_DT)： " + list_keys.get(j) + " = " + dte.getField(i).get((String) list_keys.get(j)));
                                    pstmt.setTimestamp(count, Timestamp.valueOf((String) dte.getField(i).get((String) list_keys.get(j))));
                                } else {
                                    Logger.out(Logger.DEBUG3, "DbAccessInsert#execute()", "sys", count + " 番目データは(DATE)： " + list_keys.get(j) + " = " + dte.getField(i).get((String) list_keys.get(j)));
                                    pstmt.setDate(count, Date.valueOf((String) dte.getField(i).get((String) list_keys.get(j))));
                                }
                                break;
                            case Types.TIME:
                                Logger.out(Logger.DEBUG3, "DbAccessInsert#execute()", "sys", count + " 番目データは(TIME)： " + list_keys.get(j) + " = " + dte.getField(i).get((String) list_keys.get(j)));
                                pstmt.setTime(count, Time.valueOf((String) dte.getField(i).get((String) list_keys.get(j))));
                                break;
                            case Types.TIMESTAMP:
                                if ("UPDATE_DT".equals(((String) list_keys.get(j)).toUpperCase())) {
                                    Logger.out(Logger.DEBUG3, "DbAccessInsert#execute()", "sys", count + " 番目データは(TIMESTAMP:UPDATE_DT)： " + list_keys.get(j) + " = " + sysdate);
                                    pstmt.setTimestamp(count, sysdate);
                                } else if ("CREATE_DT".equals(((String) list_keys.get(j)).toUpperCase())) {
                                    Logger.out(Logger.DEBUG3, "DbAccessInsert#execute()", "sys", count + " 番目データは(TIMESTAMP:CREATE_DT)： " + list_keys.get(j) + " = " + dte.getField(i).get((String) list_keys.get(j)));
                                    pstmt.setTimestamp(count, Timestamp.valueOf((String) dte.getField(i).get((String) list_keys.get(j))));
                                } else {
                                    Logger.out(Logger.DEBUG3, "DbAccessInsert#execute()", "sys", count + " 番目データは(TIMESTAMP)： " + list_keys.get(j) + " = " + dte.getField(i).get((String) list_keys.get(j)));
                                    pstmt.setTimestamp(count, Timestamp.valueOf((String) dte.getField(i).get((String) list_keys.get(j))));
                                }
                                break;
                            }

                        }

                        // リスト終端でカウントを初期化
                        if (count == list_keys.size()) {
                            count = 0;
                        }
                    }

                    // 1レコード分のINSERTを実行
                    totalCnt += pstmt.executeUpdate();
                    Logger.out(Logger.DEBUG3, "DbAccessInsert#execute()", "sys", totalCnt + "件　Inserted ！：");
                }
            }

            // ステートメントを閉じる
            pstmt.close();
        } catch (SQLException se) {
            // 一意制約違反の場合、登録フラグを全件 false に設定
            if (se.getErrorCode() == 1) {
                Logger.out(Logger.DEBUG3, "DbAccessInsert#execute()", "sys", "一意制約違反");
                for (int j = 0; j < dte.getArrayInsFlg().length; j++) {
                    dte.setInsFlg(false, j);
                }
            } else {
                this.doException("execute", se);
            }
        } finally {
            try {
                // ステートメントの明示的クローズ
                if (pstmt != null)
                    pstmt.close();
                pstmt = null;
            } catch (SQLException ignore) {
                this.doException("execute", ignore);
            }
        }

        return (totalCnt);
    }

    /**
     * SystemException生成メソッド
     *
     * <ul>
     *   <li>SQLException発生時にログ出力後、SystemExceptionとして再送出</li>
     * </ul>
     *
     * @param name 実行処理名
     * @param e 発生したSQLException
     * @return SystemException 変換されたシステム例外
     * @throws SystemException 発生した例外をラップして再送出
     */
    public SystemException doException(String name, SQLException e) throws SystemException {
        this.abendLog(name, e);
        throw new SystemException("DbAccessInsert#doException() " + name + "() ", "unknown ", "11004", "SQL実行エラー:" + e.getErrorCode());
    }

    /**
     * エラーログ出力（メソッド名付き）
     *
     * @param name 呼び出し元メソッド名
     * @param e 発生したSQLException
     */
    public void abendLog(String name, SQLException e) {
        while (e != null) {
            Logger.out(Logger.ERROR, "DbAccessInsert#abendLog() " + name + "()", "sys", "mes=" + e.getMessage());
            Logger.out(Logger.ERROR, "DbAccessInsert#abendLog() " + name + "()", "sys", "stat=" + e.getSQLState());
            Logger.out(Logger.ERROR, "DbAccessInsert#abendLog() " + name + "()", "sys", "code=" + String.valueOf(e.getErrorCode()));
            e = e.getNextException();
        }
    }

    /**
     * エラーログ出力（メソッド名なし簡易版）
     *
     * @param e 発生したSQLException
     */
    public void abendLog(SQLException e) {
        while (e != null) {
            Logger.out(Logger.ERROR, "DbAccessInsert#abendLog() ", "system", e.getMessage());
            Logger.out(Logger.ERROR, "DbAccessInsert#abendLog() ", "system", e.getSQLState());
            Logger.out(Logger.ERROR, "DbAccessInsert#abendLog() ", "system", String.valueOf(e.getErrorCode()));
            e = e.getNextException();
        }
    }

    /**
     * 未使用の execute メソッド（インタフェース要件）
     *
     * @param connection DBコネクション
     * @param strSql SQL文
     * @param dte テーブルエンティティ
     * @return 常に 0 を返却
     * @throws SystemException 定義上必要な throws 節
     */
    public int execute(Connection connection, String strSql, DbTableEntity dte) throws SystemException {
        return 0;
    }
}
