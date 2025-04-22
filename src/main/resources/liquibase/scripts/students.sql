-- liquibase formatted sql

-- changeset dpanyu:3
CREATE INDEX student_name_index ON student (name);

-- changeset dpanyu:4
CREATE INDEX faculty_nameColor_index ON faculty (name, color);
