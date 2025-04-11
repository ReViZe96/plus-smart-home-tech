CREATE TABLE IF NOT EXISTS Products (
                      id varchar(100) PRIMARY KEY,
                      name varchar(100) NOT NULL,
                      description varchar(1024) NOT NULL,
                      image_src varchar(100),
                      quantity_state varchar(10) NOT NULL,
                      product_state varchar(10) NOT NULL,
                      category varchar(10),
                      price real
);