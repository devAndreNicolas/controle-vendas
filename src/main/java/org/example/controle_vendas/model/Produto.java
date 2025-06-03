package org.example.controle_vendas.model;

// ... outras importações, se houver

public class Produto {
    private int produtoId;
    private int categoriaId;
    private String nomeProduto;
    private double precoVenda; // Confirmar que é double
    private double precoCusto; // Confirmar que é double
    private String unidadeMedida; // Este continua sendo String

    // Construtor vazio
    public Produto() {}

    // Construtor com campos (opcional)
    public Produto(int produtoId, int categoriaId, String nomeProduto, double precoVenda, double precoCusto, String unidadeMedida) {
        this.produtoId = produtoId;
        this.categoriaId = categoriaId;
        this.nomeProduto = nomeProduto;
        this.precoVenda = precoVenda;
        this.precoCusto = precoCusto;
        this.unidadeMedida = unidadeMedida;
    }

    // Getters e Setters
    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public double getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(double precoCusto) {
        this.precoCusto = precoCusto;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    @Override
    public String toString() {
        return nomeProduto; // Útil se você for usar Produto em outros JComboBoxes
    }
}