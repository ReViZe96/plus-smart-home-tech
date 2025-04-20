CREATE TABLE IF NOT EXISTS orders (
                      id varchar(100) PRIMARY KEY,
                      cart_id varchar(100),
                      payment_id varchar(100),
                      delivery_id varchar(100),
                      order_state varchar(32) NOT NULL,
                      delivery_weight REAL,
                      delivery_volume REAL,
                      delivery_fragile BOOLEAN,
                      total_price REAL,
                      delivery_price REAL,
                      product_price REAL
                      );

CREATE TABLE IF NOT EXISTS products (
                      order_id varchar(100) REFERENCES orders(id) ON DELETE CASCADE,
                      product_id varchar(100) NOT NULL,
                      product_quantity bigint NOT NULL,
                      PRIMARY KEY(order_id, product_id)
                      );