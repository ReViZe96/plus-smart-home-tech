CREATE TABLE IF NOT EXISTS payments (
                      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                      total_cost REAL,
                      product_cost REAL,
                      delivery_cost REAL,
                      state varchar(32),
                      fee_cost REAL
                      );