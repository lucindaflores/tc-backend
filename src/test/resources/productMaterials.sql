INSERT INTO product_materials(product_id, material_id)
VALUES ((SELECT id FROM products WHERE products.name = 'Test Product 1'),
        (SELECT id FROM materials WHERE materials.name = 'Test Material 1')),

       ((SELECT id FROM products WHERE products.name='Test Product 2'),
       (SELECT id FROM materials WHERE materials.name='Test Material 2'));

