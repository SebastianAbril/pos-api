CREATE TABLE product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(150) NOT NULL,
    price DECIMAL(10,2),
    code CHAR(2) NOT NULL
);

CREATE TABLE inventory (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE inventory_movements (
    id INT PRIMARY KEY AUTO_INCREMENT,
    origin_user_id INT NOT NULL,
    destiny_user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity_transferred INT NOT NULL,
    date DATE,
    hour TIME,
    FOREIGN KEY (origin_user_id) REFERENCES user(id),
    FOREIGN KEY (destiny_user_id) REFERENCES user(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);


