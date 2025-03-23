CREATE TABLE IF NOT EXISTS public.document
(
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name  VARCHAR,
    state VARCHAR NOT NULL,
    owner UUID,
    file  UUID
);

CREATE TABLE IF NOT EXISTS public.chunk
(
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    document  UUID,
    page INT8,
    content TEXT,
    embed_content VECTOR
);
