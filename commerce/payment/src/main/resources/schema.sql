CREATE TABLE IF NOT EXISTS payments (
                      id varchar(100) PRIMARY KEY,
                      total_cost REAL,
                      product_cost REAL,
                      delivery_cost REAL,
                      state varchar(32),
                      fee_cost REAL
                      );