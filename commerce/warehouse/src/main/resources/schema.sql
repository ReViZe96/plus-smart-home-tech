CREATE TABLE IF NOT EXISTS Products (
                      id VARCHAR(100) PRIMARY KEY,
                      quantity_in_warehouse BIGINT,
                      fragile BOOLEAN,
                      weight REAL,
                      width REAL,
                      height REAL,
                      depth REAL
);

CREATE TABLE IF NOT EXISTS Warehouses (
                      id VARCHAR(100) PRIMARY KEY,
                      address VARCHAR(1024)
);