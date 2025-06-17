package org.example.controle_vendas.service;

import org.example.controle_vendas.dao.VendaDAO;
import org.example.controle_vendas.model.ItemVenda;
import org.example.controle_vendas.model.Venda;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class VendaService {

    private final VendaDAO vendaDAO;

    public VendaService(VendaDAO vendaDAO) {
        this.vendaDAO = vendaDAO;
    }


    public void cadastrarVenda(Venda venda) throws SQLException {
        // Validações básicas
        if (venda.getClienteId() <= 0) {
            throw new IllegalArgumentException("Cliente inválido");
        }
        if (venda.getFuncionarioId() <= 0) {
            throw new IllegalArgumentException("Funcionário inválido");
        }
        if (venda.getItens() == null || venda.getItens().isEmpty()) {
            throw new IllegalArgumentException("A venda deve ter ao menos um item");
        }

        // Calcula o valor total da venda somando os subtotais dos itens
        double total = 0;
        for (ItemVenda item : venda.getItens()) {
            total += item.getSubtotalItem();
        }
        venda.setValorTotal(total);

        // Registra a venda e os itens no banco de dados
        int vendaId = vendaDAO.inserirVenda(venda);
        for (ItemVenda item : venda.getItens()) {
            item.setVendaId(vendaId); // Associa o item à venda
            vendaDAO.inserirItemVenda(item);
        }
    }

    /**
     * Retorna a lista de todas as vendas registradas.
     */
    public List<Venda> listarVendas() throws SQLException {
        return vendaDAO.listarVendas();
    }

    /**
     * Atualiza o status de uma venda (ex: "Concluída", "Cancelada").
     */
    public void atualizarStatusVenda(int vendaId, String status) throws SQLException {
        vendaDAO.atualizarStatusVenda(vendaId, status);
    }

    /**
     * Retorna a lista de vendas realizadas em uma data específica.
     */
    public List<Venda> buscarVendasPorData(LocalDate data) throws SQLException {
        return vendaDAO.buscarVendasPorData(data);
    }
}
