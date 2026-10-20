INSERT INTO insurance
(policy_number, provider, valide_till, create_at)
VALUES
    ('POL-2026-1001', 'HDFC ERGO', '2027-12-31', CURRENT_TIMESTAMP),
    ('POL-2026-1002', 'ICICI Lombard', '2028-06-30', CURRENT_TIMESTAMP),
    ('POL-2026-1003', 'Star Health', '2027-09-15', CURRENT_TIMESTAMP),
    ('POL-2026-1004', 'Niva Bupa', '2029-03-31', CURRENT_TIMESTAMP),
    ('POL-2026-1005', 'Care Health Insurance', '2028-11-30', CURRENT_TIMESTAMP);


INSERT INTO patient
(name, birth_date, gender, blood_group, gmail, insurance_id)
VALUES
    ('Rahul Sharma', '1998-05-12', 'Male', 'O_POSITIVE', 'rahul.sharma@example.com', 1),
    ('Priya Verma', '2000-08-21', 'Female', 'A_POSITIVE', 'priya.verma@example.com', 2),
    ('Amit Kumar', '1995-02-15', 'Male', 'B_POSITIVE', 'amit.kumar@example.com', 3),
    ('Sneha Gupta', '2001-11-30', 'Female', 'AB_POSITIVE', 'sneha.gupta@example.com', 4),
    ('Vikas Singh', '1997-07-09', 'Male', 'O_NEGATIVE', 'vikas.singh@example.com', 5);