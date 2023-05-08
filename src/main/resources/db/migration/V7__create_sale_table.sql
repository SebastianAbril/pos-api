CREATE TABLE sales(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    user_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity_sold INTEGER NOT NULL,
    date DATE,
    time TIME,
    total_value DOUBLE NOT NULL
);