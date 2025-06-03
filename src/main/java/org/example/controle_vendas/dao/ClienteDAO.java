package org.example.controle_vendas.dao;

import org.example.controle_vendas.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private final Connection connection;

    public ClienteDAO(Connection connection) {
        this.connection = connection;
    }

    public void inserir(Cliente cliente) throws SQLException {
        // Assume que a coluna 'status' no DB tem DEFAULT 'Ativo'
        String sql = "INSERT INTO Cliente (nome, cpf_cnpj, email, telefone, endereco) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpfCnpj());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefone());
            stmt.setString(5, cliente.getEndereco());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setClienteId(rs.getInt(1));
                }
            }
        }
    }

    // MODIFICADO: Lista apenas clientes com status 'Ativo'
    public List<Cliente> listarTodos() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Cliente WHERE status = 'Ativo'";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setClienteId(rs.getInt("cliente_id"));
                c.setNome(rs.getString("nome"));
                c.setCpfCnpj(rs.getString("cpf_cnpj"));
                c.setEmail(rs.getString("email"));
                c.setTelefone(rs.getString("telefone"));
                c.setEndereco(rs.getString("endereco"));
                clientes.add(c);
            }
        }
        return clientes;
    }

    // NOVO: Buscar Cliente por ID
    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Cliente WHERE cliente_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setClienteId(rs.getInt("cliente_id"));
                    cliente.setNome(rs.getString("nome"));
                    cliente.setCpfCnpj(rs.getString("cpf_cnpj"));
                    cliente.setEmail(rs.getString("email"));
                    cliente.setTelefone(rs.getString("telefone"));
                    cliente.setEndereco(rs.getString("endereco"));
                    return cliente;
                }
            }
        }
        return null;
    }

    // NOVO: Atualizar Cliente
    public void atualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE Cliente SET nome = ?, cpf_cnpj = ?, email = ?, telefone = ?, endereco = ? WHERE cliente_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpfCnpj());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefone());
            stmt.setString(5, cliente.getEndereco());
            stmt.setInt(6, cliente.getClienteId());
            stmt.executeUpdate();
        }
    }

    // NOVO: Inativar Cliente (Soft Delete)
    public void inativar(int id) throws SQLException {
        String sql = "UPDATE Cliente SET status = 'Inativo' WHERE cliente_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}