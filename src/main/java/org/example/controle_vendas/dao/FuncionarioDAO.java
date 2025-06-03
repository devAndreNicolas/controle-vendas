package org.example.controle_vendas.dao;

import org.example.controle_vendas.model.Funcionario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {
    private final Connection connection;

    public FuncionarioDAO(Connection connection) {
        this.connection = connection;
    }

    public void inserir(Funcionario funcionario) throws SQLException {
        // Assume que a coluna 'status' no DB tem DEFAULT 'Ativo'
        String sql = "INSERT INTO Funcionario (nome, cpf) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCpf());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    funcionario.setFuncionarioId(rs.getInt(1));
                }
            }
        }
    }

    // MODIFICADO: Lista apenas funcionários com status 'Ativo'
    public List<Funcionario> listarTodos() throws SQLException {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT * FROM Funcionario WHERE status = 'Ativo'";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Funcionario f = new Funcionario();
                f.setFuncionarioId(rs.getInt("funcionario_id"));
                f.setNome(rs.getString("nome"));
                f.setCpf(rs.getString("cpf"));
                funcionarios.add(f);
            }
        }
        return funcionarios;
    }

    // NOVO: Buscar Funcionário por ID
    public Funcionario buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Funcionario WHERE funcionario_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Funcionario funcionario = new Funcionario();
                    funcionario.setFuncionarioId(rs.getInt("funcionario_id"));
                    funcionario.setNome(rs.getString("nome"));
                    funcionario.setCpf(rs.getString("cpf"));
                    return funcionario;
                }
            }
        }
        return null;
    }

    // NOVO: Atualizar Funcionário
    public void atualizar(Funcionario funcionario) throws SQLException {
        String sql = "UPDATE Funcionario SET nome = ?, cpf = ? WHERE funcionario_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCpf());
            stmt.setInt(3, funcionario.getFuncionarioId());
            stmt.executeUpdate();
        }
    }

    // NOVO: Inativar Funcionário (Soft Delete)
    public void inativar(int id) throws SQLException {
        String sql = "UPDATE Funcionario SET status = 'Inativo' WHERE funcionario_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}