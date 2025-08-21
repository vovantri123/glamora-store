-- Insert roles
INSERT INTO role (name, description)
VALUES ('ADMIN', 'Administrator role with full permissions'),
       ('USER', 'Standard user role with limited permissions');

-- Insert permissions
INSERT INTO permission (name, description)
VALUES ('USER_READ', 'Permission to view user information'),
       ('USER_CREATE', 'Permission to create new users'),
       ('USER_REGISTER', 'Permission to register new account'),
       ('USER_UPDATE', 'Permission to update existing users'),
       ('USER_DELETE', 'Permission to delete users');

-- Link permissions to ADMIN role
INSERT INTO role_permissions (permission_name, role_name)
VALUES ('USER_READ', 'ADMIN'),
       ('USER_CREATE', 'ADMIN'),
       ('USER_REGISTER', 'ADMIN'),
       ('USER_UPDATE', 'ADMIN'),
       ('USER_DELETE', 'ADMIN');

-- Link permissions to USER role
INSERT INTO role_permissions (permission_name, role_name)
VALUES ('USER_READ', 'USER'),
       ('USER_REGISTER', 'USER');

---------------------------------------------------------------------------------

-- Gán ADMIN cho user đầu tiên
INSERT INTO user_roles (role_name, user_id)
VALUES ('ADMIN', 1);

-- Gán USER cho các user tiếp theo
INSERT INTO user_roles (role_name, user_id)
VALUES ('USER', 2),
       ('USER', 3),
       ('USER', 4),
       ('USER', 5);
