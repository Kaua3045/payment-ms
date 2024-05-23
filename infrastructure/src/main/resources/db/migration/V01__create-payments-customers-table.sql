CREATE TABLE payments_customers (
    id VARCHAR(32) PRIMARY KEY NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    account_id VARCHAR(36) UNIQUE NOT NULL,
    payment_gateway_customer_id VARCHAR(255) UNIQUE NOT NULL
);