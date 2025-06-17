package org.example.controle_vendas.dao;

import org.example.controle_vendas.model.ItemVenda;
import org.example.controle_vendas.model.Venda;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VendaDAO {
    private final Connection connection;

    public VendaDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Insere uma nova venda na base de dados e retorna o ID gerado.
     */
    public int inserirVenda(Venda venda) throws SQLException {
        String sql = "INSERT INTO venda (cliente_id, funcionario_id, data, valor_total, status) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, venda.getClienteId());
            stmt.setInt(2, venda.getFuncionarioId());
            stmt.setTimestamp(3, Timestamp.valueOf(venda.getData()));
            stmt.setDouble(4, venda.getValorTotal());
            stmt.setString(5, venda.getStatus());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int vendaId = rs.getInt(1);
                    venda.setVendaId(vendaId);
                    return vendaId;
                }
            }
        }

        throw new SQLException("Erro ao inserir venda.");
    }

    /**
     * Insere um item de venda vinculado a uma venda existente.
     */
    public void inserirItemVenda(ItemVenda item) throws SQLException {
        String sql = "INSERT INTO item_venda (venda_id, produto_id, quantidade_vendida, preco_unitario_vendido, subtotal_item) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, item.getVendaId());
            stmt.setInt(2, item.getProdutoId());
            stmt.setInt(3, item.getQuantidadeVendida());
            stmt.setDouble(4, item.getPrecoUnitarioVendido());
            stmt.setDouble(5, item.getSubtotalItem());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setItemVendaId(rs.getInt(1));
                }
            }
        }
    }

    /**
     * Retorna todas as vendas registradas no sistema.
     */
    public List<Venda> listarVendas() throws SQLException {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT * FROM venda";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Venda venda = new Venda();
                venda.setVendaId(rs.getInt("venda_id"));
                venda.setClienteId(rs.getInt("cliente_id"));
                venda.setFuncionarioId(rs.getInt("funcionario_id"));
                venda.setData(rs.getTimestamp("data").toLocalDateTime());
                venda.setValorTotal(rs.getDouble("valor_total"));
                venda.setStatus(rs.getString("status"));

                // Carrega os itens da venda
                venda.setItens(listarItensPorVenda(venda.getVendaId()));

                vendas.add(venda);
            }
        }

        return vendas;
    }

    /**
     * Lista todos os itens de uma venda específica.
     */
    public List<ItemVenda> listarItensPorVenda(int vendaId) throws SQLException {
        List<ItemVenda> itens = new ArrayList<>();
        String sql = "SELECT * FROM item_venda WHERE venda_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, vendaId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItemVenda item = new ItemVenda();
                    item.setItemVendaId(rs.getInt("item_venda_id"));
                    item.setVendaId(rs.getInt("venda_id"));
                    item.setProdutoId(rs.getInt("produto_id"));
                    item.setQuantidadeVendida(rs.getInt("quantidade_vendida"));
                    item.setPrecoUnitarioVendido(rs.getDouble("preco_unitario_vendido"));
                    item.setSubtotalItem(rs.getDouble("subtotal_item"));
                    itens.add(item);
                }
            }
        }

        return itens;
    }

    /**
     * Atualiza o status de uma venda (ex: Concluída, Cancelada).
     */
    public void atualizarStatusVenda(int vendaId, String status) throws SQLException {
        String sql = "UPDATE venda SET status = ? WHERE venda_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, vendaId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Nenhuma venda encontrada com o ID: " + vendaId);
            }
        }
    }

    /**
     * Retorna a lista de vendas realizadas em um dia específico.
     */
    public List<Venda> buscarVendasPorData(LocalDate data) throws SQLException {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT * FROM venda WHERE DATE(data) = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(data));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Venda venda = new Venda();
                    venda.setVendaId(rs.getInt("venda_id"));
                    venda.setClienteId(rs.getInt("cliente_id"));
                    venda.setFuncionarioId(rs.getInt("funcionario_id"));
                    venda.setData(rs.getTimestamp("data").toLocalDateTime());
                    venda.setValorTotal(rs.getDouble("valor_total"));
                    venda.setStatus(rs.getString("status"));
                    venda.setItens(listarItensPorVenda(venda.getVendaId()));

                    vendas.add(venda);
                }
            }
        }

        return vendas;
    }
}
