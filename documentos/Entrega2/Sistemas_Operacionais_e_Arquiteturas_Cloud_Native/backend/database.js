require("dotenv").config();
const mysql = require("mysql2/promise");

// Create a connection pool using environment variables
const pool = mysql.createPool({
  host: process.env.DB_HOST || "localhost",
  user: process.env.DB_USER || "root",
  password: process.env.DB_PASSWORD || "",
  database: process.env.DB_NAME || "comedoria",
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0,
});

// Wait for the DB to accept connections with retry/backoff
async function waitForDb(retries = 15, delayMs = 2000) {
  for (let i = 0; i < retries; i++) {
    try {
      const conn = await pool.getConnection();
      conn.release();
      console.log("Conexão com o MySQL estabelecida.");
      return true;
    } catch (err) {
      console.log(
        `Tentativa ${i + 1}/${retries} de conectar ao MySQL falhou: ${
          err.code || err.message
        }`
      );
      // last attempt -> rethrow
      if (i === retries - 1) throw err;
      await new Promise((res) => setTimeout(res, delayMs));
    }
  }
}

async function initialize() {
  // The SQL initialization (table creation) is handled by mysql-init/init.sql in docker-compose,
  // but we keep this function to attempt a safe create when connecting locally for development.
  const createTable = `
        CREATE TABLE IF NOT EXISTS products (
            id INT AUTO_INCREMENT PRIMARY KEY,
            name VARCHAR(255) NOT NULL,
            price DECIMAL(10,2) NOT NULL
        ) ENGINE=InnoDB;
    `;
  try {
    // Wait for DB to be ready before running queries
    await waitForDb();
    const conn = await pool.getConnection();
    await conn.query(createTable);
    conn.release();
    console.log("Tabela products garantida/creatada.");
  } catch (err) {
    console.error("Erro ao garantir tabela products:", err.message);
  }
}

// Initialize once (best-effort)
initialize();

module.exports = pool;
