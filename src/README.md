# Relatório de Arquitetura e Bibliotecas do Projeto

## 1. Visão Geral do Projeto

Este aplicativo Android foi desenvolvido como parte do projeto de Programação Mobile. Suas principais funcionalidades são:
1.  Consumir dados de uma API pública (`dummyjson.com`) para buscar uma lista de produtos.
2.  Exibir os produtos em uma lista rolável e interativa.
3.  Permitir que o usuário salve produtos de interesse em um banco de dados local (SQLite).
4.  Visualizar a lista de produtos salvos localmente, com a opção de apagar os dados.
5.  Apresentar análises estatísticas sobre os dados da API em forma de gráficos.

Para construir essas funcionalidades de forma robusta e eficiente, foram utilizadas bibliotecas e componentes específicos, cuja escolha é justificada a seguir.

---

## 2. Justificativa das Bibliotecas e Componentes Utilizados

### 2.1. Componentes de Interface de Usuário (UI)

#### `androidx.appcompat:appcompat`
*   **O que é?** Biblioteca de compatibilidade do Android.
*   **Por que foi usada?** Garante que o aplicativo tenha uma aparência consistente e que funcionalidades modernas, como a `ToolBar` (barra de título), funcionem corretamente em versões mais antigas do sistema Android. É um pilar fundamental para a retrocompatibilidade.

#### `androidx.constraintlayout:constraintlayout`
*   **O que é?** Um gerenciador de layout avançado.
*   **Por que foi usada?** Foi escolhido como o layout principal para todas as telas para criar interfaces de usuário flexíveis e responsivas. Ele permite posicionar elementos com base em suas relações uns com os outros e com a tela, evitando a necessidade de aninhar múltiplos layouts (como `LinearLayout` e `RelativeLayout`). Isso resulta em um melhor desempenho de renderização e um código de layout mais limpo e fácil de manter.

#### `androidx.recyclerview:recyclerview` e `androidx.cardview:cardview`
*   **O que são?** Componentes para exibir listas e cartões.
*   **Por que foram usados?**
    *   O `RecyclerView` é essencial para exibir listas de dados, como a de produtos. Ele é extremamente eficiente, pois recicla as visualizações dos itens que saem da tela, economizando memória e garantindo uma rolagem suave, mesmo com centenas de itens.
    *   O `CardView` foi usado para encapsular cada item da lista em um "cartão" visual, com elevação (sombra) e cantos arredondados. Isso cria uma separação visual clara entre os itens e segue as diretrizes de design moderno do Material Design.

#### `Toast` (Componente nativo do Android)
*   **O que é?** Uma pequena mensagem pop-up que aparece na tela por um curto período.
*   **Por que foi usado?** Foi a escolha para fornecer feedback simples e não intrusivo ao usuário. Por exemplo, ao salvar um produto ("Produto salvo!") ou apagar os dados ("Dados apagados!"). Ele informa ao usuário que a ação foi concluída com sucesso sem interromper o fluxo de navegação ou exigir uma interação adicional, sendo a ferramenta ideal para confirmações rápidas.

### 2.2. Comunicação com a Rede e Manipulação de Dados

#### `com.android.volley:volley`
*   **O que é?** Uma biblioteca de rede para fazer requisições HTTP.
*   **Por que foi usada?** Foi utilizada para se comunicar com a API `dummyjson.com`. A Volley simplifica o processo de requisição de dados da internet, gerenciando automaticamente a criação de threads de fundo (para não travar a interface do usuário), o enfileiramento de requisições e o tratamento de cache.

#### `com.google.code.gson:gson`
*   **O que é?** Uma biblioteca para converter objetos Java em sua representação JSON e vice-versa.
*   **Por que foi usada?** A resposta da API vem em formato JSON, um texto estruturado. O GSON automatiza a tarefa de "parsear" (analisar) esse texto e transformá-lo diretamente em uma lista de objetos `Produto` (nossa classe de modelo). Isso elimina a necessidade de código manual para extrair cada campo do JSON, tornando o processo mais rápido, seguro e menos propenso a erros.

### 2.3. Persistência de Dados (Banco de Dados)

#### `androidx.room:room-runtime`
*   **O que é?** Uma biblioteca de persistência que fornece uma camada de abstração sobre o SQLite. É a forma recomendada pelo Google para trabalhar com bancos de dados locais.
*   **Por que foi usada?** O Room foi escolhido para gerenciar o banco de dados SQLite onde os produtos são salvos. As principais vantagens sobre o uso direto do SQLite são:
    1.  **Verificação de SQL em tempo de compilação:** O Room valida as queries SQL durante a compilação do projeto, evitando erros que só apareceriam com o aplicativo em execução.
    2.  **Redução de código repetitivo (Boilerplate):** Ele converte automaticamente os resultados das queries em objetos Java (`Produto`), eliminando o código manual e repetitivo necessário para ler os dados de um `Cursor`.
    3.  **Facilidade de uso:** Define-se a estrutura do banco com anotações simples (`@Entity`, `@Dao`), tornando o código mais legível e organizado.

### 2.4. Análise e Visualização de Dados

#### `com.github.PhilJay:MPAndroidChart`
*   **O que é?** Uma biblioteca de terceiros para a criação de gráficos.
*   **Por que foi usada?** Para atender ao requisito de apresentar cálculos estatísticos de forma visual. Esta biblioteca é extremamente poderosa e flexível, permitindo criar diversos tipos de gráficos. No projeto, ela foi usada para gerar um **Gráfico de Pizza** (distribuição de produtos por marca) e um **Gráfico de Barras** (comparação de preço médio por marca), transformando dados brutos em insights visuais de fácil compreensão.

### 2.5. Operações em Segundo Plano

#### `AsyncTask` (Componente nativo do Android)
*   **O que é?** Uma classe que permite executar operações em segundo plano e publicar os resultados na thread de UI.
*   **Por que foi usada?** Operações de banco de dados e de rede não podem ser executadas na thread principal, pois isso travaria a interface do usuário. O `AsyncTask` foi usado para realizar essas operações (como salvar ou ler do banco de dados) em uma thread separada, garantindo que o aplicativo permaneça responsivo. Embora existam alternativas mais modernas (como Coroutines em Kotlin), o `AsyncTask` é uma solução direta e conceitualmente simples para problemas de concorrência em aplicativos Java.
