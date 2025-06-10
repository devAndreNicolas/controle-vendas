package controle_vendas_test.dao_test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseDAOTest {
    protected Connection connection;

    @BeforeEach
    public void setup() throws SQLException {
        // Conexão com banco H2 em memória
        connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        criarEsquema();
    }

    @AfterEach
    public void teardown() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP ALL OBJECTS");
        }
        connection.close();
    }

    private void criarEsquema() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Crie as tabelas necessárias para os testes (exemplo simplificado)
            stmt.execute("CREATE TABLE Categoria (categoria_id INT AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(255), descricao VARCHAR(255), status VARCHAR(20) DEFAULT 'Ativo')");
            stmt.execute("CREATE TABLE Cliente (cliente_id INT AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(255), cpf_cnpj VARCHAR(20), email VARCHAR(255), telefone VARCHAR(20), endereco VARCHAR(500), status VARCHAR(20) DEFAULT 'Ativo')");
            stmt.execute("CREATE TABLE Funcionario (funcionario_id INT AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(255), cpf VARCHAR(20), status VARCHAR(20) DEFAULT 'Ativo')");
            stmt.execute("CREATE TABLE Produto (produto_id INT AUTO_INCREMENT PRIMARY KEY, categoria_id INT, nome_produto VARCHAR(255), preco_venda DOUBLE, preco_custo DOUBLE, unidade_medida VARCHAR(10), status VARCHAR(20) DEFAULT 'Ativo')");
            stmt.execute("CREATE TABLE Venda (venda_id INT AUTO_INCREMENT PRIMARY KEY, cliente_id INT, funcionario_id INT, data TIMESTAMP DEFAULT CURRENT_TIMESTAMP, valor_total DOUBLE, status VARCHAR(50) DEFAULT 'Em Aberto')");
            stmt.execute("CREATE TABLE Item_venda (item_venda_id INT AUTO_INCREMENT PRIMARY KEY, venda_id INT, produto_id INT, quantidade_vendida INT, preco_unitario_vendido DOUBLE, subtotal_item DOUBLE)");

            // Chaves estrangeiras podem ser adicionadas se quiser
        }
    }
}
