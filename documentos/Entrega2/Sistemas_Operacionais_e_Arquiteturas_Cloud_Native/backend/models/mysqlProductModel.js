const pool = require("../database");

// Keep same callback-style interface as original model to avoid changing routes.
const ProductModel = {
  getAll: async (callback) => {
    try {
      const [rows] = await pool.query("SELECT id, name, price FROM products");
      callback(null, rows);
    } catch (err) {
      callback(err);
    }
  },

  create: async (product, callback) => {
    try {
      const { name, price } = product;
      await pool.query("INSERT INTO products (name, price) VALUES (?, ?)", [
        name,
        price,
      ]);
      callback(null);
    } catch (err) {
      callback(err);
    }
  },

  update: async (id, product, callback) => {
    try {
      const { name, price } = product;
      await pool.query("UPDATE products SET name = ?, price = ? WHERE id = ?", [
        name,
        price,
        id,
      ]);
      callback(null);
    } catch (err) {
      callback(err);
    }
  },

  delete: async (id, callback) => {
    try {
      await pool.query("DELETE FROM products WHERE id = ?", [id]);
      callback(null);
    } catch (err) {
      callback(err);
    }
  },
};

module.exports = ProductModel;
