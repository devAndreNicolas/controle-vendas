package org.example.controle_vendas.model;

public class Cliente {
    private int clienteId;
    private String nome;
    private String cpfCnpj; // CPF ou CNPJ
    private char tipo;      // 'F' para física, 'J' para jurídica
    private String email;
    private String telefone;
    private String endereco;

    public Cliente() {}

    public Cliente(int clienteId, String nome, String cpfCnpj, char tipo,
                   String email, String telefone, String endereco) {
        this.clienteId = clienteId;
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.tipo = tipo;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    @Override
    public String toString() {
        return nome;
    }
}