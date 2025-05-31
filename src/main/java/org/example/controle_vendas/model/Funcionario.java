package org.example.controle_vendas.model;

public class Funcionario {
    private int funcionarioId;
    private String nome;
    private String cpf;

    public Funcionario() {}

    public Funcionario(int funcionarioId, String nome, String cpf) {

        this.funcionarioId = funcionarioId;
        this.nome = nome;
        this.cpf = cpf;
    }

    public int getFuncionarioId() {return funcionarioId;}
    public void setFuncionarioId(int funcionarioId) {this.funcionarioId = funcionarioId;}

    public String getNome() { return nome;}
    public void setNome(String nome) { this.nome = nome;}

    public String getCpf() { return cpf;}
    public void setCpf(String cpf) { this.cpf = cpf;}
}
