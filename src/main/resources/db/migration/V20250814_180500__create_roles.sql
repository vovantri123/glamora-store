CREATE SEQUENCE IF NOT EXISTS roles_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS user_roles_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE role
(
    role_id    BIGINT      NOT NULL DEFAULT nextval('roles_seq'),
    created_at TIMESTAMP WITHOUT TIME ZONE,
    create_by  VARCHAR(255),
    update_at  TIMESTAMP WITHOUT TIME ZONE,
    update_by  VARCHAR(255),
    is_deleted BOOLEAN     NOT NULL,
    name       VARCHAR(50) NOT NULL,
    CONSTRAINT pk_role PRIMARY KEY (role_id)
);

CREATE TABLE role_aud
(
    rev        INTEGER NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    create_by  VARCHAR(255),
    update_at  TIMESTAMP WITHOUT TIME ZONE,
    update_by  VARCHAR(255),
    is_deleted BOOLEAN,
    revtype    SMALLINT,
    role_id    BIGINT  NOT NULL,
    name       VARCHAR(50),
    CONSTRAINT pk_role_aud PRIMARY KEY (rev, role_id)
);

ALTER TABLE role
    ADD CONSTRAINT uc_role_name UNIQUE (name);

ALTER TABLE role_aud
    ADD CONSTRAINT FK_ROLE_AUD_ON_REV FOREIGN KEY (rev) REFERENCES revinfo (rev);

-- user_roles


CREATE TABLE user_roles
(
    user_id BIGINT NOT NULL DEFAULT nextval('user_roles_seq'),
    role_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (role_id) REFERENCES role(role_id),
    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role_id)
);

CREATE TABLE user_roles_aud
(
    rev        INTEGER NOT NULL,
    user_id    BIGINT  NOT NULL,
    role_id    BIGINT  NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    create_by  VARCHAR(255),
    update_at  TIMESTAMP WITHOUT TIME ZONE,
    update_by  VARCHAR(255),
    is_deleted BOOLEAN,
    revtype    SMALLINT,
    name       VARCHAR(50),
    CONSTRAINT pk_user_roles_aud PRIMARY KEY (rev, user_id, role_id)
);

ALTER TABLE user_roles_aud
    ADD CONSTRAINT FK_USER_ROLES_AUD_ON_REV FOREIGN KEY (rev) REFERENCES revinfo (rev);
