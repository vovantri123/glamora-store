CREATE SEQUENCE IF NOT EXISTS role_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE role
(
    name        VARCHAR(255) NOT NULL DEFAULT nextval('role_seq'),
    description VARCHAR(255),
    CONSTRAINT pk_role PRIMARY KEY (name)
);

-------------------------------------------------------------------------------

CREATE TABLE user_roles
(
    role_name VARCHAR(255) NOT NULL,
    user_id   BIGINT       NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (role_name, user_id)
);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (role_name) REFERENCES role (name);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (user_id);

