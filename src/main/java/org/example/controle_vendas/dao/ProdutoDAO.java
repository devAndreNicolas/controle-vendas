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
        // Assume que a coluna 'status' no DB tem DEFAULT 'Ativo'
        String sql = "INSERT INTO Produto (categoria_id, nome_produto, preco_venda, preco_custo, unidade_medida) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, produto.getCategoriaId());
            stmt.setString(2, produto.getNomeProduto());
            stmt.setDouble(3, produto.getPrecoVenda()); // setDouble para preços
            stmt.setDouble(4, produto.getPrecoCusto()); // setDouble para preços
            stmt.setString(5, produto.getUnidadeMedida());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setProdutoId(rs.getInt(1));
                }
            }
        }
    }

    // MODIFICADO: Lista apenas produtos com status 'Ativo'
    public List<Produto> listarTodosAtivos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM Produto WHERE status = 'Ativo'"; // Filtra por status
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Produto p = new Produto();
                p.setProdutoId(rs.getInt("produto_id"));
                p.setCategoriaId(rs.getInt("categoria_id"));
                p.setNomeProduto(rs.getString("nome_produto"));
                p.setPrecoVenda(rs.getDouble("preco_venda")); // getDouble para preços
                p.setPrecoCusto(rs.getDouble("preco_custo")); // getDouble para preços
                p.setUnidadeMedida(rs.getString("unidade_medida"));
                produtos.add(p);
            }
        }
        return produtos;
    }

    public List<Produto> listarTodos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM Produto"; // Filtra por status
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Produto p = new Produto();
                p.setProdutoId(rs.getInt("produto_id"));
                p.setCategoriaId(rs.getInt("categoria_id"));
                p.setNomeProduto(rs.getString("nome_produto"));
                p.setPrecoVenda(rs.getDouble("preco_venda")); // getDouble para preços
                p.setPrecoCusto(rs.getDouble("preco_custo")); // getDouble para preços
                p.setUnidadeMedida(rs.getString("unidade_medida"));
                produtos.add(p);
            }
        }
        return produtos;
    }

    // NOVO: Buscar Produto por ID
    public Produto buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Produto WHERE produto_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produto produto = new Produto();
                    produto.setProdutoId(rs.getInt("produto_id"));
                    produto.setCategoriaId(rs.getInt("categoria_id"));
                    produto.setNomeProduto(rs.getString("nome_produto"));
                    produto.setPrecoVenda(rs.getDouble("preco_venda"));
                    produto.setPrecoCusto(rs.getDouble("preco_custo"));
                    produto.setUnidadeMedida(rs.getString("unidade_medida"));
                    return produto;
                }
            }
        }
        return null;
    }

    // NOVO: Atualizar Produto
    public void atualizar(Produto produto) throws SQLException {
        String sql = "UPDATE Produto SET categoria_id = ?, nome_produto = ?, preco_venda = ?, preco_custo = ?, unidade_medida = ? WHERE produto_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, produto.getCategoriaId());
            stmt.setString(2, produto.getNomeProduto());
            stmt.setDouble(3, produto.getPrecoVenda());
            stmt.setDouble(4, produto.getPrecoCusto());
            stmt.setString(5, produto.getUnidadeMedida());
            stmt.setInt(6, produto.getProdutoId()); // ID para o WHERE
            stmt.executeUpdate();
        }
    }

    // NOVO: Inativar Produto (Soft Delete)
    public void inativar(int id) throws SQLException {
        String sql = "UPDATE Produto SET status = 'Inativo' WHERE produto_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Produto buscarProdutoMaisVendido() throws SQLException {
        String sql = "SELECT p.produto_id, p.nome_produto, SUM(iv.quantidade_vendida) AS total_vendido " +
                "FROM produto p " +
                "JOIN item_venda iv ON p.produto_id = iv.produto_id " +
                "GROUP BY p.produto_id, p.nome_produto " +
                "ORDER BY total_vendido DESC " +
                "LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                Produto produto = new Produto();
                produto.setNomeProduto(rs.getString("nome_produto"));
                return produto;
            }
            return null;
        }
    }
}