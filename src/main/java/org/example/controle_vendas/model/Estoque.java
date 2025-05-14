package org.example.controle_vendas.model;

public class Estoque {
    private int estoqueId;
    private int produtoId;
    private int quantidadeAtual;
    private int quantidadeMinima;

    public Estoque() {}

    public Estoque(int estoqueId, int produtoId, int quantidadeAtual, int quantidadeMinima) {
        this.estoqueId = estoqueId;
        this.produtoId = produtoId;
        this.quantidadeAtual = quantidadeAtual;
        this.quantidadeMinima = quantidadeMinima;
    }
    public int getEstoqueID() {
        return estoqueId;
    }
    public void setEstoqueID(int estoqueId) {
        this.estoqueId = estoqueId;
    }
    public int getProdutoID() {
        return produtoId;
    }
    public void setProdutoID(int produtoId) {
        this.produtoId = produtoId;
    }
    public int getQuantidadeAtual() {
        return quantidadeAtual;
    }
    public void setQuantidadeAtual(int quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }
    public int getQuantidadeMinima() {
        return quantidadeMinima;
    }
    public void setQuantidadeMinima(int quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }
}
