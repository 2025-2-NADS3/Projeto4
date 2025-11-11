-- init.sql: cria tabela products e insere alguns registros de exemplo
CREATE DATABASE IF NOT EXISTS comedoria;
USE comedoria;

CREATE TABLE IF NOT EXISTS products (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  price DECIMAL(10,2) NOT NULL
) ENGINE=InnoDB;

INSERT INTO products (name, price) VALUES
('Coxinha', 5.00),
('Pão de Queijo', 3.50),
('Suco Natural', 6.00)
ON DUPLICATE KEY UPDATE name = VALUES(name);
