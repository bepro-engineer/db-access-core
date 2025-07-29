package com.beengineer.common;

import java.util.HashMap;
import java.util.Map;

/********************************************************************************
 * システム全体設定読み込みユーティリティクラス
 *
 * system.xml の設定を一括で読み込み、
 * 指定したセクション（例: "log", "database"）の設定だけを抽出して返します。
 *
 * 履歴:
 *   V1.0  2025/07/15  Bepro  新規開発
 *******************************************************************************/
public class SystemInfo {

    // 設定キャッシュ（最初の一度だけ読み込み、以降はキャッシュ利用）
    private static volatile Map<String, String> cachedSettings = null;

    /**
     * 指定セクションの設定情報を取得します。
     *
     * system.xmlにて "セクション_キー" 形式で管理されている設定から、
     * 指定されたセクションに属する設定のみ抽出し、
     * キーからセクション名を除いた短縮キーでMapに格納して返します。
     *
     * 例）key="log_level", section="log" の場合、"level" → "DEBUG3" など
     *
     * @param section system.xml内のセクション名（例："log", "database"）
     * @return Map<String, String> 指定セクションの設定キーと値のマップ
     */
    public static Map<String, String> getKeyValueHash(String section) {

        // 初回のみ設定を読み込み、キャッシュする（スレッドセーフなダブルチェックロック）
        if (cachedSettings == null) {
            synchronized (SystemInfo.class) {
                if (cachedSettings == null) {
                    cachedSettings = IniFileRead.readSettings();
                }
            }
        }

        // 指定セクションに該当する設定を抽出
        Map<String, String> result = new HashMap<>();
        for (String key : cachedSettings.keySet()) {
            if (key.startsWith(section + "_")) {
                String shortKey = key.substring(section.length() + 1);
                result.put(shortKey, cachedSettings.get(key));
            }
        }

        return result;
    }
}
