CREATE TABLE product (
  id       BIGINT(20)     AUTO_INCREMENT PRIMARY KEY,
  name     VARCHAR(512)   NOT NULL,
  price    DECIMAL(20, 2) NOT NULL,
  category VARCHAR(128)    NOT NULL,
  discount DECIMAL(20, 2) NOT NULL,
  status   VARCHAR(16)    NOT NULL
) COMMENT 'A product to sell in our imaginary store';