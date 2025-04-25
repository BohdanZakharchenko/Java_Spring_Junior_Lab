CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password TEXT NOT NULL,
                       role ENUM('client', 'employee', 'supplier', 'admin') NOT NULL,
                       phone VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE fruits (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        refrigeration_required BOOLEAN NOT NULL DEFAULT FALSE,
                        image_url TEXT,
                        description TEXT
);

CREATE TABLE countries (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE streets (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL UNIQUE,
                         delivery_time_minutes INT NOT NULL CHECK (delivery_time_minutes > 0)
);

CREATE TABLE addresses (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           user_id INT NOT NULL,
                           street_id INT NOT NULL,
                           building VARCHAR(20) NOT NULL,
                           apartment VARCHAR(10),
                           FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                           FOREIGN KEY (street_id) REFERENCES streets(id) ON DELETE CASCADE
);

CREATE TABLE fruit_prices
    id INT AUTO_INCREMENT PRIMARY KEY,
    fruit_id INT NOT NULL,
    country_id INT NOT NULL,
    income_price DECIMAL(10,2) NOT NULL CHECK (income_price >= 0),
    out_price DECIMAL(10,2) NOT NULL CHECK (out_price >= 0),
    initial_quantity INT NOT NULL CHECK (quantity > 0),
    current_quantity INT NOT NULL CHECK (quantity >= 0),
    unit ENUM('kg', 'pcs', 'liters') NOT NULL,
    supplier_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by INT,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (fruit_id) REFERENCES fruits(id) ON DELETE CASCADE,
    FOREIGN KEY (country_id) REFERENCES countries(id) ON DELETE CASCADE,
    FOREIGN KEY (supplier_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (updated_by) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE orders (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        user_id INT NOT NULL,
                        address_id INT NOT NULL,
                        status ENUM('pending', 'processing', 'shipped', 'delivered', 'cancelled') NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                        FOREIGN KEY (address_id) REFERENCES addresses(id) ON DELETE CASCADE
);


CREATE TABLE order_items (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             order_id INT NOT NULL,
                             fruit_price_id INT NOT NULL,
                             quantity INT NOT NULL CHECK (quantity > 0),
                             FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
                             FOREIGN KEY (fruit_price_id) REFERENCES fruit_prices(id) ON DELETE CASCADE
);

CREATE TABLE payments (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          order_id INT NOT NULL,
                          method ENUM('card', 'cash', 'paypal') NOT NULL,
                          status ENUM('pending', 'completed', 'failed', 'refunded') NOT NULL,
                          transaction_id VARCHAR(100),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);
