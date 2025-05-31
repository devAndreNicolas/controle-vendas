package org.example.controle_vendas.dao;
import org.example.controle_vendas.model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private final Connection connection;

    public ProdutoDAO(Connection connection) {
        this.connection = connection;
    }

    public void inserir(Produto produto) throws SQLException {
        String sql = "INSERT INTO Produto (categoria_id, nome_produto, preco_venda, preco_custo, unidade_medida) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, produto.getCategoriaId());
            stmt.setString(2, produto.getNomeProduto());
            stmt.setDouble(3, produto.getPrecoVenda());
            stmt.setDouble(4, produto.getPrecoCusto());
            stmt.setString(5, produto.getUnidadeMedida());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setProdutoId(rs.getInt(1));
                }
            }
        }
    }

    public List<Produto> listarTodos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM Produto";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Produto p = new Produto();
                p.setProdutoId(rs.getInt("produto_id"));
                p.setCategoriaId(rs.getInt("categoria_id"));
                p.setNomeProduto(rs.getString("nome_produto"));
                p.setPrecoVenda(rs.getInt("preco_venda"));
                p.setPrecoCusto(rs.getInt("preco_custo"));
                p.setUnidadeMedida(rs.getString("unidade_medida"));
                produtos.add(p);
            }
        }
        return produtos;
    }
}