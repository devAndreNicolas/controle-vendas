package org.example.controle_vendas.model;

public class Categoria {
    private int categoriaId;
    private String nome;
    private String descricao;

    public Categoria() {}

    public Categoria(int categoriaId, String nome, String descricao) {
        this.categoriaId = categoriaId;
        this.nome = nome;
        this.descricao = descricao;
    }

    public int getCategoriaID() {
        return categoriaId;
    }

    public void setCategoriaID(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
