# FECAP - Fundação de Comércio Álvares Penteado

<p align="center">
<a href= "https://www.fecap.br/"><img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRhZPrRa89Kma0ZZogxm0pi-tCn_TLKeHGVxywp-LXAFGR3B1DPouAJYHgKZGV0XTEf4AE&usqp=CAU" alt="FECAP - Fundação de Comércio Álvares Penteado" border="0"></a>
</p>

# Comedoria da Tia – Aplicativo Mobile

## FourWare

## Integrantes: <a href="https://www.linkedin.com/in/renan-damprelli/">Renan Damprelli</a>, <a href="https://www.linkedin.com/in/jo%C3%A3o-pedro-gon%C3%A7alves-holanda/">João Pedro Gonçalves</a>, <a>Saulo Ribeiro</a>, <a>Cassio Gonçalves</a>
## Professor Orientador: <a href="https://www.linkedin.com/in/victorbarq/?originalSubdomain=br">Dr. Victor Rosetti de Quiroz</a>


## 1. Apresentação do Projeto

A "Comendaria da Tia" é a cantina da FECAP, localizada no 1º Andar do Bloco C, responsável por atender os alunos nos intervalos de aula e no horário de almoço. Atualmente, os processos de atendimento enfrentam desafios relacionados ao tempo limitado para a realização de pedidos, sobretudo devido às filas no caixa, que comprometem o tempo dos estudantes para suas refeições.

Diante desse cenário, propõe-se o desenvolvimento de um aplicativo mobile com o objetivo de otimizar o processo de compra de produtos da cantina. O aplicativo permitirá aos alunos realizarem seus pedidos e pagamentos antecipadamente, restando apenas a retirada dos produtos no balcão. O sistema também contará com uma interface administrativa para a cantina gerenciar o cardápio, os pedidos e relatórios operacionais.

## 2. Objetivos

### Objetivo Geral:
Desenvolver um aplicativo mobile (inicialmente para Android, podendo ser multiplataforma) que permita aos alunos da FECAP realizar pedidos e pagamentos de forma prática e antecipada na cantina "Comendaria da Tia", contribuindo para a melhoria da experiência de consumo e da gestão operacional da cantina.

### Objetivos Específicos:
*   Eliminar a necessidade de enfrentar filas para pagamento na cantina.
*   Permitir à cantina gerenciar dinamicamente seu cardápio e pedidos.
*   Oferecer histórico de pedidos aos usuários.
*   Realizar simulação ou integração real com APIs de pagamento.
*   Desenvolver interfaces intuitivas e responsivas voltadas à experiência do usuário.
*   Aplicar conceitos de testes e qualidade de software para validar funcionalidades.

## 3. Requisitos Funcionais

### Acesso Aluno (Cliente):
*   Auto cadastro e login de aluno.
*   Visualização do cardápio atualizado.
*   Realização de pedidos e escolha de itens.
*   Pagamento via API (Stripe, Mercado Pago, PagSeguro) ou simulado.
*   Visualização do histórico de pedidos realizados.

### Acesso Cantina (Empresa):
*   Login administrativo.
*   Cadastro e atualização do cardápio.
*   Visualização de pedidos pendentes e confirmação de retirada.
*   Baixa de pedidos (pedido entregue).
*   Relatórios gerenciais.

## 4. Requisitos Não Funcionais
*   Interface intuitiva e responsiva (UX/UI).
*   Aplicação mobile compatível com Android (e preferencialmente iOS).
*   Armazenamento em nuvem.
*   Arquitetura orientada a objetos e/ou baseada em componentes reutilizáveis.
*   Disponibilidade mínima offline para visualização do cardápio.
*   Segurança no armazenamento de dados sensíveis.
*   Código modular e testável.

## 5. Tecnologias Utilizadas (Planejado)
*   **Plataforma Mobile:** Android (inicialmente)
*   **Linguagem:** Java (conforme o projeto atual) / Kotlin
*   **Banco de Dados:** (A definir - ex: Firebase Firestore, SQLite com sincronização)
*   **API de Pagamento:** (A definir/simular - ex: Stripe, Mercado Pago)
*   **Arquitetura:** (A definir - ex: MVVM, MVC, MVI)

## 6. Status do Projeto
Em desenvolvimento.

---

## Tarefa Inicial / Exercício: App de Informações de Cidades

Como parte do desenvolvimento e aprendizado, uma aplicação Android com as seguintes características será desenvolvida:

*   **Função Principal:** Apresentar informações de cidades.
    *   Tela inicial com lista de cidades.
    *   Tela de detalhes de uma cidade.
*   **Funcionalidades e Componentes Requeridos:**
    *   Múltiplas Activities.
    *   Intents para navegação.
    *   Fragments.
    *   Uso de ConstraintLayout.
    *   Componentes visuais: TextView, ImageView, Button.
    *   Integração e exibição de dados numéricos (ex.: população, densidade).
    *   A funcionalidade principal deve estar completamente implementada e operacional.
