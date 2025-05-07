CREATE TABLE IF NOT EXISTS public.document
(
    uuid      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name      VARCHAR,
    state     VARCHAR NOT NULL,
    owner     UUID,
    file      UUID,

    create_dt TIMESTAMP
);

alter table public.document
    ADD if not exists type varchar;

CREATE TABLE IF NOT EXISTS public.chunk
(
    uuid          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    document      UUID,
    page          INT4,
    num           INT4,
    content       TEXT,
    embed_content VECTOR,

    create_dt     TIMESTAMP
);

CREATE TABLE IF NOT EXISTS public.chat
(
    uuid      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    document  UUID,
    user_uuid UUID,
    user_ask  TEXT,
    state     varchar,
    result    TEXT null,

    create_dt TIMESTAMP
);

CREATE TABLE IF NOT EXISTS public.chat_answer_ref
(
    uuid       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    chat       UUID,
    chunk      UUID,
    similarity float,

    create_dt  TIMESTAMP
);

CREATE TABLE IF NOT EXISTS public.user_token
(
    uuid            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_uuid       UUID,
    available_token int4,
    limit_token     int4,

    create_dt       TIMESTAMP
);

CREATE TABLE IF NOT EXISTS public.user_token_history
(
    uuid      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_uuid UUID,
    type      varchar,
    diff      int4,

    create_dt TIMESTAMP
);