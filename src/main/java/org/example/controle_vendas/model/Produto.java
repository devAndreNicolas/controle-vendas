package org.example.controle_vendas.model;

public class Produto {
    private int produtoId;
    private int categoriaId;
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
        this.categoriaId = categoriaId;
    }

    public int getProdutoId() {return produtoId;}
    public void setProdutoId(int produtoId) {this.produtoId = produtoId;}

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getNomeProduto() {return nomeProduto;}
    public void setNomeProduto(String nomeProduto) {this.nomeProduto = nomeProduto;}

    public int getPrecoVenda() {return precoVenda;}
    public void setPrecoVenda(int precoVenda) {this.precoVenda = precoVenda;}

    public int getPrecoCusto() {return precoCusto;}
    public void setPrecoCusto(int precoCusto) {this.precoCusto = precoCusto;}

    public String getUnidadeMedida() {return unidadeMedida;}
    public void setUnidadeMedida(String unidadeMedida) {this.unidadeMedida = unidadeMedida;}
}