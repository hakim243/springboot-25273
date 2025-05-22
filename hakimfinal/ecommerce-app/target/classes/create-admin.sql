-- Create an admin user if not exists
-- Admin credentials: admin@gmail.com / admin

-- First ensure the ADMIN role exists
INSERT INTO roles (name) 
SELECT 'ADMIN' 
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN');

-- Insert admin user if it doesn't exist
INSERT INTO users (first_name, last_name, email, password, address, phone_number)
SELECT 'Admin', 'User', 'admin@gmail.com', '$2a$10$aUq5JQsKE1VrXOceRhKUyO.5cbPAoaYsY0/dC/6X/i1wWEGzjVKRO', 'Admin Office', '123-456-7890'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@gmail.com');

-- Assign admin role to admin user
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.email = 'admin@gmail.com' AND r.name = 'ADMIN'
AND NOT EXISTS (
    SELECT 1 FROM user_roles ur 
    JOIN users u2 ON ur.user_id = u2.id 
    JOIN roles r2 ON ur.role_id = r2.id 
    WHERE u2.email = 'admin@gmail.com' AND r2.name = 'ADMIN'
); 