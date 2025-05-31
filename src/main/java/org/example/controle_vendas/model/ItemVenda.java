package org.example.controle_vendas.model;

public class ItemVenda {
    private int itemVendaId;
    private int vendaId;
    private int produtoId;
    private int quantidadeVendida;
    private double precoUnitarioVendido;
    private double subtotalItem;

    public ItemVenda() {}

    public ItemVenda(int itemVendaId, int vendaId, int produtoId, int quantidadeVendida, double precoUnitarioVendido) {
        this.itemVendaId = itemVendaId;
        this.vendaId = vendaId;
        this.produtoId = produtoId;
        this.quantidadeVendida = quantidadeVendida;
        this.precoUnitarioVendido = precoUnitarioVendido;
        this.subtotalItem = quantidadeVendida * precoUnitarioVendido;
    }

    public int getItemVendaId() {
        return itemVendaId;
    }

    public void setItemVendaId(int itemVendaId) {
        this.itemVendaId = itemVendaId;
    }

    public int getVendaId() {
        return vendaId;
    }

    public void setVendaId(int vendaId) {
        this.vendaId = vendaId;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public int getQuantidadeVendida() {
        return quantidadeVendida;
    }

    public void setQuantidadeVendida(int quantidadeVendida) {
        this.quantidadeVendida = quantidadeVendida;
        calcularSubtotal();
    }
    public double getPrecoUnitarioVendido() {
        return precoUnitarioVendido;
    }

    public void setPrecoUnitarioVendido(double precoUnitarioVendido) {
        this.precoUnitarioVendido = precoUnitarioVendido;
        calcularSubtotal();
    }

    public double getSubtotalItem() {
        return subtotalItem;
    }
    private void calcularSubtotal() {
        this.subtotalItem = this.quantidadeVendida * this.precoUnitarioVendido;
    }
}