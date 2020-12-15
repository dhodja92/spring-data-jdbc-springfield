CREATE extension if NOT EXISTS "uuid-ossp";

CREATE TABLE project (
    id    UUID NOT NULL DEFAULT uuid_generate_v4(),
    name  TEXT NOT NULL,
    color TEXT NOT NULL
);

CREATE TABLE task (
    id         UUID NOT NULL DEFAULT uuid_generate_v4(),
    name       TEXT NOT NULL,
    label      TEXT NOT NULL,
    finished   BOOLEAN DEFAULT FALSE,
    priority   TEXT,
    project_id UUID
);

ALTER TABLE project
    ADD CONSTRAINT pk_project PRIMARY KEY (id);

ALTER TABLE task
    ADD CONSTRAINT pk_task PRIMARY KEY (id);

ALTER TABLE ONLY task
    ADD CONSTRAINT fk_project FOREIGN KEY (project_id) REFERENCES project(id)
    ON DELETE CASCADE;

ALTER TABLE project
    ADD CONSTRAINT project_name_key UNIQUE (name);

CREATE INDEX idx_task_project_id ON task USING btree (project_id);
