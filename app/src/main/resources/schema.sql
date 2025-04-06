CREATE TABLE IF NOT EXISTS public.document
(
    uuid  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name  VARCHAR,
    state VARCHAR NOT NULL,
    owner UUID,
    file  UUID,

    create_dt TIMESTAMP
);

CREATE TABLE IF NOT EXISTS public.chunk
(
    uuid          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    document      UUID,
    page          INT4,
    num           INT4,
    content       TEXT,
    embed_content VECTOR,

    create_dt TIMESTAMP
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
    uuid      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    chat  UUID,
    chunk  UUID,
    similarity float,

    create_dt TIMESTAMP
);


select
    *
from public.chunk c
where uuid in ('c9cc7b53-9045-4088-a0c6-528dd535e99a')