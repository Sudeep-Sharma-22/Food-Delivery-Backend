-- =============================================================================
-- FOOD DELIVERY BACKEND — DATABASE SCHEMA
-- =============================================================================
-- File:    create_database.sql
-- Purpose: Creates the database and all 9 tables with constraints.
-- Order:   Tables are created in topological order of FK dependencies.
-- Usage:   Run this script in MySQL: source create_database.sql;
-- =============================================================================

-- -----------------------------------------------------------------------------
-- STEP 1: CREATE AND USE THE DATABASE
-- -----------------------------------------------------------------------------

DROP DATABASE IF EXISTS food_delivery;
CREATE DATABASE food_delivery CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE food_delivery;


-- =============================================================================
-- LEVEL 0: ROOT ENTITIES (No foreign key dependencies)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- TABLE 1: users
-- -----------------------------------------------------------------------------
-- Root entity. All person-types (Customer, Owner, Delivery Partner) in one
-- table, differentiated by the 'role' column. Referenced by 6 FKs across
-- the schema. No table needs to exist before this one.
-- -----------------------------------------------------------------------------

CREATE TABLE users (
    user_id     INT             AUTO_INCREMENT,
    name        VARCHAR(100)    NOT NULL,
    email       VARCHAR(150)    NOT NULL,
    phone       VARCHAR(15)     NOT NULL,
    password    VARCHAR(255)    NOT NULL,
    role        ENUM('CUSTOMER', 'OWNER', 'DELIVERY_PARTNER') NOT NULL,
    created_at  TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    is_active   BOOLEAN         DEFAULT TRUE,

    CONSTRAINT pk_users        PRIMARY KEY (user_id),
    CONSTRAINT uk_users_email  UNIQUE      (email)
);


-- -----------------------------------------------------------------------------
-- TABLE 2: coupons
-- -----------------------------------------------------------------------------
-- Root entity. Promotional discount codes. Referenced by orders.coupon_id.
-- No FK dependencies — can be created alongside users.
-- -----------------------------------------------------------------------------

CREATE TABLE coupons (
    coupon_id           INT             AUTO_INCREMENT,
    code                VARCHAR(20)     NOT NULL,
    discount_percentage DECIMAL(5,2)    NOT NULL,
    max_discount_amount DECIMAL(10,2)   NOT NULL,
    min_order_amount    DECIMAL(10,2)   DEFAULT 0.00,
    valid_from          DATE            NOT NULL,
    valid_until         DATE            NOT NULL,
    max_usage           INT             NOT NULL,
    current_usage       INT             DEFAULT 0,
    is_active           BOOLEAN         DEFAULT TRUE,

    CONSTRAINT pk_coupons       PRIMARY KEY (coupon_id),
    CONSTRAINT uk_coupons_code  UNIQUE      (code),
    CONSTRAINT ck_coupons_discount_pct   CHECK (discount_percentage BETWEEN 0 AND 100),
    CONSTRAINT ck_coupons_max_discount   CHECK (max_discount_amount > 0),
    CONSTRAINT ck_coupons_min_order      CHECK (min_order_amount >= 0),
    CONSTRAINT ck_coupons_date_range     CHECK (valid_until >= valid_from),
    CONSTRAINT ck_coupons_max_usage      CHECK (max_usage > 0),
    CONSTRAINT ck_coupons_current_usage  CHECK (current_usage >= 0)
);


-- =============================================================================
-- LEVEL 1: ENTITIES DEPENDING ON LEVEL 0
-- =============================================================================

-- -----------------------------------------------------------------------------
-- TABLE 3: addresses
-- -----------------------------------------------------------------------------
-- Depends on: users (FK: user_id → users.user_id)
-- Implements the 1:N relationship: Users → Addresses
-- A user can have multiple addresses (Home, Work, Other).
-- -----------------------------------------------------------------------------

CREATE TABLE addresses (
    address_id   INT             AUTO_INCREMENT,
    user_id      INT             NOT NULL,
    address_line VARCHAR(255)    NOT NULL,
    city         VARCHAR(100)    NOT NULL,
    state        VARCHAR(100)    NOT NULL,
    pincode      VARCHAR(10)     NOT NULL,
    is_default   BOOLEAN         DEFAULT FALSE,
    label        ENUM('HOME', 'WORK', 'OTHER') DEFAULT 'HOME',

    CONSTRAINT pk_addresses         PRIMARY KEY (address_id),
    CONSTRAINT fk_addresses_user    FOREIGN KEY (user_id)
        REFERENCES users (user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


-- -----------------------------------------------------------------------------
-- TABLE 4: restaurants
-- -----------------------------------------------------------------------------
-- Depends on: users (FK: owner_id → users.user_id)
-- Implements the 1:N relationship: Users (Owners) → Restaurants
-- avg_rating and total_reviews are denormalized (kept in sync by trigger).
-- -----------------------------------------------------------------------------

CREATE TABLE restaurants (
    restaurant_id  INT             AUTO_INCREMENT,
    owner_id       INT             NOT NULL,
    name           VARCHAR(150)    NOT NULL,
    cuisine_type   VARCHAR(100)    NOT NULL,
    address        VARCHAR(255)    NOT NULL,
    city           VARCHAR(100)    NOT NULL,
    avg_rating     DECIMAL(3,1)    DEFAULT 0.0,
    total_reviews  INT             DEFAULT 0,
    is_open        BOOLEAN         DEFAULT TRUE,
    is_active      BOOLEAN         DEFAULT TRUE,
    created_at     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_restaurants       PRIMARY KEY (restaurant_id),
    CONSTRAINT fk_restaurants_owner FOREIGN KEY (owner_id)
        REFERENCES users (user_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);


-- =============================================================================
-- LEVEL 2: ENTITIES DEPENDING ON LEVEL 1
-- =============================================================================

-- -----------------------------------------------------------------------------
-- TABLE 5: menu_items
-- -----------------------------------------------------------------------------
-- Depends on: restaurants (FK: restaurant_id → restaurants.restaurant_id)
-- Implements the 1:N relationship: Restaurants → Menu Items
-- CHECK constraint ensures price is always positive.
-- -----------------------------------------------------------------------------

CREATE TABLE menu_items (
    item_id        INT             AUTO_INCREMENT,
    restaurant_id  INT             NOT NULL,
    name           VARCHAR(150)    NOT NULL,
    description    VARCHAR(500)    NULL,
    price          DECIMAL(10,2)   NOT NULL,
    category       VARCHAR(50)     NOT NULL,
    is_vegetarian  BOOLEAN         DEFAULT FALSE,
    is_available   BOOLEAN         DEFAULT TRUE,
    created_at     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_menu_items            PRIMARY KEY (item_id),
    CONSTRAINT fk_menu_items_restaurant FOREIGN KEY (restaurant_id)
        REFERENCES restaurants (restaurant_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT ck_menu_items_price      CHECK (price > 0)
);


-- =============================================================================
-- LEVEL 3: THE HUB ENTITY (Depends on Levels 0, 1, and 2)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- TABLE 6: orders
-- -----------------------------------------------------------------------------
-- Depends on: users, restaurants, addresses, coupons
-- The most connected table — 5 foreign keys.
-- Two FKs reference users (customer_id and delivery_partner_id).
-- delivery_partner_id and coupon_id are NULLABLE (partial participation).
-- -----------------------------------------------------------------------------

CREATE TABLE orders (
    order_id             INT             AUTO_INCREMENT,
    customer_id          INT             NOT NULL,
    restaurant_id        INT             NOT NULL,
    delivery_partner_id  INT             NULL,
    delivery_address_id  INT             NOT NULL,
    coupon_id            INT             NULL,
    total_amount         DECIMAL(10,2)   NOT NULL,
    discount_amount      DECIMAL(10,2)   DEFAULT 0.00,
    final_amount         DECIMAL(10,2)   NOT NULL,
    status               ENUM('PLACED', 'CONFIRMED', 'PREPARING', 'READY',
                              'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED')
                                         DEFAULT 'PLACED',
    order_time           TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    delivery_time        TIMESTAMP       NULL,

    CONSTRAINT pk_orders                    PRIMARY KEY (order_id),
    CONSTRAINT fk_orders_customer           FOREIGN KEY (customer_id)
        REFERENCES users (user_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_orders_restaurant         FOREIGN KEY (restaurant_id)
        REFERENCES restaurants (restaurant_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_orders_delivery_partner   FOREIGN KEY (delivery_partner_id)
        REFERENCES users (user_id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    CONSTRAINT fk_orders_delivery_address   FOREIGN KEY (delivery_address_id)
        REFERENCES addresses (address_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_orders_coupon             FOREIGN KEY (coupon_id)
        REFERENCES coupons (coupon_id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);


-- =============================================================================
-- LEVEL 4: ENTITIES DEPENDING ON LEVEL 3 (the orders table)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- TABLE 7: order_items (Junction Table / Associative Entity)
-- -----------------------------------------------------------------------------
-- Depends on: orders, menu_items
-- Resolves the M:N relationship: Orders ↔ Menu Items
-- COMPOSITE PRIMARY KEY: (order_id, item_id) — no auto-increment.
-- CHECK constraint ensures quantity is always positive.
-- -----------------------------------------------------------------------------

CREATE TABLE order_items (
    order_id        INT             NOT NULL,
    item_id         INT             NOT NULL,
    quantity        INT             NOT NULL,
    price_at_order  DECIMAL(10,2)   NOT NULL,
    subtotal        DECIMAL(10,2)   NOT NULL,

    CONSTRAINT pk_order_items           PRIMARY KEY (order_id, item_id),
    CONSTRAINT fk_order_items_order     FOREIGN KEY (order_id)
        REFERENCES orders (order_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_order_items_menu_item FOREIGN KEY (item_id)
        REFERENCES menu_items (item_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT ck_order_items_quantity   CHECK (quantity > 0)
);


-- -----------------------------------------------------------------------------
-- TABLE 8: payments
-- -----------------------------------------------------------------------------
-- Depends on: orders
-- Implements 1:1 relationship: Orders ↔ Payments
-- The 1:1 is enforced by FK + UNIQUE on order_id.
-- RESTRICT on delete — financial records must survive.
-- -----------------------------------------------------------------------------

CREATE TABLE payments (
    payment_id       INT             AUTO_INCREMENT,
    order_id         INT             NOT NULL,
    amount           DECIMAL(10,2)   NOT NULL,
    payment_method   ENUM('CASH', 'CARD', 'UPI', 'WALLET') NOT NULL,
    payment_status   ENUM('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED') DEFAULT 'PENDING',
    transaction_time TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_payments           PRIMARY KEY (payment_id),
    CONSTRAINT fk_payments_order     FOREIGN KEY (order_id)
        REFERENCES orders (order_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT uk_payments_order_id  UNIQUE (order_id)
);


-- -----------------------------------------------------------------------------
-- TABLE 9: reviews
-- -----------------------------------------------------------------------------
-- Depends on: users, restaurants, orders
-- Three FKs connecting customer, restaurant, and order.
-- FK + UNIQUE on order_id implements 1:1 (one review per order).
-- CHECK constraint restricts rating to 1–5.
-- restaurant_id is denormalized (could be derived via order → restaurant join).
-- -----------------------------------------------------------------------------

CREATE TABLE reviews (
    review_id      INT             AUTO_INCREMENT,
    customer_id    INT             NOT NULL,
    restaurant_id  INT             NOT NULL,
    order_id       INT             NOT NULL,
    rating         INT             NOT NULL,
    comment        TEXT            NULL,
    created_at     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_reviews               PRIMARY KEY (review_id),
    CONSTRAINT fk_reviews_customer      FOREIGN KEY (customer_id)
        REFERENCES users (user_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_reviews_restaurant    FOREIGN KEY (restaurant_id)
        REFERENCES restaurants (restaurant_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_reviews_order         FOREIGN KEY (order_id)
        REFERENCES orders (order_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT uk_reviews_order_id      UNIQUE (order_id),
    CONSTRAINT ck_reviews_rating        CHECK (rating BETWEEN 1 AND 5)
);


-- =============================================================================
-- SCHEMA CREATION COMPLETE
-- =============================================================================
-- Tables created: 9
-- Primary Keys:   9  (8 surrogate + 1 composite)
-- Foreign Keys:  13
-- Unique Keys:    4  (users.email, coupons.code, payments.order_id, reviews.order_id)
-- Check Constr:   9  (price, quantity, rating, 6 coupon rules)
-- =============================================================================

SELECT 'Database food_delivery created successfully with 9 tables.' AS status;
