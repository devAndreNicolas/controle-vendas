package org.example.controle_vendas.service;

import org.example.controle_vendas.dao.ClienteDAO;
import org.example.controle_vendas.model.Cliente;

import java.sql.SQLException;
import java.util.List;

public class ClienteService {
    private final ClienteDAO clienteDAO;

    public ClienteService(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public void cadastrarCliente(Cliente cliente) throws SQLException {
        if (cliente.getNome() == null || cliente.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do cliente é obrigatório");
        }
        if (cliente.getCpfCnpj() == null || cliente.getCpfCnpj().isBlank()) {
            throw new IllegalArgumentException("CPF/CNPJ do cliente é obrigatório");
        }
        // Adicionar validações de CPF/CNPJ, Email, Telefone, Endereço se necessário
        clienteDAO.inserir(cliente);
    }

    public List<Cliente> listarClientes() throws SQLException {
        return clienteDAO.listarTodos();
    }

    // NOVO: Atualizar Cliente
    public void atualizarCliente(Cliente cliente) throws SQLException {
        if (cliente.getClienteId() <= 0) {
            throw new IllegalArgumentException("ID do cliente inválido para atualização.");
        }
        if (cliente.getNome() == null || cliente.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do cliente é obrigatório para atualização.");
        }
        if (cliente.getCpfCnpj() == null || cliente.getCpfCnpj().isBlank()) {
            throw new IllegalArgumentException("CPF/CNPJ do cliente é obrigatório para atualização.");
        }
        clienteDAO.atualizar(cliente);
    }

    // NOVO: Buscar Cliente por ID
    public Cliente buscarPorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do cliente inválido para busca.");
        }
        return clienteDAO.buscarPorId(id);
    }

    // NOVO: Inativar Cliente (Soft Delete)
    public void inativarCliente(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do cliente inválido para inativação.");
        }
        // Poderia adicionar validações aqui antes de inativar, se necessário
        clienteDAO.inativar(id);
    }
}