INSERT INTO order_details(QUANTITY, UNIT_PRICE, ORDER_ID, PRODUCT_ID, PRODUCT_NAME)
VALUES (2,
        (SELECT products.price FROM products WHERE code = 'TE-001'),
        (SELECT orders.id FROM orders WHERE user_id = (SELECT id FROM users WHERE email = 'email1@test.com')),
        (SELECT products.id FROM products WHERE code = 'TE-001'),
        (SELECT products.name FROM products WHERE code = 'TE-001'));