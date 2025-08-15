-- Insert roles
INSERT INTO role (name, created_at, create_by, update_at, update_by, is_deleted, description)
VALUES ('ADMIN', NOW(), 'System', NOW(), 'System', false, 'Administrator role with full permissions'),
       ('USER', NOW(), 'System', NOW(), 'System', false, 'Standard user role with limited permissions');

-- Insert permissions
INSERT INTO permission (name, description)
VALUES ('USER_READ', 'Permission to view user information'),
       ('USER_CREATE', 'Permission to create new users'),
       ('USER_UPDATE', 'Permission to update existing users'),
       ('USER_DELETE', 'Permission to delete users'),
       ('VIEW_DASHBOARD', 'Permission to view dashboard');

-- Link permissions to ADMIN role
INSERT INTO role_permissions (permission_name, role_name)
VALUES ('USER_READ', 'ADMIN'),
       ('USER_CREATE', 'ADMIN'),
       ('USER_UPDATE', 'ADMIN'),
       ('USER_DELETE', 'ADMIN'),
       ('VIEW_DASHBOARD', 'ADMIN');

-- Link permissions to USER role
INSERT INTO role_permissions (permission_name, role_name)
VALUES ('USER_READ', 'USER'),
       ('VIEW_DASHBOARD', 'USER');

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
