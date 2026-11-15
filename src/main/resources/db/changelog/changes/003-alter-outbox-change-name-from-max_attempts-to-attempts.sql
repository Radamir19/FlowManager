--liquibase formatted sql
--changeset flowmanager:3
ALTER TABLE outbox DROP COLUMN max_attempts;
ALTER TABLE outbox ADD COLUMN attempts SMALLINT NOT NULL DEFAULT 0;