package org.example.controle_vendas.service;

import org.example.controle_vendas.dao.FuncionarioDAO;
import org.example.controle_vendas.model.Funcionario;

import java.sql.SQLException;
import java.util.List;

public class FuncionarioService {
    private final FuncionarioDAO funcionarioDAO;

    public FuncionarioService(FuncionarioDAO funcionarioDAO) {
        this.funcionarioDAO = funcionarioDAO;
    }

    public void cadastrarFuncionario(Funcionario funcionario) throws SQLException {
        if (funcionario.getNome() == null || funcionario.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do funcionário é obrigatório");
        }
        if (funcionario.getCpf() == null || funcionario.getCpf().isBlank()) {
            throw new IllegalArgumentException("CPF do funcionário é obrigatório");
        }
        // Possível validação de formato CPF

        funcionarioDAO.inserir(funcionario);
    }

    public List<Funcionario> listarFuncionarios() throws SQLException {
        return funcionarioDAO.listarTodos();
    }
}