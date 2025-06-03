# Sistema de Controle de Vendas

## 1. Visão Geral do Sistema

Este sistema é uma aplicação desktop desenvolvida em **Java**, utilizando **Swing** para a interface gráfica (GUI) e **MySQL** para persistência dos dados. Ele visa oferecer uma solução para o gerenciamento de:

- Clientes (Pessoa Física ou Jurídica)
- Produtos e Categorias
- Funcionários
- Estoque
- Vendas, Itens de Venda, Pagamentos e Notas Fiscais
- Formas de Pagamento

Utiliza **"soft delete"** (inativação lógica) para preservar o histórico dos dados.

---

## 2. Funcionalidades

- **Gestão de Categorias:** CRUD completo com inativação lógica.
- **Gestão de Produtos:** CRUD com validações (nome, preço, associação a categorias/unidades).
- **Gestão de Clientes:** Suporte a PF/PJ, validação de CPF/CNPJ.
- **Gestão de Funcionários:** Cadastro, edição e inativação com validação de CPF.
- **Gestão de Estoque:** Controle de quantidade atual e mínima.
- **Gestão de Formas de Pagamento:** Cadastro e manutenção.
- **Gestão de Vendas:** Registro de vendas completas, com clientes, itens, pagamentos e nota fiscal.
- **Interface Moderna (Swing):**
  - Layouts com `GridBagLayout`
  - Campos formatados com `JFormattedTextField`
  - Textos guia (placeholders) e botões interativos

---

## 3. Arquitetura do Sistema

O projeto segue uma **arquitetura em camadas (layered architecture)**:

org.example.controle_vendas
│
├── dao # Camada de persistência (JDBC)
├── model # Entidades de negócio
├── service # Lógica de negócio e validação
└── ui # Interface gráfica (Swing)


---

## 4. Estrutura de Pacotes

| Pacote      | Conteúdo                                                   |
|-------------|------------------------------------------------------------|
| `dao`       | Acesso a dados via JDBC: `CategoriaDAO`, `ClienteDAO`...  |
| `model`     | Entidades como `Cliente`, `Produto`, `Venda`, etc.        |
| `service`   | Validações e lógica: `ProdutoService`, `VendaService`...  |
| `ui`        | Telas Swing: `TelaPrincipalUI`, `VendaUI`, etc.           |

---

## 5. Componentes Principais

| Componente       | Camada  | Função |
|------------------|---------|--------|
| Categoria         | Model   | Define categorias de produtos. |
| UnidadeMedida     | Model   | Unidade, Litro, Kg, etc. |
| Produto           | Model   | Descrição, preços, categoria, unidade. |
| Estoque           | Model   | Quantidade atual/mínima por produto. |
| Cliente           | Model   | Dados pessoais/jurídicos. |
| Funcionario       | Model   | Nome, CPF, dados internos. |
| FormaPagamento    | Model   | Tipos de pagamento. |
| Venda             | Model   | Transação completa com itens, pagamentos, cliente. |
| ItemVenda         | Model   | Produto dentro de uma venda. |
| NotaFiscal        | Model   | Dados fiscais relacionados à venda. |
| Pagamento         | Model   | Registro de valor pago por forma de pagamento. |

---

## 6. Termos Técnicos Importantes

- **DAO (Data Access Object):** Encapsula operações com banco de dados.
- **JDBC:** Interface Java para SQL.
- **Soft Delete:** Exclusão lógica via status/ativo.
- **UI/UX:** Interface do usuário e experiência do usuário.
- **JFormattedTextField:** Campo formatado (valores monetários, etc).
- **Placeholder:** Texto guia exibido em campos vazios.
- **CRUD:** Create, Read, Update, Delete.

---

## 7. Requisitos e Dependências

### 7.1 Requisitos de Software

- Java JDK 21+
- MySQL 8.0+
- IDE: IntelliJ IDEA, Eclipse ou similar

### 7.2 Dependências via Maven

```xml
<!-- MySQL Driver -->
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <version>8.0.33</version>
</dependency>

<!-- FlatLaf para UI moderna -->
<dependency>
  <groupId>com.formdev</groupId>
  <artifactId>flatlaf</artifactId>
  <version>3.4</version>
</dependency>

<!-- Java Dotenv para variáveis de ambiente -->
<dependency>
  <groupId>io.github.cdimascio</groupId>
  <artifactId>java-dotenv</artifactId>
  <version>5.2.2</version>
</dependency>

<!-- JUnit para testes (escopo test) -->
<dependency>
  <groupId>org.junit.jupiter</groupId>
  <artifactId>junit-jupiter</artifactId>
  <version>5.13.0-M2</version>
  <scope>test</scope>
</dependency>

<!-- Kotlin Stdlib -->
<dependency>
  <groupId>org.jetbrains.kotlin</groupId>
  <artifactId>kotlin-stdlib</artifactId>
  <version>1.9.0</version>
</dependency>
```

| Tabela           | Descrição                    | PK / FK                                                       | Soft Delete |
| ---------------- | ---------------------------- | ------------------------------------------------------------- | ----------- |
| categoria        | Categorias de produtos       | categoria\_id (PK)                                            | `ativo`     |
| cliente          | Dados do cliente (PF/PJ)     | cliente\_id (PK)                                              | `ativo`     |
| funcionario      | Dados dos funcionários       | funcionario\_id (PK)                                          | `ativo`     |
| produto          | Produtos registrados         | produto\_id (PK), categoria\_id (FK)                          | `ativo`     |
| estoque          | Estoque por produto          | estoque\_id (PK), produto\_id (FK)                            | N/A         |
| forma\_pagamento | Métodos de pagamento aceitos | forma\_pagamento\_id (PK)                                     | `ativo`     |
| venda            | Registro de vendas           | venda\_id (PK), cliente\_id (FK), funcionario\_id (FK)        | `status`    |
| item\_venda      | Produtos vinculados à venda  | item\_venda\_id (PK), venda\_id (FK), produto\_id (FK)        | N/A         |
| nota\_fiscal     | Nota fiscal da venda         | nota\_fiscal\_id (PK), venda\_id (FK)                         | N/A         |
| pagamento        | Valores pagos em cada venda  | pagamento\_id (PK), venda\_id (FK), forma\_pagamento\_id (FK) | `status`    |