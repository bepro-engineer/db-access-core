# db-access-core
共通DBアクセスクラス群（Select/Insert/Update/Delete対応）。Java業務アプリケーション向けの抽象化されたSQL実行基盤。

# 共通DBアクセスクラス（db-access-core）

このプロジェクトは、Javaで開発される中小規模〜個人向け業務システムにおいて、  
**簡潔かつ柔軟にSQLを扱える共通データベースアクセス基盤**を提供することを目的としています。  
ORMに頼らず、SQL構文をそのまま活かしたい現場向けに設計されています。

---

## 特徴

- SELECT / INSERT / UPDATE / DELETE の共通実装を提供
- SQLは文字列定数で管理され、ソースコードと明確に分離
- BeanによるSQLパラメータの受け渡しに対応
- Loggerとの連携による詳細ログ出力
- system.xml による設定一元管理（DB接続・ログ出力）

---

## ディレクトリ構成

```
db-access-core/
├── data/         … DBアクセス本体クラス群（Select/Insert/Update/Delete）
├── log/          … ログ出力共通クラス（Logger / LogWriter）
├── exception/    … 共通例外クラス（SystemException）
```

※ 各ディレクトリには個別の `README.md` を配置し、詳細説明を行っています。

---

## 利用方法（概要）

1. system.xml に DB接続情報とログ設定を定義
2. DataBean クラスを作成し、IDbAccessBean を実装
3. DbAccessController に DataBean を渡して実行

```java
// SELECT例
DbAccessController ctrl = new DbAccessController();
ctrl.doSelect(myBean);  // SELECT処理実行
```

---

## 前提環境

- Java 8 以上（JDK）
- PostgreSQL または MySQL 等のJDBC対応DB
- RHEL系 Linux or Windows（開発環境はどちらでも可）

---

## 詳細解説（ブログ記事）

本クラスの設計背景や導入方法の解説記事を以下に公開しています：  
👉 [https://www.pmi-sfbac.org/java-dbaccess-01-basic](https://www.pmi-sfbac.org/java-dbaccess-01-basic)

---

## ライセンス / クレジット

- 開発者: Bepro  
- 使用・改変は自由。再配布・商用利用も制限なし（クレジットの記載推奨）

---

## 関連ディレクトリREADME

- [data/README.md](src/main/java/com/beengineer/common/data/README.md)
- [log/README.md](src/main/java/com/beengineer/common/log/README.md)
- [exception/README.md](src/main/java/com/beengineer/common/exception/README.md)

