Comedoria da Tia — Backend e Banco em Contêiner

Este repositório contém um backend Node.js (API simples de produtos) e configuração para rodar um banco MySQL em contêiner via Docker Compose. A entrega pede que o banco esteja em contêiner e que o backend realize operações CRUD.

Resumo rápido
- Backend: `backend/server.js` (Express)
- Modelo MySQL: `backend/models/mysqlProductModel.js`
- Pool/Init: `backend/database.js` (usa `mysql2/promise` e faz retry/backoff)
- Compose: `docker-compose.yml` — cria `mysql` e `backend`.
- Script de inicialização do MySQL: `mysql-init/init.sql` (cria DB `comedoria` e insere dados de exemplo)

Pré-requisitos
- Docker Desktop (Windows) instalado e em execução
- Powershell (os comandos abaixo usam PowerShell)

Rodando com Docker Compose (recomendado)
1. Abra o PowerShell e vá para a raiz do projeto (onde está `docker-compose.yml`):

```powershell
cd 'D:\Estudos\PIADS3\Projeto4\documentos\Entrega2\Sistemas_Operacionais_e_Arquiteturas_Cloud_Native'
```

2. Subir o stack (modo interativo, útil para ver logs):

```powershell
docker compose up --build
```

3. Ou em background (detached):

```powershell
docker compose up --build -d
docker compose logs -f backend
```

Observações importantes sobre portas e acesso
- O backend (dentro do container) expõe a porta 3000. No `docker-compose.yml` mapearam para a porta 3001 no host: `3001:3000`. Ou seja, acesse a API em `http://localhost:3001`.
- Se preferir usar `localhost:3000`, pare qualquer processo local que esteja usando a porta 3000 (por exemplo, `node server.js`) ou altere o `docker-compose.yml` para mapear `"3000:3000"`.

Testes rápidos da API (PowerShell)
- Listar produtos (GET):

```powershell
Invoke-RestMethod -Method Get -Uri http://localhost:3001/products
```

- Criar produto (POST):

```powershell
$body = @{ name = 'Bolo' ; price = 8.5 } | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri http://localhost:3001/products -ContentType 'application/json' -Body $body
```

- Atualizar (PUT):

```powershell
$body = @{ name = 'Bolo de Chocolate' ; price = 9.5 } | ConvertTo-Json
Invoke-RestMethod -Method Put -Uri http://localhost:3001/products/1 -ContentType 'application/json' -Body $body
```

- Deletar (DELETE):

```powershell
Invoke-RestMethod -Method Delete -Uri http://localhost:3001/products/1
```

Inspecionar dados diretamente (client mysql)
1. Usando cliente `mysql` local (se instalado):

```powershell
mysql -h 127.0.0.1 -P 3307 -u appuser -p
# senha: apppassword
USE comedoria;
SELECT * FROM products;
```

2. Ou entrando no container MySQL:

```powershell
docker compose exec mysql mysql -u appuser -p comedoria
# quando pedir senha, digite: apppassword
```

Credenciais e nomes (atual configuração)
- MySQL root (container): `rootpassword` (usado apenas durante init)
- DB criado: `comedoria` (veja `mysql-init/init.sql`)
- Usuário criado: `appuser` / senha `apppassword` (usado pelo backend)

Notas técnicas e decisions
- O `backend/database.js` foi atualizado para usar um pool `mysql2/promise` e inclui um mecanismo de retry/backoff para aguardar o MySQL ficar pronto (evita `ECONNREFUSED` no startup dos containers).
- Mantive as rotas no mesmo formato callback para compatibilidade com o código existente; o novo modelo `mysqlProductModel.js` usa o pool para CRUD.

Possíveis melhorias (opcionais)
- Renomear o banco para `comedoria` se preferir esse nome textual do enunciado. Posso fazer essa alteração em todos os pontos se quiser (ajustar `mysql-init/init.sql`, `docker-compose.yml` e `backend/.env`).
- Adicionar `healthcheck` no `docker-compose.yml` para o serviço `mysql` (opção útil para orchestration).
- Adicionar testes automatizados básicos (jest / supertest) para validar o CRUD.

Troubleshooting rápido
- Se o backend não iniciar: verifique `docker compose logs -f backend` para erros.
- Se o MySQL não aceitar conexões imediatamente: o `database.js` faz até 15 tentativas (2s cada). Aguarde ~30s ou veja logs do MySQL: `docker compose logs -f mysql`.
- Porta já em uso: verifique o PID com `netstat -ano | Select-String ':3000'` e encerre o processo local com `Stop-Process -Id <PID> -Force` se quiser usar a porta 3000.

Entrega e verificação
- Para a entrega do trabalho (banco em contêiner e integração com backend), você pode enviar:
  - `docker-compose.yml` (incluído)
  - `mysql-init/init.sql` (incluído)
  - `backend` com o código adaptado para MySQL
  - `README.md` com instruções de como executar e testar

Se quiser, eu também posso:
- alterar o nome do banco para `comedoria` (opção C que você considerou antes) — aplico as mudanças e atualizo o README; ou
- adicionar o `healthcheck` no `docker-compose.yml` e/ou criar um README mais extenso com screenshots e output de logs.

---
Se quiser que eu já aplique a mudança do nome do banco para `comedoria`, diga e eu atualizo todos os arquivos (init.sql, docker-compose.yml e `.env`) agora.

FIM
