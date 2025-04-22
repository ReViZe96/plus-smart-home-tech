CREATE TABLE IF NOT EXISTS deliveries (
                      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                      from_warehouse_address varchar(512) NOT NULL,
                      to_warehouse_address varchar(512) NOT NULL,
                      order_id varchar(100) NOT NULL,
                      state varchar(32) NOT NULL,
                      volume real NOT NULL,
                      weigh real NOT NULL,
                      is_fragile boolean NOT NULL
                      );