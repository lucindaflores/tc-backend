INSERT INTO orders(order_date, status, user_id)
VALUES (now(),
        'PLACED',
        (SELECT id FROM users WHERE email = 'email1@test.com')),

        (now(),
        'PLACED',
        (SELECT id FROM users WHERE email = 'email2@test.com'));


