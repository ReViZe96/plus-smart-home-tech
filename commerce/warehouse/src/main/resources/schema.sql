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
                      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                      address_city VARCHAR(64),
                      address_street VARCHAR(128),
                      address_house VARCHAR(32),
                      address_country VARCHAR(32),
                      address_flat VARCHAR(32)
);

INSERT INTO warehouses (id, address_city, address_street, address_house, address_country, address_flat)
                      VALUES (1, 'ADDRESS_1', 'ADDRESS_1', 'ADDRESS_1', 'ADDRESS_1', 'ADDRESS_1');

INSERT INTO warehouses (id, address_city, address_street, address_house, address_country, address_flat)
                      VALUES (2, 'ADDRESS_2', 'ADDRESS_2', 'ADDRESS_2', 'ADDRESS_2', 'ADDRESS_2');