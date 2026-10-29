INSERT INTO insurance
(policy_number, provider, valide_till, create_at)
VALUES
    ('POL-2026-1001', 'HDFC ERGO', '2027-12-31', CURRENT_TIMESTAMP),
    ('POL-2026-1002', 'ICICI Lombard', '2028-06-30', CURRENT_TIMESTAMP),
    ('POL-2026-1003', 'Star Health', '2027-09-15', CURRENT_TIMESTAMP),
    ('POL-2026-1004', 'Niva Bupa', '2029-03-31', CURRENT_TIMESTAMP),
    ('POL-2026-1005', 'Care Health Insurance', '2028-11-30', CURRENT_TIMESTAMP)
ON CONFLICT (policy_number) DO NOTHING;


INSERT INTO patient
(name, birth_date, gender, blood_group, gmail, insurance_id)
VALUES
    ('Rahul Sharma', '1998-05-12', 'Male', 'O_POSITIVE', 'rahul.sharma@example.com', 1),
    ('Priya Verma', '2000-08-21', 'Female', 'A_POSITIVE', 'priya.verma@example.com', 2),
    ('Amit Kumar', '1995-02-15', 'Male', 'B_POSITIVE', 'amit.kumar@example.com', 3),
    ('Sneha Gupta', '2001-11-30', 'Female', 'AB_POSITIVE', 'sneha.gupta@example.com', 4),
    ('Vikas Singh', '1997-07-09', 'Male', 'O_NEGATIVE', 'vikas.singh@example.com', 5);


INSERT INTO doctor (name, specilization, email)
VALUES
    ('Dr. Amit Sharma', 'Cardiology', 'amit.sharma@example.com'),
    ('Dr. Priya Verma', 'Neurology', 'priya.verma@example.com'),
    ('Dr. Rahul Singh', 'Orthopedics', 'rahul.singh@example.com'),
    ('Dr. Neha Gupta', 'Dermatology', 'neha.gupta@example.com'),
    ('Dr. Vikram Patel', 'Pediatrics', 'vikram.patel@example.com')
ON CONFLICT (email) DO NOTHING;


-- Seed departments
INSERT INTO department (name, head_doctor_id)
VALUES 
    ('Cardiology', 1),
    ('Neurology', 2),
    ('Orthopedics', 3),
    ('Dermatology', 4),
    ('Pediatrics', 5)
ON CONFLICT (name) DO NOTHING;


-- Associate doctors with departments
INSERT INTO my_dpt_doctors (doctor_id, dpt_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4),
    (5, 5)
ON CONFLICT (doctor_id, dpt_id) DO NOTHING;


-- Default users for authentication and role-based access control
-- Credentials:
-- 1. admin / admin123 (ROLE_ADMIN)
-- 2. doctor / doctor123 (ROLE_DOCTOR)
-- 3. patient / patient123 (ROLE_PATIENT)
INSERT INTO users (username, password, role)
VALUES 
    ('admin', '$2b$12$AjlpAMzfQrDl.HClrkWQkeFlCoUXzZul0PNpQnd9QVqDfQ13F/f5G', 'ADMIN'),
    ('doctor', '$2b$12$r/jOirGxBQQDEJOPyT78tuAPLqxut82Ec4ixLICvjyzz6TjAw3v4e', 'DOCTOR'),
    ('patient', '$2b$12$FmjhYp0I0wWOTZCe2Ft/F.edNaTvA/09tbXnQNMF.HNqp4HmFpiA6', 'PATIENT')
ON CONFLICT (username) DO NOTHING;
