CREATE SEQUENCE IF NOT EXISTS permission_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS user_roles_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE permission
(
    name        VARCHAR(255) NOT NULL DEFAULT nextval('permission_seq'),
    description VARCHAR(255),
    CONSTRAINT pk_permission PRIMARY KEY (name)
);

CREATE TABLE permission_aud
(
    rev         INTEGER      NOT NULL,
    revtype     SMALLINT,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    CONSTRAINT pk_permission_aud PRIMARY KEY (rev, name)
);

ALTER TABLE permission_aud
    ADD CONSTRAINT FK_PERMISSION_AUD_ON_REV FOREIGN KEY (rev) REFERENCES revinfo (rev);

----------------------------------------------------------------------

CREATE TABLE role_permissions
(
    permission_name VARCHAR(255) NOT NULL,
    role_name       VARCHAR(255) NOT NULL,
    CONSTRAINT pk_role_permissions PRIMARY KEY (permission_name, role_name)
);

CREATE TABLE role_permissions_aud
(
    permission_name VARCHAR(255) NOT NULL,
    rev             INTEGER      NOT NULL,
    revtype         SMALLINT,
    role_name       VARCHAR(255) NOT NULL,
    CONSTRAINT pk_role_permissions_aud PRIMARY KEY (permission_name, rev, role_name)
);

ALTER TABLE role_permissions_aud
    ADD CONSTRAINT fk_role_permissions_aud_on_rev FOREIGN KEY (rev) REFERENCES revinfo (rev);

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_permission FOREIGN KEY (permission_name) REFERENCES permission (name);

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_role FOREIGN KEY (role_name) REFERENCES role (name);
