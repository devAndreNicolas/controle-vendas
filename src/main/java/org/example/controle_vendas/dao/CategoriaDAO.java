package org.example.controle_vendas.dao;

import org.example.controle_vendas.model.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    private final Connection connection;

    public CategoriaDAO(Connection connection) {
        this.connection = connection;
    }

    public void inserir(Categoria categoria) throws SQLException {
        // A coluna 'status' terá o valor padrão 'Ativo' se definida no banco.
        // Se não houver padrão no DB, você pode adicioná-la aqui:
        // String sql = "INSERT INTO Categoria (nome, descricao, status) VALUES (?, ?, 'Ativo')";
        String sql = "INSERT INTO Categoria (nome, descricao) VALUES (?, ?)"; // Assume DEFAULT 'Ativo' no DB
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getDescricao());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    categoria.setCategoriaId(rs.getInt(1));
                    // categoria.setStatus("Ativo"); // Opcional: Se quiser que o objeto reflita o default do DB
                }
            }
        }
    }

    public List<Categoria> listarTodos() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        // AGORA, LISTA APENAS AS CATEGORIAS ATIVAS
        String sql = "SELECT * FROM Categoria WHERE status = 'Ativo'";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Categoria c = new Categoria();
                c.setCategoriaId(rs.getInt("categoria_id"));
                c.setNome(rs.getString("nome"));
                c.setDescricao(rs.getString("descricao"));
                // c.setStatus(rs.getString("status")); // Se você precisar carregar o status também
                categorias.add(c);
            }
        }
        return categorias;
    }

    public Categoria buscarPorId(int id) throws SQLException {
        // Você pode decidir se quer buscar categorias inativas por ID ou não.
        // Para a UI atual, buscará qualquer uma para edição se for o caso.
        String sql = "SELECT * FROM Categoria WHERE categoria_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Categoria categoria = new Categoria();
                    categoria.setCategoriaId(rs.getInt("categoria_id"));
                    categoria.setNome(rs.getString("nome"));
                    categoria.setDescricao(rs.getString("descricao"));
                    // categoria.setStatus(rs.getString("status")); // Carregar o status também
                    return categoria;
                }
            }
        }
        return null;
    }

    public void atualizar(Categoria categoria) throws SQLException {
        // O status também pode ser atualizado aqui, se você tiver uma UI para isso.
        // Por enquanto, apenas nome e descrição.
        String sql = "UPDATE Categoria SET nome = ?, descricao = ? WHERE categoria_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getDescricao());
            stmt.setInt(3, categoria.getCategoriaId());
            stmt.executeUpdate();
        }
    }

    // MÉTODO ALTERADO: Agora inativa a categoria em vez de excluí-la fisicamente
    public void inativar(int id) throws SQLException {
        String sql = "UPDATE Categoria SET status = 'Inativo' WHERE categoria_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}