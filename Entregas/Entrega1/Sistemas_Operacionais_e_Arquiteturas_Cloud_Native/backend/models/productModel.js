const db = require("../database");

class ProductModel {
  static getAll(callback) {
    db.all("SELECT * FROM products", [], callback);
  }

  static create(product, callback) {
    const { name, price } = product;
    db.run(
      "INSERT INTO products (name, price) VALUES (?, ?)",
      [name, price],
      callback
    );
  }

  static update(id, product, callback) {
    const { name, price } = product;
    db.run(
      "UPDATE products SET name = ?, price = ? WHERE id = ?",
      [name, price, id],
      callback
    );
  }

  static delete(id, callback) {
    db.run("DELETE FROM products WHERE id = ?", [id], callback);
  }
}

module.exports = ProductModel;
