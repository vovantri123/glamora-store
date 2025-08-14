INSERT INTO role (created_at, create_by, update_at, update_by, is_deleted, name)
VALUES (NOW(), 'System', NOW(), 'System', false, 'ADMIN'),
       (NOW(), 'System', NOW(), 'System', false, 'USER');
