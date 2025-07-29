package com.beengineer.common.data;

/********************************************************************************
 * データ変更用 DBアクセス実行クラス
 *
 * 本クラスは、指定されたSQL文とテーブルエンティティを用いて
 * UPDATE 処理を実行するための実装クラスです。
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

class DbAccessUpdate implements IDbAccessBean {

	// プリコンパイル済みSQLを保持するPreparedStatement
	PreparedStatement pstmt = null;

	// 主キー種別識別子（例：ROWIDやOID）
	String object_id;

	/**
	 * コンストラクタ
	 *
	 * @param object_id 主キー識別子
	 */
	protected DbAccessUpdate(String object_id) {
		this.object_id = object_id;
	}

	/**
	 * SystemExceptionの生成とスロー処理
	 *
	 * <ul>
	 *   <li>SQLExceptionをログに出力し、SystemExceptionとして再スロー</li>
	 * </ul>
	 *
	 * @param name 呼び出し元メソッド名
	 * @param e 発生したSQLException
	 * @return SystemException 例外オブジェクト
	 * @throws SystemException 常にスローされる
	 */
	public SystemException doException(String name, SQLException e) throws SystemException {
		this.abendLog(name, e);
		throw new SystemException("DbAccessUpdate#doException() " + name + "()", "unknown", "11004",
				"SQL実行エラー:" + e.getErrorCode());
	}

	/**
	 * エラーログ出力処理（メソッド名あり）
	 *
	 * @param name 呼び出し元メソッド名
	 * @param e 発生したSQLException
	 */
	public void abendLog(String name, SQLException e) {
		while (e != null) {
			Logger.out(Logger.ERROR, "DbAccessUpdate#abendLog() " + name + "()", "sys", "mes=" + e.getMessage());
			Logger.out(Logger.ERROR, "DbAccessUpdate#abendLog() " + name + "()", "sys", "stat=" + e.getSQLState());
			Logger.out(Logger.ERROR, "DbAccessUpdate#abendLog() " + name + "()", "sys",
					"code=" + String.valueOf(e.getErrorCode()));
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
			Logger.out(Logger.ERROR, "DbAccessUpdate#abendLog() ", "system", e.getMessage());
			Logger.out(Logger.ERROR, "DbAccessUpdate#abendLog() ", "system", e.getSQLState());
			Logger.out(Logger.ERROR, "DbAccessUpdate#abendLog() ", "system", String.valueOf(e.getErrorCode()));
			e = e.getNextException();
		}
	}

	/**
	 * 未使用の execute メソッド（インターフェース実装要件）
	 *
	 * @param connection DBコネクション
	 * @param strSql SQL文
	 * @param dte テーブルエンティティ
	 * @return 常に0を返す
	 * @throws SystemException 例外定義のため
	 */
	public int execute(Connection connection, String strSql, DbTableEntity dte)
			throws SystemException {
		return 0;
	}

	/**
	 * UPDATE処理の実行メソッド
	 *
	 * <ul>
	 *   <li>指定されたSQL文とDbTableEntityのデータを用いてレコードを更新します</li>
	 *   <li>型に応じたバインド処理を実施し、PreparedStatementを実行</li>
	 *   <li>null値や日付フィールドに対する特殊処理あり</li>
	 * </ul>
	 *
	 * @param connection DBコネクション
	 * @param strSql 実行するUPDATE SQL文（プレースホルダ付き）
	 * @param dte 更新対象のテーブルエンティティ
	 * @param list_keys バインド対象フィールドのキー一覧
	 * @return 更新件数合計
	 * @throws SystemException SQL実行時の例外
	 */
	public int execute(Connection connection, String strSql, DbTableEntity dte,
			ArrayList list_keys) throws SystemException {
		SystemException exp = null;
		pstmt = null;

		// PreparedStatement用のパラメータカウント
		int count = 0;
		int cnt = 0;
		int totalCnt = 0;
		int i = 0;
		Timestamp sysdate = new Timestamp(System.currentTimeMillis());
		DateFormat df = DateFormat.getDateInstance();

		try {
			//-------------------------------------------------
			// 更新処理本体
			//-------------------------------------------------
			pstmt = connection.prepareStatement(strSql);
			Logger.out(Logger.DEBUG3, "DbAccessUpdate#executeUpdate()", "sys",
					"変更フラグ配列の件数 : " + dte.getArrayEditFlg().length);

			for (i = 0; i < dte.getArrayEditFlg().length; i++) {
				Logger.out(Logger.DEBUG3, "DbAccessUpdate#executeUpdate()", "sys",
						"更新フラグの値 : " + i + " : " + dte.getUpFlg(i));
				if (dte.getUpFlg(i)) {
					Logger.out(Logger.DEBUG3, "DbAccessUpdate#executeUpdate()", "sys",
							"項目配列の件数 : " + dte.getFieldKeysCount());

					for (int j = 0; j < list_keys.size(); j++) {
						if (!(list_keys.get(j).toString().toUpperCase()).equals(object_id)) {
							count++;

							String strTmp = "";
							try {
								strTmp = ((String) dte.getField(i).get(list_keys.get(j)));
							} catch (Exception e) {
								strTmp = null;
							}

							try {
								if (strTmp == null || strTmp.length() == 0) {
									if (((list_keys.get(j).toString().toUpperCase()).equals("UPDATE_DT"))
											|| (list_keys.get(j).toString().toUpperCase()).equals("CREATE_DT")) {
										Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys",
												count + " 番目データは(NULLブロック:日付) ：" + list_keys.get(j) + " = " + sysdate);
										pstmt.setTimestamp(count, sysdate);
									} else {
										Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys",
												count + " 番目データは(NULLブロック:OTHER) ：" + list_keys.get(j) + " = null");
										pstmt.setNull(count,
												Integer.parseInt(dte.getFieldType((String) list_keys.get(j))));
									}
								} else {
									switch (Integer.parseInt(dte.getFieldType((String) list_keys.get(j)))) {
									case Types.CHAR:
										Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys",
												count + " 番目データは(CHAR) ：" + list_keys.get(j) + " = "
														+ dte.getField(i).get((String) list_keys.get(j)));
										pstmt.setString(count,
												((String) dte.getField(i).get((String) list_keys.get(j))));
										break;
									case Types.VARCHAR:
										Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys",
												count + " 番目データは(VARCHAR) ：" + list_keys.get(j) + " = "
														+ dte.getField(i).get((String) list_keys.get(j)));
										pstmt.setString(count,
												((String) dte.getField(i).get((String) list_keys.get(j))));
										break;
									case Types.LONGVARCHAR:
										Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys",
												count + " 番目データは(LONGVARCHAR) ：" + list_keys.get(j) + " = "
														+ dte.getField(i).get((String) list_keys.get(j)));
										pstmt.setString(count,
												((String) dte.getField(i).get((String) list_keys.get(j))));
										break;
									case Types.NUMERIC:
										Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys",
												count + " 番目データは(NUMERIC) ：" + list_keys.get(j) + " = "
														+ dte.getField(i).get((String) list_keys.get(j)));
										pstmt.setObject(count, new java.math.BigDecimal(
												(String) dte.getField(i).get((String) list_keys.get(j))));
										break;
									case Types.DECIMAL:
										Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys",
												count + " 番目データは(DECIMAL) ：" + list_keys.get(j) + " = "
														+ dte.getField(i).get((String) list_keys.get(j)));
										pstmt.setObject(count, new java.math.BigDecimal(
												(String) dte.getField(i).get((String) list_keys.get(j))));
										break;
									case Types.BIT:
										// pstmt.setBoolean (count, ...);
										break;
									case Types.TINYINT:
										// pstmt.setByte(count, ...);
										break;
									case Types.SMALLINT:
										// pstmt.setShort(count, ...);
										break;
									case Types.INTEGER:
										Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys",
												count + " 番目データは(INTEGER) ：" + list_keys.get(j) + " = "
														+ dte.getField(i).get((String) list_keys.get(j)));
										pstmt.setInt(count, Integer
												.parseInt((String) dte.getField(i).get((String) list_keys.get(j))));
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
											Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys",
													count + " 番目データは(DATE) ：" + list_keys.get(j) + " = " + sysdate);
											pstmt.setTimestamp(count, sysdate);
										} else if ("CREATE_DT".equals(((String) list_keys.get(j)).toUpperCase())) {
											Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys",
													count + " 番目データは(DATE) ：" + list_keys.get(j) + " = "
															+ dte.getField(i).get((String) list_keys.get(j)));
											pstmt.setTimestamp(count, Timestamp
													.valueOf((String) dte.getField(i).get((String) list_keys.get(j))));
										} else {
											Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys",
													count + " 番目データは(DATE) ：" + list_keys.get(j) + " = "
															+ dte.getField(i).get((String) list_keys.get(j)));
											pstmt.setDate(count, Date
													.valueOf((String) dte.getField(i).get((String) list_keys.get(j))));
										}
										break;
									case Types.TIME:
										Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys",
												count + " 番目データは(TIME) ：" + list_keys.get(j) + " = "
														+ dte.getField(i).get((String) list_keys.get(j)));
										pstmt.setTime(count,
												Time.valueOf((String) dte.getField(i).get((String) list_keys.get(j))));
										break;
									case Types.OTHER: // ctid(tid) 用
										// Logger.out(Logger.DEBUG3,"DbAccessUpdate#execute()", "sys",count + " 番目データは(OTHER→String) ：" + list_keys.get(j) + " = " + dte.getField(i).get((String) list_keys.get(j)));
										// pstmt.setString(count,String.valueOf(dte.getField(i).get((String) list_keys.get(j))));
										count--; // ← すでに ++ していたならここで戻す
										break;
									case Types.TIMESTAMP:
										if ("UPDATE_DT".equals(((String) list_keys.get(j)).toUpperCase())) {
											Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys", count
													+ " 番目データは(TIMESTAMP) ：" + list_keys.get(j) + " = " + sysdate);
											pstmt.setTimestamp(count, sysdate);
										} else if ("CREATE_DT".equals(((String) list_keys.get(j)).toUpperCase())) {
											Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys",
													count + " 番目データは(TIMESTAMP) ：" + list_keys.get(j) + " = "
															+ dte.getField(i).get((String) list_keys.get(j)));
											pstmt.setTimestamp(count, Timestamp
													.valueOf((String) dte.getField(i).get((String) list_keys.get(j))));
										} else {
											Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys",
													count + " 番目データは(TIMESTAMP) ：" + list_keys.get(j) + " = "
															+ dte.getField(i).get((String) list_keys.get(j)));
											pstmt.setTimestamp(count, Timestamp
													.valueOf((String) dte.getField(i).get((String) list_keys.get(j))));
										}
										break;
									}
								}
							} catch (IllegalArgumentException iae) {
								pstmt.setNull(count, Integer.parseInt(dte.getFieldType((String) list_keys.get(j))));
							}
						}
					}

					// -------------------------------------------------
					// WHERE句の構築（ROWID：ctid）
					// -------------------------------------------------
					for (int k = 0; k < list_keys.size(); k++) {
						if (list_keys.get(k).toString().equalsIgnoreCase(object_id)) {
							String ctidVal = String.valueOf(dte.getField(i).get((String) list_keys.get(k)));
							Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys",
									(count + 1) + " 番目データは(WHERE構文領域)：ctid = " + ctidVal);

							try {
								pstmt.setString(++count, ctidVal);
								Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys",
										"▶ ctid実行時のcount = " + count);
								Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys", "ctidバインド成功");
							} catch (Exception e) {
								Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys",
										"ctidバインド失敗：" + e.getMessage());
							}
						}
					}

					// -------------------------------------------------
					// WHERE句の構築（UPDATE_DT）
					// -------------------------------------------------
					for (int c = 0; c < list_keys.size(); c++) {
						if ("UPDATE_DT".equals(((String) list_keys.get(c)).toUpperCase())) {
							String update_dt = (String) dte.getField(i).get((String) list_keys.get(c));
							if (update_dt == null || update_dt.trim().isEmpty()) {
								Logger.out(Logger.ERROR, "DbAccessUpdate#execute()", "sys",
										"更新対象データのUPDATE_DTが未設定のため、更新できません。");
								throw new SystemException("DbAccessUpdate#execute()", "unknown", "11000",
										"更新対象データのUPDATE_DT未設定");
							} else {
								try {
									Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys",
											(count + 1) + " 番目データは(WHERE構文領域)：UPDATE_DT = " + update_dt);
									Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys",
											"▶ 現在の count = " + count);
									pstmt.setTimestamp(++count, Timestamp.valueOf(update_dt));
									Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys",
											"▶ UPDATE_DT バインド完了位置 = " + count);
									Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys", "UPDATE_DTバインド成功");
								} catch (Exception e) {
									Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys",
											"UPDATE_DTバインド失敗：" + e.getMessage());
								}
							}
						}
					}

					// ステートメントを実行
					cnt = pstmt.executeUpdate();

					totalCnt += cnt;
					Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys", "▶ totalCnt = " + totalCnt);

					if (cnt == 0) {
						Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys", "更新対象なし");
					}

					count = 0;
				}
			}

			Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys", totalCnt + "件 Updated ！：");

			// ステートメントを閉じる
			pstmt.close();

		} catch (SQLException se) {
			if (se.getErrorCode() == 1) {
				Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys", "一意制約違反");
			} else {
				Logger.out(Logger.DEBUG3, "DbAccessUpdate#execute()", "sys", "一意制約違反以外");
				this.doException("execute", se);
			}
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				pstmt = null;
			} catch (SQLException ignore) {
				this.doException("execute", ignore);
			}
		}

		return totalCnt;
	}
}
