package com.beengineer.common.log;

/********************************************************************************
 * ログ出力クラス
 *
 * このクラスはアプリケーション全体で使用可能な共通ログ出力機能を提供します。
 *
 * 使用例：
 *   Logger.init();  // 必ず最初に初期化を行う（LogWriterなどの準備）
 *   Logger.out(Logger.INFO, "InitInfo#getParam()", "user0001", "システム設定情報を取得します。");
 *
 * 引数の説明：
 *   - level     : 出力したいログレベル（INFO, DEBUG2, DEBUG3, WARN, ERROR）
 *   - className : クラス名（例："InitInfo#getParam()"）
 *   - method    : ユーザー名や識別情報など（任意の識別用文字列）
 *   - message   : 出力するログメッセージ
 *
 * 履歴：
 *   V1.0  R00  2025/07/15  Bepro  新規開発
 ********************************************************************************/

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.beengineer.common.SystemInfo;

public class Logger {

    // ログレベルの定数定義（出力制御に使用）
	public static final String INFO = "INFO";
	public static final String DEBUG1 = "DEBUG1";
	public static final String DEBUG2 = "DEBUG2";
	public static final String DEBUG3 = "DEBUG3";
	public static final String ERROR = "ERROR";
	public static final String WARN = "WARN";

    // ログ設定値（現在のログレベル／出力先／ファイル名）
    private static String logLevel = INFO;
    private static LogWriter logWriter;
    private static String logFilePath = "logfile.log";
    private static String logFileName = "logfile.log";

    private static SimpleDateFormat LOGTIME_FORMAT = null;

    /**
     * ロガーの初期化処理を行います。
     * システム設定ファイル（system.xml）からログレベルを取得し、
     * 出力先のログファイルパスを確定させます。
     */
    public static void init() {
        System.out.println("[INIT] Logger.init() called");

        Map<String, String> log_settings = SystemInfo.getKeyValueHash("log");
        if (log_settings != null && log_settings.get("level") != null) {
            logLevel = log_settings.get("level").toUpperCase();
        } else {
            logLevel = INFO;
        }

        System.out.println("DEBUG: logLevel=" + logLevel);

        logWriter = LogWriter.getInstance();
        System.out.println("[DEBUG] logWriter = " + logWriter); 
        checkLogFile();
    }

    public static String getLogLevel() {
        return logLevel;
    }

    /**
     * 指定された条件に従ってログを出力します。
     * 出力条件を満たす場合のみ、ログファイルへ書き込みます。
     *
     * @param level     ログレベル（例：Logger.INFO）
     * @param className 呼び出し元のクラス名
     * @param method    メソッド名または任意識別子
     * @param message   出力メッセージ
     */
    public static void out(String level, String className, String method, String message) {
        System.out.println("[TRACE] Logger.out() CALLED: level=" + level + " / class=" + className + " / method=" + method);
        System.out.println("[LOGGER OUT] level=" + level + " / logLevel=" + logLevel);

        // ログ出力制御
        if (!shouldLog(level)) {
            System.out.println("[SKIP] Logger.out(): level=" + level + " is below logLevel=" + logLevel);
            return;
        }

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        logWriter.write("[" + timeStamp + "] [" + level + "] " + className + "." + method + ": " + message + "\n");
        logWriter.flush();
        System.out.println("[WRITE] logWriter = " + (logWriter != null));
    }

    /**
     * 指定されたログレベルで出力を行うかどうかを判定します。
     *
     * @param level 出力しようとしているログレベル
     * @return 出力可否（true: 出力する／false: 出力しない）
     */
    private static boolean shouldLog(String level) {
        if (logLevel == null || level == null) return false;

        int current = getLogRank(level);
        int threshold = getLogRank(logLevel);

        System.out.println("[CHECK] shouldLog(): level=" + level + " (" + current + ") vs logLevel=" + logLevel + " (" + threshold + ")");
        return current <= threshold;
    }

    /**
     * ログレベルの重要度を数値として返します（低い値ほど重大）。
     * 例：ERROR=1, DEBUG3=5
     *
     * @param lv ログレベル
     * @return 数値化された優先順位（重大度）
     */
    private static int getLogRank(String lv) {
        if (lv == null) return -1;
        lv = lv.trim().toUpperCase();
        
    	switch (lv) {
        case DEBUG3: return 5;
        case DEBUG2: return 4;
        case DEBUG1: return 3;
        case INFO:   return 2;
        case WARN:   return 1;
        case ERROR:  return 0;
        default:     return -1;
    	}
    }

    /**
     * ログファイルの出力パスとファイル名を設定し、
     * ファイルの存在確認およびディレクトリの作成を行います。
     */
    private static void checkLogFile() {
        Map<String, String> sysinfo = SystemInfo.getKeyValueHash("log");
        logFilePath = (String) sysinfo.get("filepath");
        String logFileBaseName = (String) sysinfo.get("logfile");

        LOGTIME_FORMAT = new SimpleDateFormat("yyyyMMdd");
        String nowDate = LOGTIME_FORMAT.format(new Date());
        logFileName = logFileBaseName.replace(".log", "_" + nowDate + ".log");

        String logFilePathName = logFilePath + File.separator + logFileName;

        File logDir = new File(logFilePath);
        if (!logDir.exists()) {
            logDir.mkdirs();
        }

        File logFile = new File(logFilePath + "/" + logFileName);
        if (logFile.exists()) {
            System.out.println("ログファイルは正常に作成されました: " + logFile.getAbsolutePath());
        } else {
            System.out.println("ログファイルが作成されませんでした。");
        }
    }

    /**
     * 現在のログレベルを手動で上書き設定します。
     *
     * @param level 新しいログレベル（例："DEBUG3"）
     */
    public static void setLogLevel(String level) {
        logLevel = level;
    }

    /**
     * ログファイルの出力パスを手動で設定します。
     *
     * @param path ログ出力先パス
     */
    public static void setLogFilePath(String path) {
        logFilePath = path;
    }
}
