CREATE SEQUENCE IF NOT EXISTS role_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS user_roles_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE role
(
    name        VARCHAR(255) NOT NULL DEFAULT nextval('role_seq'),
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    create_by   VARCHAR(255),
    update_at   TIMESTAMP WITHOUT TIME ZONE,
    update_by   VARCHAR(255),
    is_deleted  BOOLEAN      NOT NULL,
    description VARCHAR(255),
    CONSTRAINT pk_role PRIMARY KEY (name)
);

CREATE TABLE role_aud
(
    rev         INTEGER      NOT NULL,
    revtype     SMALLINT,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    CONSTRAINT pk_role_aud PRIMARY KEY (rev, name)
);


ALTER TABLE role_aud
    ADD CONSTRAINT FK_ROLE_AUD_ON_REV FOREIGN KEY (rev) REFERENCES revinfo (rev);

-------------------------------------------------------------------------------

CREATE TABLE user_roles
(
    role_name VARCHAR(255) NOT NULL,
    user_id   BIGINT       NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (role_name, user_id)
);

CREATE TABLE user_roles_aud
(
    rev       INTEGER      NOT NULL,
    revtype   SMALLINT,
    role_name VARCHAR(255) NOT NULL,
    user_id   BIGINT       NOT NULL,
    CONSTRAINT pk_user_roles_aud PRIMARY KEY (rev, role_name, user_id)
);

ALTER TABLE user_roles_aud
    ADD CONSTRAINT fk_user_roles_aud_on_rev FOREIGN KEY (rev) REFERENCES revinfo (rev);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (role_name) REFERENCES role (name);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (user_id);

