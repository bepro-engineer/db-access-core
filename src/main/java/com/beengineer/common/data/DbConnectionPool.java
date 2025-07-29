package com.beengineer.common.data;

/********************************************************************************
 * コネクションプールクラス
 *
 * DBMSへの問い合わせの際に使用するコネクションを管理し、
 * 再利用を促進するためのコネクションプール実装クラスです。
 *
 * シングルトンパターンを採用しており、
 * アプリケーション全体で1つのインスタンスを共有します。
 *
 * 履歴:
 *   V1.0  2025/07/15  Bepro  新規開発
 *******************************************************************************/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

import com.beengineer.common.SystemInfo;
import com.beengineer.common.log.Logger;

public class DbConnectionPool {

    /**
     * シングルトンインスタンス
     */
    static private DbConnectionPool instance;

    // チェックアウト済みコネクション数
    private int checkedOut = 0;

    // 最大コネクション数
    private int maxConn = 0;

    // 使用可能なコネクションのリスト
    private Vector<Connection> freeConnections = new Vector<>();

    // 接続情報
    private String url = null;
    private String password = null;
    private String username = null;
    private String driver = null;

    /**
     * シングルトンインスタンス取得メソッド
     *
     * @return DbConnectionPool インスタンス
     */
    static synchronized public DbConnectionPool getInstance() {
        Logger.out(Logger.DEBUG3, "DbConnectionPool#getInstance", "app", "▼コネクションプールクラス Start");
        if (instance == null) {
            instance = new DbConnectionPool();
        }
        Logger.out(Logger.DEBUG3, "DbConnectionPool#getInstance", "app", "▼コネクションプールクラス End");
        return instance;
    }

    /**
     * コンストラクタ
     *
     * 環境設定から接続情報を取得し、コネクションプール初期化を行います。
     */
    private DbConnectionPool() {
        Logger.out(Logger.DEBUG3, "DbConnectionPool#DbConnectionPool", "app", "▼freeConnections=" + freeConnections.size());
        Map<String, String> sysinfo = SystemInfo.getKeyValueHash("database");
        System.out.println("[DbConnectionPool AFTER DB init] logLevel=" + Logger.getLogLevel());
        this.url = sysinfo.get("url");
        this.username = sysinfo.get("username");
        this.password = sysinfo.get("password");
        this.maxConn = Integer.parseInt(sysinfo.get("connect"));
        this.driver = sysinfo.get("driver");
    }

    /**
     * コネクションの解放メソッド
     *
     * @param con 解放するコネクション
     */
    public synchronized void release(Connection con) {
        freeConnections.addElement(con);
        checkedOut--;
        notifyAll();
        Logger.out(Logger.DEBUG3, "DbConnectionPool#release", "app", "▼checkedOut=" + checkedOut);
    }

    /**
     * コネクションの取得メソッド
     *
     * プール内に空きがあれば再利用し、なければ新規作成します。
     *
     * @return Connection 使用可能なコネクション
     */
    public synchronized Connection getConnection() {
        Connection con = null;
        if (freeConnections.size() > 0) {
            con = freeConnections.firstElement();
            freeConnections.removeElementAt(0);

            try {
                if (con.isClosed()) {
                    con = getConnection();
                }
            } catch (SQLException e) {
                con = getConnection();
            }
        } else if (maxConn == 0 || checkedOut < maxConn) {
            con = newConnection();
        }

        if (con != null) {
            checkedOut++;
        }
        Logger.out(Logger.DEBUG3, "DbConnectionPool#getConnection", "app", "▼checkedOut=" + checkedOut);
        return con;
    }

    /**
     * プール内のすべてのコネクションをクローズし解放します。
     */
    public synchronized void releaseAll() {
        Enumeration<Connection> allConnections = freeConnections.elements();
        while (allConnections.hasMoreElements()) {
            Connection con = allConnections.nextElement();
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Can't close connection pool");
            }
        }
        freeConnections.removeAllElements();
    }

    /**
     * 新規コネクション作成メソッド
     *
     * @return Connection 新規作成したコネクション（失敗時はnull）
     */
    private Connection newConnection() {
        Connection con = null;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Can't create a new connection for " + url);
            return null;
        } catch (SQLException e) {
            System.out.println("Can't create a new connection for " + url);
            return null;
        }
        return con;
    }

    /**
     * テーブルロック処理メソッド
     *
     * @param conn 対象のコネクション
     * @return 0:成功、1:失敗
     */
    public int lockTable(Connection conn) {
        int rc = 0;
        java.sql.Statement stmt = null;
        String sql = "lock table t_order in share row exclusive mode";
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
            rc = 1;
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (Exception ex) {
                // Ignore any errors here
            }
        }
        return rc;
    }

    /**
     * テーブルロック解除処理メソッド
     *
     * @param conn 対象のコネクション
     * @return 0:成功、1:失敗
     */
    public int deLockTable(Connection conn) {
        int rc = 0;
        java.sql.Statement stmt = null;
        String sql = "end";
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
            rc = 1;
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (Exception ex) {
                // Ignore any errors here
            }
        }
        return rc;
    }
}
