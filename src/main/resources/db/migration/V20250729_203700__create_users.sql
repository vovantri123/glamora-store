CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 1;

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

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);
