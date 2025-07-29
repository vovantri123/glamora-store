CREATE SEQUENCE IF NOT EXISTS revinfo_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE revinfo
(
    rev      INTEGER NOT NULL DEFAULT nextval('revinfo_seq'),
    revtstmp BIGINT,
    CONSTRAINT pk_revinfo PRIMARY KEY (rev)
);

CREATE TABLE users
(
    user_id      BIGINT       NOT NULL DEFAULT nextval('users_seq'),
    created_at   TIMESTAMP WITHOUT TIME ZONE,
    create_by    VARCHAR(255),
    update_at    TIMESTAMP WITHOUT TIME ZONE,
    update_by    VARCHAR(255),
    is_deleted   BOOLEAN      NOT NULL,
    full_name    VARCHAR(100),
    gender       VARCHAR(10),
    dob          date,
    email        VARCHAR(255) NOT NULL,
    password     VARCHAR(100) NOT NULL,
    phone_number VARCHAR(15),
    address      TEXT,
    image        TEXT,
    CONSTRAINT pk_users PRIMARY KEY (user_id)
);

CREATE TABLE users_aud
(
    rev          INTEGER NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE,
    create_by    VARCHAR(255),
    update_at    TIMESTAMP WITHOUT TIME ZONE,
    update_by    VARCHAR(255),
    is_deleted   BOOLEAN,
    revtype      SMALLINT,
    user_id      BIGINT  NOT NULL,
    full_name    VARCHAR(255),
    gender       VARCHAR(10),
    dob          date,
    email        VARCHAR(255),
    password     VARCHAR(255),
    phone_number VARCHAR(15),
    address      TEXT,
    image        TEXT,
    CONSTRAINT pk_users_aud PRIMARY KEY (rev, user_id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users_aud
    ADD CONSTRAINT FK_USERS_AUD_ON_REV FOREIGN KEY (rev) REFERENCES revinfo (rev);
