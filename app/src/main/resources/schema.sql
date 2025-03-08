CREATE TABLE IF NOT EXISTS public.document
(
    uuid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name  VARCHAR,
    state VARCHAR NOT NULL,
    owner UUID,
    file  UUID
);


