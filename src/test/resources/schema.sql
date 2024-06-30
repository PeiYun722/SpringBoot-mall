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

CREATE TABLE `customer`
(
    `customer_id`        INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `email`              VARCHAR(256) NOT NULL UNIQUE,
    `password`           VARCHAR(256) NOT NULL,
    `created_date`       TIMESTAMP    NOT NULL,
    `last_modified_date` TIMESTAMP    NOT NULL
);