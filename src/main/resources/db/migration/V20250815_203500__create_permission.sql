CREATE SEQUENCE IF NOT EXISTS permission_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE permission
(
    name        VARCHAR(255) NOT NULL DEFAULT nextval('permission_seq'),
    description VARCHAR(255),
    CONSTRAINT pk_permission PRIMARY KEY (name)
);

----------------------------------------------------------------------

CREATE TABLE role_permissions
(
    permission_name VARCHAR(255) NOT NULL,
    role_name       VARCHAR(255) NOT NULL,
    CONSTRAINT pk_role_permissions PRIMARY KEY (permission_name, role_name)
);

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_permission FOREIGN KEY (permission_name) REFERENCES permission (name);

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_role FOREIGN KEY (role_name) REFERENCES role (name);
