-- Create roles table
CREATE TABLE roles (
    name VARCHAR(50) PRIMARY KEY,
    description TEXT
);

-- Create permissions table
CREATE TABLE permissions (
    name VARCHAR(100) PRIMARY KEY,
    description TEXT
);

-- Create role_permissions junction table
CREATE TABLE role_permissions (
    role_name VARCHAR(50) NOT NULL,
    permission_name VARCHAR(100) NOT NULL,
    PRIMARY KEY (role_name, permission_name),
    FOREIGN KEY (role_name) REFERENCES roles(name),
    FOREIGN KEY (permission_name) REFERENCES permissions(name)
);

-- Create user_roles junction table
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role_name),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_name) REFERENCES roles(name)
);
