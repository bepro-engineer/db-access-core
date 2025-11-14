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
CREATE TABLE IF NOT EXISTS public.user_mst
(
    user_id character varying(32) COLLATE pg_catalog."default" NOT NULL,
    user_name character varying(64) COLLATE pg_catalog."default" NOT NULL,
    e_mail character varying(256) COLLATE pg_catalog."default" NOT NULL,
    password character varying(256) COLLATE pg_catalog."default" NOT NULL,
    create_dt timestamp without time zone DEFAULT now(),
    create_user character varying(32) COLLATE pg_catalog."default",
    update_dt timestamp without time zone DEFAULT now(),
    update_user character varying(32) COLLATE pg_catalog."default",
    del_flg character(1) COLLATE pg_catalog."default" DEFAULT '0'::bpchar,
    remark text COLLATE pg_catalog."default",
    CONSTRAINT user_mst_pkey PRIMARY KEY (user_id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.user_mst
    OWNER to postgres;

COMMENT ON TABLE public.user_mst
    IS 'User master table';

COMMENT ON COLUMN public.user_mst.user_id
    IS 'User ID';

COMMENT ON COLUMN public.user_mst.user_name
    IS 'User Name';

COMMENT ON COLUMN public.user_mst.e_mail
    IS 'Email';

COMMENT ON COLUMN public.user_mst.password
    IS 'password';

COMMENT ON COLUMN public.user_mst.create_dt
    IS 'Create datetime';

COMMENT ON COLUMN public.user_mst.create_user
    IS 'Create user';

COMMENT ON COLUMN public.user_mst.update_dt
    IS 'Update datetime';

COMMENT ON COLUMN public.user_mst.update_user
    IS 'Update user';

COMMENT ON COLUMN public.user_mst.del_flg
    IS 'Delete flag';

COMMENT ON COLUMN public.user_mst.remark
    IS 'Remark';
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