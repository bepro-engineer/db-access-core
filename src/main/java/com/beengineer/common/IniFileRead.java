package com.beengineer.common;

/********************************************************************************
 * 設定ファイル（system.xml）読み込みクラス
 *
 * XML形式の設定ファイルを読み込み、
 * セクション名＋項目名をキーとしたHashtable形式で設定情報を返します。
 *
 * 履歴:
 *   V1.0  2025/07/15  Bepro  新規開発
 *******************************************************************************/
import java.io.File;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IniFileRead {

	public static Hashtable<String, String> readSettings() {
		// 設定項目を格納するHashtableを生成
		Hashtable<String, String> settings = new Hashtable<>();

		try {
			// XMLファイルのパスを取得
			String xmlFilePath = getConfigFilePath();
			System.out.println("[IniFileRead] XML読み込みパス: " + xmlFilePath);
			// XMLファイルオブジェクトを生成
			File xmlFile = new File(xmlFilePath);

			// DOMパーサーを初期化
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// XMLファイルを読み込み、DOMオブジェクトとして構築
			Document doc = builder.parse(xmlFile);

			// XMLのルート要素（例：<system>）を取得
			Element root = doc.getDocumentElement();

			// ルート直下の子ノード一覧（log, databaseなどのセクション）を取得
			NodeList childNodes = root.getChildNodes();

			// 各セクションをループ処理
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node section = childNodes.item(i);

				// セクションが要素ノードである場合のみ処理（コメントや空白を無視）
				if (section.getNodeType() == Node.ELEMENT_NODE) {
					// セクションのタグ名（log, databaseなど）を取得
					Element sectionElement = (Element) section;
					String sectionName = sectionElement.getTagName();

					// セクション内の項目一覧を取得
					NodeList items = sectionElement.getChildNodes();

					// 各項目（level, filepath, urlなど）をループ処理
					for (int j = 0; j < items.getLength(); j++) {
						Node item = items.item(j);

						// 要素ノードでない場合（空白や改行など）はスキップ
						if (item.getNodeType() == Node.ELEMENT_NODE) {
							// キーは「セクション名_項目名」（例：log_level, database_url）
							String key = sectionName + "_" + item.getNodeName();
							String value = item.getTextContent();
							// 🔽 この行を追加（ここなら key/value の両方が使える）
							System.out.println("[IniFileRead] key=" + key + " / val=" + value);
							// 設定情報にキーと値を格納
							settings.put(key, value);
						}
					}
				}
			}

		} catch (Exception e) {
			// 解析・読み込み中にエラーが発生した場合は標準エラーに出力
			System.err.println("設定の読み込み中にエラーが発生しました: " + e.getMessage());
		}

		// 設定情報を返す
		return settings;
	}

	// 設定ファイルのパスを動的に取得するメソッド
	public static String getConfigFilePath() {
		// 設定ファイルのパスを取得する処理（仮に設定）
		return "/Users/<<ユーザー名>>/Work/Java/db-access-core/src/main/webapp/WEB-INF/system.xml";
	}
}
