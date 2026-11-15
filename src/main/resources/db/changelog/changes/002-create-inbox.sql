--liquibase formatted sql
--changeset flowmanager:2
CREATE TABLE inbox(
    event_id UUID PRIMARY KEY,
    processed_at TIMESTAMPTZ,
    status VARCHAR NOT NULL DEFAULT 'RECEIVED'
);
--rollback DROP TABLE inbox;