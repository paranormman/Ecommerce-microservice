INSERT INTO orders (total_price, order_status) VALUES
(100.50, 'PENDING'),
(250.75, 'CONFIRMED'),
(499.99, 'DELIVERED'),
(149.30, 'CANCELLED'),
(79.99, 'PENDING'),
(325.00, 'CONFIRMED'),
(199.99, 'DELIVERED'),
(50.25, 'PENDING'),
(400.00, 'CANCELLED'),
(89.90, 'CONFIRMED'),
(120.49, 'DELIVERED');


--Inserting order items, ensuring each order has items
INSERT INTO order_item (order_id, product_id, quantity) VALUES
(1, 101, 2),
(1, 102, 1),
(2, 103, 4),
(3, 104, 3),
(4, 105, 2),
(5, 106, 5),
(6, 107, 1),
(7, 108, 2),
(8, 109, 3),
(9, 110, 4),
(10, 111, 2);