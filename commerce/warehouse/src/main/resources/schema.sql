CREATE TABLE IF NOT EXISTS items (
                      id VARCHAR(100) PRIMARY KEY,
                      quantity_in_warehouse BIGINT,
                      fragile BOOLEAN,
                      weight REAL,
                      width REAL,
                      height REAL,
                      depth REAL
);

CREATE TABLE IF NOT EXISTS warehouses (
                      id VARCHAR(100) PRIMARY KEY,
                      address_city VARCHAR(64),
                      address_street VARCHAR(128),
                      address_house VARCHAR(4),
                      address_country VARCHAR(32),
                      address_flat VARCHAR(4)
);