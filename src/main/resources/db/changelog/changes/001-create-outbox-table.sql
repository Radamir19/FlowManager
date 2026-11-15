--liquibase formatted sql
--changeset flowmanager:1
CREATE TABLE outbox (
    file_id           UUID PRIMARY KEY,
    aggregate    VARCHAR(100) NOT NULL,
    payload      VARCHAR NOT NULL,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    published_at TIMESTAMPTZ,
    status VARCHAR(100) NOT NULL,
    max_attempts SMALLINT NOT NULL DEFAULT 0
);
--rollback DROP TABLE outbox;