--liquibase formatted sql
--changeset flowmanager:4
CREATE TABLE file(
    id UUID PRIMARY KEY,
    source_key VARCHAR NOT NULL,
    result_key VARCHAR,
    status VARCHAR NOT NULL DEFAULT 'NEW'
);