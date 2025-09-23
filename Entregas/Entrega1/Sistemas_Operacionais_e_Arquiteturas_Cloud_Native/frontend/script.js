const apiUrl = "http://localhost:3000/products";

// Listar produtos
async function fetchProducts() {
  const response = await fetch(apiUrl);
  const products = await response.json();
  const productList = document.getElementById("productList");
  productList.innerHTML = "";

  products.forEach((product) => {
    const li = document.createElement("li");
    li.textContent = `${product.name} - R$${product.price}`;
    li.innerHTML += ` <button onclick="deleteProduct(${product.id})">Excluir</button>`;
    productList.appendChild(li);
  });
}

// Adicionar produto
document.getElementById("productForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const name = document.getElementById("name").value;
  const price = document.getElementById("price").value;

  await fetch(apiUrl, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ name, price }),
  });

  document.getElementById("name").value = "";
  document.getElementById("price").value = "";
  fetchProducts();
});

// Excluir produto
async function deleteProduct(id) {
  await fetch(`${apiUrl}/${id}`, { method: "DELETE" });
  fetchProducts();
}

// Inicializar
fetchProducts();
