CREATE TABLE  if not exists product (
                           `product_id` int AUTO_INCREMENT NOT NULL PRIMARY KEY,
                           `product_name` varchar(128) NOT NULL,
                           `category` varchar(32) NOT NULL,
                           `image_url` varchar(256) NOT NULL,
                           `price` int NOT NULL,
                           `stock` int NOT NULL,
                           `description` varchar(1024) DEFAULT NULL,
                           `created_date` timestamp NOT NULL,
                           `last_modified_date` timestamp NOT NULL
);

CREATE TABLE if not exists `customer`
(
    `customer_id`        INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `email`              VARCHAR(256) NOT NULL UNIQUE,
    `password`           VARCHAR(256) NOT NULL,
    `created_date`       TIMESTAMP    NOT NULL,
    `last_modified_date` TIMESTAMP    NOT NULL
);

CREATE TABLE if not exists  order_detail
(
    order_detail_id    INT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    customer_id        INT       NOT NULL,
    total_amount       INT       NOT NULL, -- 訂單總花費
    created_date       TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NOT NULL
);

CREATE TABLE if not exists  order_item
(
    order_item_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    order_detail_id      INT NOT NULL,
    product_id    INT NOT NULL,
    quantity      INT NOT NULL, -- 商品數量
    amount        INT NOT NULL  -- 商品總花費
);