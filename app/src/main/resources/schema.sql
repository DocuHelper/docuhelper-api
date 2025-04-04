CREATE TABLE IF NOT EXISTS public.document
(
    uuid  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name  VARCHAR,
    state VARCHAR NOT NULL,
    owner UUID,
    file  UUID
);

CREATE TABLE IF NOT EXISTS public.chunk
(
    uuid          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    document      UUID,
    page          INT8,
    num           INT8,
    content       TEXT,
    embed_content VECTOR
);

CREATE TABLE IF NOT EXISTS public.chat
(
    uuid      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    document  UUID,
    user_uuid UUID,
    user_ask  TEXT,
    state     varchar,
    result    TEXT null
);

CREATE TABLE IF NOT EXISTS public.chat_answer_ref
(
    uuid      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    chat  UUID,
    chunk  UUID,
    similarity float
);

