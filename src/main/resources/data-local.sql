-- Create a demo member if it doesn't exist (by phone)
INSERT INTO member (name, phone)
SELECT 'Demo Member', '+34111111111'
WHERE NOT EXISTS (SELECT 1 FROM member WHERE phone = '+34111111111');

-- Create a demo bus pass for the demo member if it doesn't exist (by code)
INSERT INTO bus_pass (member_id, code, total_uses, used_count)
SELECT (SELECT id FROM member WHERE phone = '+34111111111'), 'DEMO-123-555', 5, 0
WHERE NOT EXISTS (SELECT 1 FROM bus_pass WHERE code = 'DEMO-123-555');
