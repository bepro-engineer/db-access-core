# PostgreSQL 接続設定まとめ（user_table用）

## データベース作成

```sql
CREATE DATABASE postgres
  WITH ENCODING 'UTF8'
       LC_COLLATE='C'
       LC_CTYPE='C'
       TEMPLATE template0;
```

## 接続ユーザー作成

```sql
CREATE USER postgres WITH PASSWORD 'P@ssW0rd';
GRANT ALL PRIVILEGES ON DATABASE postgres TO postgres
```

## テーブル作成

```sql
CREATE TABLE IF NOT EXISTS public.user_table
(
    user_id character varying(32) COLLATE pg_catalog."default" NOT NULL,
    user_name text COLLATE pg_catalog."default" NOT NULL,
    status character varying(16) COLLATE pg_catalog."default" NOT NULL,
    create_dt timestamp without time zone DEFAULT now(),
    update_dt timestamp without time zone DEFAULT now(),
    CONSTRAINT user_table_pkey PRIMARY KEY (user_id)
);

ALTER TABLE IF EXISTS public.user_table
    OWNER TO postgres;

COMMENT ON TABLE public.user_table IS 'DbAccessController simple test table';
COMMENT ON COLUMN public.user_table.user_id IS 'User ID';
COMMENT ON COLUMN public.user_table.user_name IS 'User Name';
COMMENT ON COLUMN public.user_table.status IS 'Status';
```

## 最小接続設定

（`/db-access-core/src/main/webapp/WEB-INF/system.xml`）

```xml
<system>
  <database>
    <username>postgres</username>
    <password>P@ssW0rd</password>
    <!-- 接続URLは問題の無い形式で記述 -->
    <url>jdbc:postgresql://<<データベースサーバアドレス>>:5432/postgres</url>
    <connect>20</connect>
    <driver>org.postgresql.Driver</driver>
    <object_id>CTID</object_id>
  </database>
  <!-- （省略） -->
</system>
```

## sample実行

Sample.javaをアプリケーション実行してください。