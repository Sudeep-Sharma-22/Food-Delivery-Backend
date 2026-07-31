CREATE DATABASE IF NOT EXISTS food_delivery CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE food_delivery;

CREATE TABLE IF NOT EXISTS users (
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

CREATE TABLE IF NOT EXISTS coupons (
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

CREATE TABLE IF NOT EXISTS addresses (
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

CREATE TABLE IF NOT EXISTS restaurants (
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

CREATE TABLE IF NOT EXISTS menu_items (
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

CREATE TABLE IF NOT EXISTS orders (
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

CREATE TABLE IF NOT EXISTS order_items (
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

CREATE TABLE IF NOT EXISTS payments (
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

CREATE TABLE IF NOT EXISTS reviews (
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

SELECT 'Database food_delivery created successfully with 9 tables.' AS status;
