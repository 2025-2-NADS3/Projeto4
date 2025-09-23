const express = require("express");
const router = express.Router();
const ProductModel = require("../models/productModel");

// Listar todos os produtos
router.get("/", (req, res) => {
  ProductModel.getAll((err, rows) => {
    if (err) return res.status(500).json({ error: err.message });
    res.json(rows);
  });
});

// Criar um novo produto
router.post("/", (req, res) => {
  ProductModel.create(req.body, (err) => {
    if (err) return res.status(500).json({ error: err.message });
    res.status(201).json({ message: "Produto criado com sucesso!" });
  });
});

// Atualizar um produto
router.put("/:id", (req, res) => {
  ProductModel.update(req.params.id, req.body, (err) => {
    if (err) return res.status(500).json({ error: err.message });
    res.json({ message: "Produto atualizado com sucesso!" });
  });
});

// Deletar um produto
router.delete("/:id", (req, res) => {
  ProductModel.delete(req.params.id, (err) => {
    if (err) return res.status(500).json({ error: err.message });
    res.json({ message: "Produto deletado com sucesso!" });
  });
});

module.exports = router;
