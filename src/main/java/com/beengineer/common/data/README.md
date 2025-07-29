# ディレクトリ概要：data

このディレクトリには、共通DBアクセスクラスの中核を構成するクラス群が配置されています。  
SQL構文を直接操作することを前提とし、ORMを使わずにシンプルかつ直感的なDB操作を実現するための構成です。  
各クラスは「単一責任」に基づき、処理の種類（SELECT、INSERT、UPDATE、DELETE）ごとに分割されています。

---

## 📌 各クラスの役割

| クラス名                   | 概要 |
|----------------------------|------|
| `DbAccessController.java`  | 共通処理の中核。`doSelect()` や `doExec()` によってSQL処理を振り分ける。|
| `DbAccessFactory.java`     | 各DB操作クラスの生成を行うFactoryクラス。処理種別に応じたインスタンスを返す。|
| `DbAccessSelect.java`      | SELECT処理専用クラス。通常の検索に対応。|
| `DbAccessSelectType.java`  | 特殊なSELECTタイプに対応する拡張クラス。|
| `DbAccessInsert.java`      | INSERT処理専用クラス。|
| `DbAccessUpdate.java`      | UPDATE処理専用クラス。|
| `DbAccessDelete.java`      | DELETE処理専用クラス。|
| `DbConnectionPool.java`    | DBコネクションの再利用を目的としたコネクションプール管理クラス。|
| `DbTableEntity.java`       | SELECT結果を1行分のデータとして保持するエンティティクラス。|
| `IDbAccessBean.java`       | DataBeanが実装すべきインターフェース。テーブル名、SQL、WHERE句などを外部から提供するために使用。|

---

## 🔧 使用上の注意

- 各処理（SELECT/INSERTなど）は、個別のクラスで定義された `execute()` メソッドにて実行されます。
- SQL文やWHERE句などの情報は、各種`DataBean`（`IDbAccessBean`を実装）から取得します。
- SQL文は基本的に `SqlConstants.java` に定数として定義し、コード内に直書きしない設計を推奨しています。
- このディレクトリのクラスを直接修正する場合は、全体設計への影響に十分注意してください。

---

## 🧭 関連情報

- 各処理で必要なログは `log` ディレクトリの `Logger.java` を使用してください。
- DB接続情報は `system.xml` ファイルにて定義され、`SystemInfo.getKeyValueHash("database")` で取得できます。

