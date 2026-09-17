INSERT INTO products(code, name, description, price, stock, image_url, category_id, origin_id)
VALUES ('TE-001',
        'Test Product 1',
        'Test description 1',
        1.0,
        1,
        '/test/test1.png',
        (SELECT id FROM categories WHERE categories.name='Test Category 1'),
        (SELECT id FROM origins WHERE origins.name='Test Origin 1')),

       ('TE-002',
        'Test Product 2',
        'Test description 2',
        10.0, 5,
        '/test/test2.png',
        (SELECT id FROM categories WHERE categories.name='Test Category 2'),
         (SELECT id FROM origins WHERE origins.name='Test Origin 2'));