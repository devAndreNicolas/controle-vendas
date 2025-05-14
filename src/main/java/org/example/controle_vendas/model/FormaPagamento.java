package org.example.controle_vendas.model;

public class FormaPagamento {
    private int formaPagamentoId;
    private String nome;
    private String descricao;
    private boolean ativo;

    public FormaPagamento() {}

    public FormaPagamento(int formaPagamentoId, String nome, String descricao, boolean ativo) {
        this.formaPagamentoId = formaPagamentoId;
        this.nome = nome;
        this.descricao = descricao;
        this.ativo = ativo;
    }

    public int getFormaPagamentoId() {
        return formaPagamentoId;
    }

    public void setFormaPagamentoId(int formaPagamentoId) {
        this.formaPagamentoId = formaPagamentoId;
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

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
