package org.example.controle_vendas.model;

public class Produto {
    private int produtoId;
    private String nomeProduto;
    private int precoVenda;
    private int precoCusto;
    private String unidadeMedida;

    public Produto() {}

    public Produto(int produtoId, String nomeProduto, int precoVenda, int precoCusto) {

        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.precoVenda = precoVenda;
        this.precoCusto = precoCusto;
        this.unidadeMedida = unidadeMedida;
    }

    public int getProdutoID() {return produtoId;}
    public void setProdutoID(int produtoId) {this.produtoId = produtoId;}

    public String getNomeProduto() {return nomeProduto;}
    public void setNomeProduto(String nomeProduto) {this.nomeProduto = nomeProduto;}

    public int getPrecoVenda() {return precoVenda;}
    public void setPrecoVenda(int precoVenda) {this.precoVenda = precoVenda;}

    public int getPrecoCusto() {return precoCusto;}
    public void setPrecoCusto(int precoCusto) {this.precoCusto = precoCusto;}

    public String getUnidadeMedida() {return unidadeMedida;}
    public void setUnidadeMedida(String unidadeMedida) {this.unidadeMedida = unidadeMedida;}
}
