package com.beengineer.common.log;
/********************************************************************************
 * ログ出力制御クラス
 *
 * <PRE>
 *   このクラスは、ログファイルへの書き込みを制御するためのクラスです。
 *   デフォルトの文字エンコーディングを使用して、ログを書き込みます。
 *
 * 履歴：
 *  V1.0  R01  2025/07/15  Bepro  新規開発
 * </PRE>
 *******************************************************************************/
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.beengineer.common.SystemInfo;

public class LogWriter {

    private static BufferedWriter logWriter = null;  // BufferedWriter のみを使用

    private static String diffTime = new String();  // 最後に使用した日付を保持
    private static SimpleDateFormat LOGTIME_FORMAT = null;  // 日付フォーマットを管理
    private static final String FILE_SEP = System.getProperty("file.separator");  // OSによるファイルセパレータの取得
    private static String logFilePath = new String();  // ログファイルのパス
    private static String logFileName = new String();  // ログファイルの名前
    private static boolean logStat = true;  // ログの状態管理（ログが有効かどうか）

    private static LogWriter instance;  // LogWriterのシングルトンインスタンス

    /**
     * LogWriter クラスのインスタンスを取得します。
     * シングルトンパターンでインスタンスを生成します。
     *
     * @return LogWriterのインスタンス
     */
    private LogWriter() {
        logWriterInit();  // ログライターの初期化
    }

    public static LogWriter getInstance() {
        if (instance == null) {
            try {
                instance = new LogWriter();
            } catch (Exception e) {
                System.err.println("LogWriter初期化失敗: " + e.getMessage());
                instance = null;
            }
        }
        return instance;
    }

    /**
     * ログファイルの初期化を行います。
     * 設定ファイルからログファイルのパスやファイル名を取得し、ログ書き込みの準備を行います。
     */
    private void logWriterInit() {
        try {
            // 設定ファイルからログファイルパスを取得
            Map<String, String> sysinfo = SystemInfo.getKeyValueHash("log");
            logFilePath = (String) sysinfo.get("filepath");  // ログファイルの保存先パス
            String logFileBaseName = (String) sysinfo.get("logfile"); // system.xmlから取得したファイル名（例: "application.log"）

            // 日付フォーマットの設定
            LOGTIME_FORMAT = new SimpleDateFormat("yyyyMMdd");

            // 現在の日付を取得し、ログファイル名を生成
            String nowDate = LOGTIME_FORMAT.format(new Date());
            logFileName = logFileBaseName.replace(".log", "_" + nowDate + ".log");

            // ログファイルのパスを組み立て
            String logFilePathName = logFilePath + FILE_SEP + logFileName;

            // ログディレクトリが存在しない場合は作成
            File logDir = new File(logFilePath);
            if (!logDir.exists()) {
                logDir.mkdirs();  // ディレクトリ作成
            }

            // ログファイルのパスがディレクトリでないことを確認
            File logFile = new File(logFilePathName);
            if (logFile.exists() && logFile.isDirectory()) {
                throw new IOException("指定されたパスはディレクトリです: " + logFilePathName);
            }

            // ログファイルの書き込み準備
            logWriter = new BufferedWriter(new FileWriter(logFilePathName, true)); // 追記モードでファイルを開く
        } catch (IOException e) {
            e.printStackTrace();  // エラー発生時にはスタックトレースを出力
        }
    }

    /**
     * ログメッセージをファイルに書き込みます。
     *
     * @param message 書き込むログメッセージ
     */
    public void write(String message) {
        try {
            if (logWriter != null) {
                logWriter.write(message);  // ログメッセージを書き込む
                logWriter.newLine();  // 改行を追加
                logWriter.flush();  // メッセージを書き込んだ後、フラッシュして保存
            }
        } catch (IOException e) {
            e.printStackTrace();  // エラー発生時にはスタックトレースを出力
        }
    }

    /**
     * 同期化されたログ書き込み処理を行います。
     * ログファイルが日付変更されているかを確認し、必要に応じて新しいファイルに切り替えます。
     *
     * @param logs 書き込むログメッセージ
     */
    public synchronized void doWrite(String logs) {
        checkTime();  // ログファイルの切り替えを確認

        try {
            if (logWriter != null) {
                logWriter.write(logs);  // ログメッセージを書き込む
                logWriter.newLine();  // 改行を追加
                logWriter.flush();  // 即座にフラッシュして保存
            }
        } catch (IOException e) {
            e.printStackTrace();  // エラー発生時にはスタックトレースを出力
        }
    }

    /**
     * ログファイルの切り替えを確認し、日付が異なっていれば新しいログファイルを開きます。
     */
    private synchronized void checkTime() {
        String nowTime = LOGTIME_FORMAT.format(new Date());  // 現在の日付を取得

        // 前回と日付が異なる場合、新しいログファイルを生成
        if (!diffTime.equals(nowTime)) {
            String logFileName = nowTime + ".log";  // 新しいログファイル名
            String logFilePathName = logFilePath + FILE_SEP + logFileName;

            try {
                logWriter = new BufferedWriter(new FileWriter(logFilePathName, true));  // 新しいファイルを開く
            } catch (IOException e) {
                e.printStackTrace();  // エラー発生時にはスタックトレースを出力
            }
            diffTime = nowTime;  // 日付情報を更新
        }
    }

    /**
     * ログバッファをクリアし、ファイルを閉じます。
     */
    public void clear() {
        try {
            if (logWriter != null) {
                logWriter.flush();  // バッファ内の内容をフラッシュ
                logWriter.close();  // ファイルを閉じる
            }
        } catch (IOException e) {
            e.printStackTrace();  // エラー発生時にはスタックトレースを出力
        }
    }

    /**
     * バッファ内の内容を即座にフラッシュしてファイルに保存します。
     */
    public void flush() {
        try {
            if (logWriter != null) {
                logWriter.flush();  // バッファをフラッシュ
            }
        } catch (IOException e) {
            e.printStackTrace();  // エラー発生時にはスタックトレースを出力
        }
    }
}
