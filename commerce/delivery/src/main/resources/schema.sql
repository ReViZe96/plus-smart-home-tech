CREATE TABLE IF NOT EXISTS deliveries (
                      id varchar(100) PRIMARY KEY,
                      from_warehouse_address varchar(512) NOT NULL,
                      to_warehouse_address varchar(512) NOT NULL,
                      order_id varchar(100) NOT NULL,
                      state varchar(32) NOT NULL
                      );