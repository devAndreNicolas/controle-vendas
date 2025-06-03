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
        // Adicionar validações de CPF se necessário
        funcionarioDAO.inserir(funcionario);
    }

    public List<Funcionario> listarFuncionarios() throws SQLException {
        return funcionarioDAO.listarTodos();
    }

    // NOVO: Atualizar Funcionário
    public void atualizarFuncionario(Funcionario funcionario) throws SQLException {
        if (funcionario.getFuncionarioId() <= 0) {
            throw new IllegalArgumentException("ID do funcionário inválido para atualização.");
        }
        if (funcionario.getNome() == null || funcionario.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do funcionário é obrigatório para atualização.");
        }
        if (funcionario.getCpf() == null || funcionario.getCpf().isBlank()) {
            throw new IllegalArgumentException("CPF do funcionário é obrigatório para atualização.");
        }
        funcionarioDAO.atualizar(funcionario);
    }

    // NOVO: Buscar Funcionário por ID
    public Funcionario buscarPorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do funcionário inválido para busca.");
        }
        return funcionarioDAO.buscarPorId(id);
    }

    // NOVO: Inativar Funcionário (Soft Delete)
    public void inativarFuncionario(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do funcionário inválido para inativação.");
        }
        // Poderia adicionar validações aqui antes de inativar, se necessário
        funcionarioDAO.inativar(id);
    }
}