-- =============================================================================
-- FOOD DELIVERY BACKEND — SAMPLE DATA
-- =============================================================================
-- File:    insert_sample_data.sql
-- Purpose: Populates all 9 tables with realistic Indian food delivery data.
-- Order:   Inserts follow the same topological order as table creation.
--          Level 0 → Level 1 → Level 2 → Level 3 → Level 4
-- Usage:   Run AFTER create_database.sql
--          mysql> source insert_sample_data.sql;
-- =============================================================================

USE food_delivery;

-- =============================================================================
-- LEVEL 0: ROOT ENTITIES
-- =============================================================================

-- -----------------------------------------------------------------------------
-- TABLE 1: users (11 users: 5 Customers, 3 Owners, 3 Delivery Partners)
-- -----------------------------------------------------------------------------
-- Passwords are BCrypt-hashed placeholders. In production, the Java application
-- would hash passwords before INSERT. We store hashes, NEVER plaintext.
-- -----------------------------------------------------------------------------

INSERT INTO users (name, email, phone, password, role) VALUES
    ('Aarav Sharma',    'aarav@mail.com',    '9876543210', 'customerpassword', 'CUSTOMER'),
    ('Priya Patel',     'priya@mail.com',    '9123456789', '$2a$10$kM3nB7vR1wX5qL9pT2sYueA4fC6hJ8dG0iE2rO4uS6wN8xZ0yP1bQ', 'CUSTOMER'),
    ('Vikram Singh',    'vikram@mail.com',   '9234567890', '$2a$10$qW5eR7tY1uI3oP5aS7dF9gH1jK3lZ5xC7vB9nM1pQ3rT5uW7yA9sE', 'CUSTOMER'),
    ('Meera Joshi',     'meera@mail.com',    '9345678901', '$2a$10$rT6yU8iO2pA4sD6fG8hJ0kL2zX4cV6bN8mQ0wE2rT4yU6iO8pA0sD', 'CUSTOMER'),
    ('Rohan Gupta',     'rohan@mail.com',    '9456789012', '$2a$10$aS3dF5gH7jK9lZ1xC3vB5nM7qW9eR1tY3uI5oP7aS9dF1gH3jK5lZ', 'CUSTOMER'),
    ('Anita Desai',     'anita@mail.com',    '9567890123', 'ownerpassword', 'OWNER'),
    ('Rajesh Khanna',   'rajesh@mail.com',   '9678901234', '$2a$10$cV5bN7mQ9wE1rT3yU5iO7pA9sD1fG3hJ5kL7zX9cV1bN3mQ5wE7rT', 'OWNER'),
    ('Sunita Verma',    'sunita@mail.com',   '9789012345', '$2a$10$dF6gH8jK0lZ2xC4vB6nM8qW0eR2tY4uI6oP8aS0dF2gH4jK6lZ8xC', 'OWNER'),
    ('Ravi Kumar',      'ravi@mail.com',     '9111222333', 'dboypassword', 'DELIVERY_PARTNER'),
    ('Sana Ali',        'sana@mail.com',     '9444555666', '$2a$10$fH8jK0lZ2xC4vB6nM8qW2eR4tY6uI8oP0aS2dF4gH6jK8lZ0xC2vB', 'DELIVERY_PARTNER'),
    ('Deepak Yadav',    'deepak@mail.com',   '9777888999', '$2a$10$gI9kL1zX3cV5bN7mQ9wE3rT5yU7iO9pA1sD3fG5hJ7kL9zX1cV3bN', 'DELIVERY_PARTNER');


-- -----------------------------------------------------------------------------
-- TABLE 2: coupons (5 promotional coupons with various rules)
-- -----------------------------------------------------------------------------
-- current_usage reflects how many times each coupon has been used in the
-- sample orders below. In production, a trigger or application logic
-- would increment this on each valid redemption.
-- -----------------------------------------------------------------------------

INSERT INTO coupons (code, discount_percentage, max_discount_amount, min_order_amount, valid_from, valid_until, max_usage, current_usage) VALUES
    ('WELCOME50', 50.00, 100.00,   0.00, '2026-01-01', '2026-12-31', 1000, 1),
    ('FLAT100',   20.00, 100.00, 500.00, '2026-01-01', '2026-06-30', 500,  1),
    ('FESTIVE30', 30.00, 150.00, 300.00, '2026-07-01', '2026-08-31', 2000, 1),
    ('NEWUSER',   40.00,  80.00,   0.00, '2026-01-01', '2026-12-31', 5000, 1),
    ('FREEDEL',   10.00,  50.00, 200.00, '2026-06-01', '2026-09-30', 3000, 0);


-- =============================================================================
-- LEVEL 1: DEPENDS ON LEVEL 0
-- =============================================================================

-- -----------------------------------------------------------------------------
-- TABLE 3: addresses (8 addresses for various users)
-- -----------------------------------------------------------------------------
-- Some users have multiple addresses (Home + Work).
-- FK: user_id → users.user_id
-- -----------------------------------------------------------------------------

INSERT INTO addresses (user_id, address_line, city, state, pincode, is_default, label) VALUES
    (1, '42, Lajpat Nagar',         'New Delhi',  'Delhi',       '110024', TRUE,  'HOME'),
    (1, '15, Connaught Place',       'New Delhi',  'Delhi',       '110001', FALSE, 'WORK'),
    (2, '88, Bandra West',           'Mumbai',     'Maharashtra', '400050', TRUE,  'HOME'),
    (3, '23, MG Road',               'Bangalore',  'Karnataka',   '560001', TRUE,  'HOME'),
    (4, '56, Jubilee Hills',         'Hyderabad',  'Telangana',   '500033', TRUE,  'HOME'),
    (4, '12, HITEC City',            'Hyderabad',  'Telangana',   '500081', FALSE, 'WORK'),
    (5, '7, Park Street',            'Kolkata',    'West Bengal',  '700016', TRUE,  'HOME'),
    (3, '101, Indiranagar',          'Bangalore',  'Karnataka',   '560038', FALSE, 'WORK');


-- -----------------------------------------------------------------------------
-- TABLE 4: restaurants (5 restaurants owned by OWNER-role users)
-- -----------------------------------------------------------------------------
-- FK: owner_id → users.user_id (only users with role = 'OWNER')
-- The application layer enforces that owner_id corresponds to an OWNER user.
-- The database only enforces that owner_id exists in users.
-- avg_rating and total_reviews will be updated after reviews are inserted.
-- -----------------------------------------------------------------------------

INSERT INTO restaurants (owner_id, name, cuisine_type, address, city) VALUES
    (6, 'Spice Garden',   'North Indian',  '10, Chandni Chowk',     'New Delhi'),
    (7, 'Pizza Hub',       'Italian',       '25, Linking Road',      'Mumbai'),
    (8, 'Dragon Bowl',     'Chinese',       '8, Brigade Road',       'Bangalore'),
    (6, 'Dosa Express',    'South Indian',  '3, Karol Bagh',         'New Delhi'),
    (7, 'Biryani House',   'Hyderabadi',    '44, Colaba Causeway',   'Mumbai');


-- =============================================================================
-- LEVEL 2: DEPENDS ON LEVEL 1
-- =============================================================================

-- -----------------------------------------------------------------------------
-- TABLE 5: menu_items (25 items — 5 per restaurant)
-- -----------------------------------------------------------------------------
-- FK: restaurant_id → restaurants.restaurant_id
-- CHECK: price > 0 (enforced by database)
-- Each restaurant has a variety of items across categories.
-- -----------------------------------------------------------------------------

-- Spice Garden (restaurant_id = 1)
INSERT INTO menu_items (restaurant_id, name, description, price, category, is_vegetarian) VALUES
    (1, 'Butter Chicken',    'Creamy tomato-based chicken curry, a Mughlai classic',                350.00, 'Main Course',  FALSE),
    (1, 'Paneer Tikka',      'Marinated cottage cheese cubes grilled in tandoor',                   280.00, 'Starters',     TRUE),
    (1, 'Dal Makhani',       'Slow-cooked black lentils with butter and cream',                     220.00, 'Main Course',  TRUE),
    (1, 'Naan',              'Soft leavened flatbread baked in tandoor',                              50.00, 'Breads',       TRUE),
    (1, 'Gulab Jamun',       'Deep-fried milk dumplings soaked in rose-scented sugar syrup',          80.00, 'Desserts',     TRUE);

-- Pizza Hub (restaurant_id = 2)
INSERT INTO menu_items (restaurant_id, name, description, price, category, is_vegetarian) VALUES
    (2, 'Margherita Pizza',  'Classic pizza with mozzarella, fresh basil, and tomato sauce',         450.00, 'Pizza',        TRUE),
    (2, 'Pepperoni Pizza',   'Loaded with spicy pepperoni slices and melted cheese',                 550.00, 'Pizza',        FALSE),
    (2, 'Garlic Bread',      'Toasted bread with garlic butter and herbs',                           180.00, 'Sides',        TRUE),
    (2, 'Pasta Alfredo',     'Fettuccine in rich creamy parmesan sauce',                             320.00, 'Pasta',        TRUE),
    (2, 'Tiramisu',          'Italian coffee-flavored layered dessert with mascarpone',              200.00, 'Desserts',     TRUE);

-- Dragon Bowl (restaurant_id = 3)
INSERT INTO menu_items (restaurant_id, name, description, price, category, is_vegetarian) VALUES
    (3, 'Hakka Noodles',     'Stir-fried noodles with vegetables and Indo-Chinese spices',           250.00, 'Noodles',      TRUE),
    (3, 'Manchurian',        'Crispy vegetable balls tossed in spicy soy-chili sauce',               280.00, 'Starters',     TRUE),
    (3, 'Fried Rice',        'Wok-tossed rice with vegetables and soy sauce',                        220.00, 'Rice',         TRUE),
    (3, 'Spring Roll',       'Crispy rolls stuffed with seasoned vegetables',                        150.00, 'Starters',     TRUE),
    (3, 'Sweet Corn Soup',   'Creamy corn soup with a hint of pepper',                               120.00, 'Soups',        TRUE);

-- Dosa Express (restaurant_id = 4)
INSERT INTO menu_items (restaurant_id, name, description, price, category, is_vegetarian) VALUES
    (4, 'Masala Dosa',       'Crispy rice crepe filled with spiced potato filling, served with chutneys',  150.00, 'Dosa',    TRUE),
    (4, 'Idli Sambar',       'Steamed rice cakes served with lentil stew and coconut chutney',             100.00, 'Snacks',  TRUE),
    (4, 'Vada Pav',          'Spiced potato fritter in a bun with chutneys — Mumbai street food classic',   60.00, 'Snacks',  TRUE),
    (4, 'Filter Coffee',     'Traditional South Indian filter-brewed coffee with frothy milk',              50.00, 'Beverages', TRUE),
    (4, 'Uttapam',           'Thick rice pancake topped with onions, tomatoes, and chilies',               130.00, 'Dosa',    TRUE);

-- Biryani House (restaurant_id = 5)
INSERT INTO menu_items (restaurant_id, name, description, price, category, is_vegetarian) VALUES
    (5, 'Chicken Biryani',   'Fragrant basmati rice layered with spiced chicken, slow-cooked in dum style',  320.00, 'Biryani', FALSE),
    (5, 'Mutton Biryani',    'Rich, aromatic biryani with tender mutton pieces and saffron',                 420.00, 'Biryani', FALSE),
    (5, 'Veg Biryani',       'Aromatic rice with mixed vegetables and whole spices',                         250.00, 'Biryani', TRUE),
    (5, 'Raita',             'Cool yogurt with cucumber, onion, and mint',                                    50.00, 'Sides',   TRUE),
    (5, 'Phirni',            'Creamy ground rice pudding infused with cardamom and saffron',                   90.00, 'Desserts', TRUE);


-- =============================================================================
-- LEVEL 3: THE HUB ENTITY
-- =============================================================================

-- -----------------------------------------------------------------------------
-- TABLE 6: orders (10 orders across various statuses)
-- -----------------------------------------------------------------------------
-- FK: customer_id → users.user_id (CUSTOMER role users: 1-5)
-- FK: restaurant_id → restaurants.restaurant_id (1-5)
-- FK: delivery_partner_id → users.user_id (DELIVERY_PARTNER role: 9-11, or NULL)
-- FK: delivery_address_id → addresses.address_id (1-8)
-- FK: coupon_id → coupons.coupon_id (1-5, or NULL)
--
-- Amounts are pre-calculated to match order_items below:
--   total_amount    = SUM of all item subtotals
--   discount_amount = MIN(total × discount_pct, max_discount) from coupon
--   final_amount    = total_amount - discount_amount
-- -----------------------------------------------------------------------------

INSERT INTO orders (customer_id, restaurant_id, delivery_partner_id, delivery_address_id, coupon_id, total_amount, discount_amount, final_amount, status, delivery_time) VALUES
    -- Order 1: Aarav orders from Spice Garden, delivered by Ravi, no coupon
    (1, 1, 9,    1,    NULL, 950.00,  0.00,   950.00, 'DELIVERED',         '2026-07-20 13:45:00'),
    -- Order 2: Priya orders from Pizza Hub, delivered by Sana, coupon WELCOME50 (50% off, max ₹100)
    (2, 2, 10,   3,    1,    810.00,  100.00, 710.00, 'DELIVERED',         '2026-07-21 20:30:00'),
    -- Order 3: Vikram orders from Dragon Bowl, delivered by Deepak, no coupon
    (3, 3, 11,   4,    NULL, 1230.00, 0.00,   1230.00, 'DELIVERED',        '2026-07-22 19:15:00'),
    -- Order 4: Meera orders from Dosa Express, delivered by Ravi, coupon FLAT100 (20% off, max ₹100)
    (4, 4, 9,    5,    2,    500.00,  100.00, 400.00, 'DELIVERED',         '2026-07-23 08:30:00'),
    -- Order 5: Aarav orders from Biryani House, no partner yet (just placed)
    (1, 5, NULL, 2,    NULL, 420.00,  0.00,   420.00, 'PLACED',            NULL),
    -- Order 6: Rohan orders from Spice Garden, Sana delivering, coupon FESTIVE30 (30% off, max ₹150)
    (5, 1, 10,   7,    3,    1140.00, 150.00, 990.00, 'OUT_FOR_DELIVERY',  NULL),
    -- Order 7: Priya orders from Dragon Bowl, no partner yet (confirmed)
    (2, 3, NULL, 3,    NULL, 340.00,  0.00,   340.00, 'CONFIRMED',         NULL),
    -- Order 8: Vikram orders from Pizza Hub, delivered by Deepak, no coupon
    (3, 2, 11,   8,    NULL, 1070.00, 0.00,   1070.00, 'DELIVERED',        '2026-07-25 21:00:00'),
    -- Order 9: Meera orders from Spice Garden, no partner yet (preparing)
    (4, 1, NULL, 6,    NULL, 720.00,  0.00,   720.00, 'PREPARING',         NULL),
    -- Order 10: Aarav orders from Dosa Express, Ravi delivering, coupon NEWUSER (40% off, max ₹80), CANCELLED
    (1, 4, 9,    1,    4,    290.00,  80.00,  210.00, 'CANCELLED',         NULL);


-- =============================================================================
-- LEVEL 4: DEPENDS ON LEVEL 3
-- =============================================================================

-- -----------------------------------------------------------------------------
-- TABLE 7: order_items (line items for each order)
-- -----------------------------------------------------------------------------
-- PK: (order_id, item_id) — composite key
-- FK: order_id → orders.order_id
-- FK: item_id → menu_items.item_id
-- price_at_order = the price captured at the moment of ordering
-- subtotal = price_at_order × quantity
-- CHECK: quantity > 0
-- -----------------------------------------------------------------------------

-- Order 1: Aarav @ Spice Garden — Butter Chicken ×2, Naan ×5 → total 950
INSERT INTO order_items (order_id, item_id, quantity, price_at_order, subtotal) VALUES
    (1, 1, 2, 350.00, 700.00),   -- Butter Chicken × 2
    (1, 4, 5,  50.00, 250.00);   -- Naan × 5

-- Order 2: Priya @ Pizza Hub — Margherita ×1, Garlic Bread ×2 → total 810
INSERT INTO order_items (order_id, item_id, quantity, price_at_order, subtotal) VALUES
    (2, 6, 1, 450.00, 450.00),   -- Margherita Pizza × 1
    (2, 8, 2, 180.00, 360.00);   -- Garlic Bread × 2

-- Order 3: Vikram @ Dragon Bowl — Hakka Noodles ×2, Manchurian ×1, Spring Roll ×3 → total 1230
INSERT INTO order_items (order_id, item_id, quantity, price_at_order, subtotal) VALUES
    (3, 11, 2, 250.00, 500.00),  -- Hakka Noodles × 2
    (3, 12, 1, 280.00, 280.00),  -- Manchurian × 1
    (3, 14, 3, 150.00, 450.00);  -- Spring Roll × 3

-- Order 4: Meera @ Dosa Express — Masala Dosa ×2, Filter Coffee ×2, Idli Sambar ×1 → total 500
INSERT INTO order_items (order_id, item_id, quantity, price_at_order, subtotal) VALUES
    (4, 16, 2, 150.00, 300.00),  -- Masala Dosa × 2
    (4, 19, 2,  50.00, 100.00),  -- Filter Coffee × 2
    (4, 17, 1, 100.00, 100.00);  -- Idli Sambar × 1

-- Order 5: Aarav @ Biryani House — Chicken Biryani ×1, Raita ×2 → total 420
INSERT INTO order_items (order_id, item_id, quantity, price_at_order, subtotal) VALUES
    (5, 21, 1, 320.00, 320.00),  -- Chicken Biryani × 1
    (5, 24, 2,  50.00, 100.00);  -- Raita × 2

-- Order 6: Rohan @ Spice Garden — Paneer Tikka ×2, Dal Makhani ×1, Naan ×4, Gulab Jamun ×2 → total 1140
INSERT INTO order_items (order_id, item_id, quantity, price_at_order, subtotal) VALUES
    (6, 2, 2, 280.00, 560.00),   -- Paneer Tikka × 2
    (6, 3, 1, 220.00, 220.00),   -- Dal Makhani × 1
    (6, 4, 4,  50.00, 200.00),   -- Naan × 4
    (6, 5, 2,  80.00, 160.00);   -- Gulab Jamun × 2

-- Order 7: Priya @ Dragon Bowl — Fried Rice ×1, Sweet Corn Soup ×1 → total 340
INSERT INTO order_items (order_id, item_id, quantity, price_at_order, subtotal) VALUES
    (7, 13, 1, 220.00, 220.00),  -- Fried Rice × 1
    (7, 15, 1, 120.00, 120.00);  -- Sweet Corn Soup × 1

-- Order 8: Vikram @ Pizza Hub — Pepperoni Pizza ×1, Pasta Alfredo ×1, Tiramisu ×1 → total 1070
INSERT INTO order_items (order_id, item_id, quantity, price_at_order, subtotal) VALUES
    (8, 7,  1, 550.00, 550.00),  -- Pepperoni Pizza × 1
    (8, 9,  1, 320.00, 320.00),  -- Pasta Alfredo × 1
    (8, 10, 1, 200.00, 200.00);  -- Tiramisu × 1

-- Order 9: Meera @ Spice Garden — Butter Chicken ×1, Dal Makhani ×1, Naan ×3 → total 720
INSERT INTO order_items (order_id, item_id, quantity, price_at_order, subtotal) VALUES
    (9, 1, 1, 350.00, 350.00),   -- Butter Chicken × 1
    (9, 3, 1, 220.00, 220.00),   -- Dal Makhani × 1
    (9, 4, 3,  50.00, 150.00);   -- Naan × 3

-- Order 10: Aarav @ Dosa Express — Vada Pav ×4, Filter Coffee ×1 → total 290 (CANCELLED)
INSERT INTO order_items (order_id, item_id, quantity, price_at_order, subtotal) VALUES
    (10, 18, 4,  60.00, 240.00), -- Vada Pav × 4
    (10, 19, 1,  50.00,  50.00); -- Filter Coffee × 1


-- -----------------------------------------------------------------------------
-- TABLE 8: payments (one per order — enforced by UNIQUE on order_id)
-- -----------------------------------------------------------------------------
-- FK + UK: order_id → orders.order_id (1:1 relationship)
-- Each order has exactly one payment record.
-- Delivered/active orders have COMPLETED payments.
-- PLACED order has PENDING payment.
-- CANCELLED order has REFUNDED payment.
-- -----------------------------------------------------------------------------

INSERT INTO payments (order_id, amount, payment_method, payment_status) VALUES
    (1,  950.00,  'UPI',    'COMPLETED'),
    (2,  710.00,  'CARD',   'COMPLETED'),
    (3,  1230.00, 'UPI',    'COMPLETED'),
    (4,  400.00,  'WALLET', 'COMPLETED'),
    (5,  420.00,  'UPI',    'PENDING'),
    (6,  990.00,  'CASH',   'COMPLETED'),
    (7,  340.00,  'UPI',    'COMPLETED'),
    (8,  1070.00, 'CARD',   'COMPLETED'),
    (9,  720.00,  'UPI',    'COMPLETED'),
    (10, 210.00,  'WALLET', 'REFUNDED');


-- -----------------------------------------------------------------------------
-- TABLE 9: reviews (5 reviews — only for DELIVERED orders)
-- -----------------------------------------------------------------------------
-- FK: customer_id → users.user_id
-- FK: restaurant_id → restaurants.restaurant_id
-- FK + UK: order_id → orders.order_id (1:1, one review per order)
-- CHECK: rating BETWEEN 1 AND 5
-- Only delivered orders can be reviewed (enforced by application, not DB).
-- -----------------------------------------------------------------------------

INSERT INTO reviews (customer_id, restaurant_id, order_id, rating, comment) VALUES
    (1, 1, 1, 5, 'Absolutely fantastic Butter Chicken! The gravy was rich and creamy. Naan was perfectly fluffy. Will definitely order again.'),
    (2, 2, 2, 4, 'Good pizza, nice crust. Garlic bread was a bit oily though. Delivery was fast. Would recommend.'),
    (3, 3, 3, 3, 'Noodles were decent but could use more spice. Spring rolls were crispy and fresh. Average overall experience.'),
    (4, 4, 4, 5, 'Best Masala Dosa I have had outside South India! Crispy, authentic, and the filter coffee was perfect. Hidden gem!'),
    (3, 2, 8, 4, 'Pepperoni pizza was loaded with toppings. Pasta was creamy. Good value for money. Tiramisu was a nice touch.');


-- =============================================================================
-- POST-INSERT: UPDATE DENORMALIZED COLUMNS
-- =============================================================================
-- avg_rating and total_reviews in restaurants are denormalized (stored for
-- read performance). In production, a TRIGGER would update these automatically
-- on every INSERT/UPDATE/DELETE in reviews. For now, we update manually.
-- We will create the trigger in Phase 9.
-- =============================================================================

UPDATE restaurants SET avg_rating = 5.0, total_reviews = 1 WHERE restaurant_id = 1;  -- Spice Garden: 1 review, 5 stars
UPDATE restaurants SET avg_rating = 4.0, total_reviews = 2 WHERE restaurant_id = 2;  -- Pizza Hub: 2 reviews, avg (4+4)/2 = 4.0
UPDATE restaurants SET avg_rating = 3.0, total_reviews = 1 WHERE restaurant_id = 3;  -- Dragon Bowl: 1 review, 3 stars
UPDATE restaurants SET avg_rating = 5.0, total_reviews = 1 WHERE restaurant_id = 4;  -- Dosa Express: 1 review, 5 stars
-- Biryani House: 0 reviews, stays at default (0.0, 0)


-- =============================================================================
-- VERIFICATION QUERIES — Run these to confirm data integrity
-- =============================================================================

SELECT '--- TABLE ROW COUNTS ---' AS '';
SELECT 'users'       AS table_name, COUNT(*) AS row_count FROM users
UNION ALL
SELECT 'coupons',     COUNT(*) FROM coupons
UNION ALL
SELECT 'addresses',   COUNT(*) FROM addresses
UNION ALL
SELECT 'restaurants', COUNT(*) FROM restaurants
UNION ALL
SELECT 'menu_items',  COUNT(*) FROM menu_items
UNION ALL
SELECT 'orders',      COUNT(*) FROM orders
UNION ALL
SELECT 'order_items', COUNT(*) FROM order_items
UNION ALL
SELECT 'payments',    COUNT(*) FROM payments
UNION ALL
SELECT 'reviews',     COUNT(*) FROM reviews;

SELECT '--- SAMPLE DATA INSERTED SUCCESSFULLY ---' AS status;
