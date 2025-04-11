CREATE TABLE IF NOT EXISTS Carts (
    id varchar(100) PRIMARY KEY,
    username varchar(50) NOT NULL,
    active boolean NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS Products (
    cart_id varchar(100) REFERENCES Carts(id) ON DELETE CASCADE,
    product_id varchar(100) NOT NULL,
    quantity bigint NOT NULL,
    PRIMARY KEY(cart_id, product_id)
);