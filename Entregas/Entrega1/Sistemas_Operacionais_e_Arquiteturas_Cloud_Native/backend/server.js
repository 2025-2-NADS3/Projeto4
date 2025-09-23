const express = require("express");
const cors = require("cors");
const productRoutes = require("./routes/productRoutes");

const app = express();
app.use(cors());
app.use(express.json());

app.use("/products", productRoutes);

app.listen(3000, () => {
  console.log("Servidor rodando na porta 3000");
});
