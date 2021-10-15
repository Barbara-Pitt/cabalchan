CREATE TABLE if not exists flags(
    id BIGSERIAL PRIMARY KEY
    ,filetype text not null
    ,filename text not null
    ,flagname text not NULL
    ,unique(flagname)
    ,unique(filename)
);

CREATE TABLE if not exists categories (
    id BIGSERIAL PRIMARY KEY
    ,title text not null
    ,subtitle text not null
    ,unique(title)
);

CREATE TABLE if not exists filters(
    id BIGSERIAL PRIMARY KEY
    ,cssclass text not null
    ,filtername text not NULL
    ,unique(cssclass)
    ,unique(filtername)
);

CREATE TABLE if not exists entries(
    id BIGSERIAL PRIMARY KEY
    ,parent_id bigint references entries(id)
    ,flag_id bigint references flags(id)
    ,filter_id bigint references filters(id)
    ,category_id bigint references categories(id)
    ,ipaddr text not null
    ,cabaluuid text not null
    ,comment text
    ,create_dt timestamp without time zone not null
);

CREATE TABLE if not exists attachments(
    id BIGSERIAL PRIMARY KEY
    ,entry_id bigint references entries(id)
    ,filename text not null
    ,filetype text not null
    ,spoiler boolean not null
    ,filter_id bigint references filters(id)
    ,unique(filename)
);

CREATE TABLE if not exists notifications(
    id BIGSERIAL PRIMARY KEY
    ,entry_id bigint not null references entries(id)
    ,cabaluuid text not null
    ,seen boolean not null
    ,msgtype text not null
    ,create_dt timestamp without time zone not null
);

CREATE TABLE if not exists users (
    id BIGSERIAL PRIMARY KEY
    ,username text not NULL
    ,password_hashed text not null
    ,staff boolean not null
);

CREATE TABLE if not exists reports (
    id BIGSERIAL PRIMARY KEY
    ,entry_id bigint not null references entries(id)
    ,create_dt timestamp without time zone not null
);

CREATE TABLE if not exists bans (
    id BIGSERIAL PRIMARY KEY
    ,mod_id BIGINT not null references users(id)
    ,ipaddr text not null
    ,reason TEXT not null
    ,blurb TEXT
    ,create_dt timestamp without time zone not null
    ,exp_dt timestamp without time zone not null
);

CREATE TABLE if not exists appeals (
    id BIGSERIAL PRIMARY KEY
    ,ban_id BIGINT not null references bans(id)
    ,comment TEXT
    ,appeal_status boolean
    ,create_dt timestamp without time zone not null
);

CREATE TABLE if not exists news (
    id BIGSERIAL PRIMARY KEY
    ,mod_id BIGINT not null references users(id)
    ,blurb TEXT not null
    ,create_dt timestamp without time zone not null
);