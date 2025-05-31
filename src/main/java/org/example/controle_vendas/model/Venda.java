package org.example.controle_vendas.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venda {
    private int vendaId;
    private int clienteId;
    private int funcionarioId;
    private LocalDateTime data;
    private double valorTotal;
    private String status;
    private List<ItemVenda> itens = new ArrayList<>();

    public Venda() {
        this.data = LocalDateTime.now();
        this.status = "ABERTA";
    }

    public Venda(int vendaId, int clienteId, int funcionarioId, LocalDateTime data, double valorTotal, String status) {
        this.vendaId = vendaId;
        this.clienteId = clienteId;
        this.funcionarioId = funcionarioId;
        this.data = data;
        this.valorTotal = valorTotal;
        this.status = status;
    }

    public int getVendaId() {
        return vendaId;
    }

    public void setVendaId(int vendaId) {
        this.vendaId = vendaId;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(int funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }

    public void adicionarItem(ItemVenda item) {
        this.itens.add(item);
        this.valorTotal += item.getSubtotalItem();
    }
}