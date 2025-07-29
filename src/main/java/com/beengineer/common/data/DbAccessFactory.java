package com.beengineer.common.data;

/********************************************************************************
 * データアクセスファクトリークラス
 *
 * 各DB操作種別（SELECT / UPDATE / INSERT / DELETE など）に応じて、
 * 適切な IDbAccessBean 実装クラスのインスタンスを生成する責任を持つクラスです。
 *
 * DbAccessController などから呼び出され、実行処理に必要なインスタンスを返却します。
 *
 * 履歴:
 *   V1.0  R00  2025/07/15  Bepro  新規作成
 *******************************************************************************/

import com.beengineer.common.exception.SystemException;
import com.beengineer.common.log.Logger;

public class DbAccessFactory {

    /**
     * 各種DbAccess実装クラスの共通インターフェース
     * ファクトリメソッド内で生成されるオブジェクトを保持
     */
    static private IDbAccessBean instance;

    /**
     * 指定された job_id に応じて、対応する DBアクセスインスタンスを返す
     *
     * <ul>
     *   <li>SELECT → DbAccessSelect</li>
     *   <li>SELECT_TYPE → DbAccessSelectType</li>
     *   <li>UPDATE → DbAccessUpdate</li>
     *   <li>INSERT → DbAccessInsert</li>
     *   <li>DELETE → DbAccessDelete</li>
     * </ul>
     *
     * @param object_id 主キー種別（例：ROWIDやOID）
     * @param job_id 実行対象のDB操作種別
     * @return IDbAccessBean 実行対象クラスのインスタンス
     * @throws SystemException job_idが無効な場合や生成に失敗した場合
     */
    static synchronized public IDbAccessBean getInstance(String object_id, String job_id) throws SystemException {
        Logger.out(Logger.DEBUG3, "DbAccessFactory#getInstance()", "sys", " STARTED ");

        if (job_id.equals("SELECT")) {
            instance = new DbAccessSelect(object_id);
            Logger.out(Logger.DEBUG3, "DbAccessFactory#getInstance()", "sys", "DbAccessSelect :");

        } else if (job_id.equals("SELECT_TYPE")) {
            instance = new DbAccessSelectType(object_id);
            Logger.out(Logger.DEBUG3, "DbAccessFactory#getInstance()", "sys", "DbAccessSelectType :");

        } else if (job_id.equals("UPDATE")) {
            instance = new DbAccessUpdate(object_id);
            Logger.out(Logger.DEBUG3, "DbAccessFactory#getInstance()", "sys", "DbAccessUpdate :");

        } else if (job_id.equals("INSERT")) {
            instance = new DbAccessInsert(object_id);
            Logger.out(Logger.DEBUG3, "DbAccessFactory#getInstance()", "sys", "DbAccessInsert :");

        } else if (job_id.equals("DELETE")) {
            instance = new DbAccessDelete(object_id);
            Logger.out(Logger.DEBUG3, "DbAccessFactory#getInstance()", "sys", "DbAccessDelete :");
        }

        Logger.out(Logger.DEBUG3, "DbAccessBean#getInstance()", "sys", " ENDED ");
        return instance;
    }
}
