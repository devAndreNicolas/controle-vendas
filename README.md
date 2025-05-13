# Projeto Vendas - Configuração Completa com MySQL e JUnit

Este README guia você passo a passo para configurar o ambiente MySQL, criar o banco de dados com as tabelas da modelagem, criar um usuário para a aplicação, configurar um projeto Java Maven com JUnit e testar a conexão com o banco.

---

## 1. Instalar o MySQL no seu Computador

- Baixe e instale o MySQL Server a partir do site oficial:
  [https://dev.mysql.com/downloads/mysql/](https://dev.mysql.com/downloads/mysql/)

- Durante a instalação, defina uma senha para o usuário `root` e guarde essa senha com segurança.

- Opcionalmente, instale o **MySQL Workbench** para facilitar a administração gráfica do banco.

---

## 2. Configurar o MySQL
    
- Abra o terminal ou prompt de comando.

- Acesse o MySQL como root:

  ```bash
  mysql -u root -p


Digite a senha do root quando solicitado.

- Crie o banco de dados `vendasdb`:

  ```sql
  CREATE DATABASE vendasdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
  ```

- Crie um usuário para a aplicação (substitua `usuario_app` e `senhaSegura123` pelos seus valores):

  ```sql
  CREATE USER 'usuario_app'@'localhost' IDENTIFIED BY 'senhaSegura123';
  GRANT ALL PRIVILEGES ON vendasdb.* TO 'usuario_app'@'localhost';
  FLUSH PRIVILEGES;
  ```

- Saia do MySQL:

  ```sql
  EXIT;
  ```

-----

## 3\. Criar as Tabelas no Banco de Dados

- Acesse o MySQL com seu usuário criado, ou continue como root:

  ```bash
  mysql -u root -p vendasdb
  ```

- Execute o script abaixo para criar as tabelas:

  ```sql
  USE vendasdb;

  CREATE TABLE Cliente (
      cliente_id INT AUTO_INCREMENT PRIMARY KEY,
      nome VARCHAR(255) NOT NULL,
      cpf_cnpj VARCHAR(20) UNIQUE NOT NULL,
      email VARCHAR(255),
      telefone VARCHAR(20),
      endereco TEXT
  ); etc...
  ```

  Execute este script no MySQL Workbench ou terminal para criar todas as tabelas.

-----

## 4\. Criar o Projeto Maven Java

- Abra sua IDE (ex: IntelliJ IDEA) e crie um novo projeto Maven.

- No arquivo `pom.xml`, adicione as dependências necessárias:

  ```xml
  <dependencies>
      <dependency>
          <groupId>org.junit.jupiter</groupId>
          <artifactId>junit-jupiter</artifactId>
          <version>5.9.3</version>
          <scope>test</scope>
      </dependency>

      <dependency>
          <groupId>mysql</groupId>
          <artifactId>mysql-connector-java</artifactId>
          <version>8.0.33</version>
      </dependency>
  </dependencies>
  ```

- Atualize o Maven para baixar as dependências.

-----

## 5\. Criar Teste JUnit para Validar Conexão com o Banco

- Crie o arquivo `MySQLConnectionTest.java` em `src/test/java/com/suaempresa/vendas/` com o código:

  ```java
  package com.suaempresa.vendas;

  import org.junit.jupiter.api.Test;

  import java.sql.Connection;
  import java.sql.DriverManager;
  import java.sql.SQLException;

  import static org.junit.jupiter.api.Assertions.*;

  public class MySQLConnectionTest {

      private static final String URL = "jdbc:mysql://localhost:3306/vendasdb?useSSL=false&serverTimezone=UTC";
      private static final String USER = "usuario_app"; // seu usuário criado no MySQL
      private static final String PASSWORD = "senhaSegura123"; // sua senha

      @Test
      public void testConnection() {
          try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
              assertNotNull(conn, "A conexão deve ser criada com sucesso");
              System.out.println("Conexão com MySQL estabelecida com sucesso!");
          } catch (SQLException e) {
              fail("Falha ao conectar com o banco: " + e.getMessage());
          }
      }
  }
  ```

- Altere `usuario_app` e `senhaSegura123` para os valores que você criou no banco.

-----

## 6\. Rodar o Teste

- Na sua IDE, clique com o botão direito no arquivo `MySQLConnectionTest.java` e execute o teste.

- Ou no terminal, dentro do diretório do projeto, execute:

  ```bash
  mvn test
  ```

- Se o teste passar, você está conectado corretamente ao banco\!

-----

## Extras

* Para inserir dados de teste, crie scripts SQL INSERT ou faça testes Java que façam inserções via JDBC.
* Use o MySQL Workbench para visualizar e gerenciar seu banco de forma gráfica.
* Lembre-se de manter suas senhas seguras e não expô-las em códigos públicos.

-----

## Fluxo Resumido

```plaintext
[Instalar MySQL] -> [Criar Banco e Usuário] -> [Criar Tabelas com Script] -> [Criar Projeto Maven] -> [Configurar Dependências] -> [Criar Teste de Conexão] -> [Rodar Teste]
```