package org.example.controle_vendas.model;

import java.util.Objects; // Adicionar esta importação para 'Objects.hash'

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

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
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

    // MUITO IMPORTANTE PARA O JComboBox
    @Override
    public String toString() {
        return nome; // O JComboBox irá exibir o nome da categoria
    }

    // Opcional: equals e hashCode se você for usar Categoria em Set/Map
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return categoriaId == categoria.categoriaId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoriaId);
    }
}