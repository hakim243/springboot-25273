-- Fix the users table by removing the 'role' column if it exists
DO $$
BEGIN
   ALTER TABLE users DROP COLUMN IF EXISTS role;
EXCEPTION
   WHEN undefined_column THEN NULL;
END $$; 