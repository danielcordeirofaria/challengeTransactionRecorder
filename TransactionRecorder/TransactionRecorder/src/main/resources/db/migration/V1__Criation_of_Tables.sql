CREATE TABLE TRANSACTION (
    id_transaction INT PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(50) NOT NULL UNIQUE,
    date DATETIME NOT NULL,
    puchase_amount DOUBLE NOT NULL
);