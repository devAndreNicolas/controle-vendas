package org.example.controle_vendas.service;

import org.example.controle_vendas.dao.VendaDAO;
import org.example.controle_vendas.model.ItemVenda;
import org.example.controle_vendas.model.Venda;

import java.sql.SQLException;
import java.util.List;

public class VendaService {
    private final VendaDAO vendaDAO;

    public VendaService(VendaDAO vendaDAO) {
        this.vendaDAO = vendaDAO;
    }

    public void cadastrarVenda(Venda venda) throws SQLException {
        if (venda.getClienteId() <= 0) {
            throw new IllegalArgumentException("Cliente inválido");
        }
        if (venda.getFuncionarioId() <= 0) {
            throw new IllegalArgumentException("Funcionário inválido");
        }
        if (venda.getItens() == null || venda.getItens().isEmpty()) {
            throw new IllegalArgumentException("A venda deve ter ao menos um item");
        }

        // Calcula valor total
        double total = 0;
        for (ItemVenda item : venda.getItens()) {
            total += item.getSubtotalItem();
        }
        venda.setValorTotal(total);

        // Insere venda e itens no banco
        int vendaId = vendaDAO.inserirVenda(venda);
        for (ItemVenda item : venda.getItens()) {
            item.setVendaId(vendaId);
            vendaDAO.inserirItemVenda(item);
        }
    }

    public List<Venda> listarVendas() throws SQLException {
        return vendaDAO.listarVendas();
    }
}