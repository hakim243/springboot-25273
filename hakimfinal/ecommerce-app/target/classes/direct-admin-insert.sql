-- Direct insert script for admin user

-- First ensure the roles table exists and has ADMIN role
CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Ensure the users table exists
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone_number VARCHAR(20)
);

-- Ensure the join table exists
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Insert the ADMIN role if it doesn't exist
INSERT INTO roles (name) 
SELECT 'ADMIN' 
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN');

-- Insert the USER role if it doesn't exist
INSERT INTO roles (name) 
SELECT 'USER' 
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'USER');

-- Insert admin user if it doesn't exist
-- Password is 'admin' encoded with BCrypt
INSERT INTO users (first_name, last_name, email, password, address, phone_number)
SELECT 'Admin', 'User', 'admin@gmail.com', '$2a$10$aUq5JQsKE1VrXOceRhKUyO.5cbPAoaYsY0/dC/6X/i1wWEGzjVKRO', 'Admin Office', '123-456-7890'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@gmail.com');

-- Assign ADMIN role to admin user
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.email = 'admin@gmail.com' AND r.name = 'ADMIN'
AND NOT EXISTS (
    SELECT 1 FROM user_roles ur 
    JOIN users u2 ON ur.user_id = u2.id 
    JOIN roles r2 ON ur.role_id = r2.id 
    WHERE u2.email = 'admin@gmail.com' AND r2.name = 'ADMIN'
); 