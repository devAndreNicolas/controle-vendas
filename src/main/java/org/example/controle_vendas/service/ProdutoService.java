package org.example.controle_vendas.service;

import org.example.controle_vendas.dao.ProdutoDAO;
import org.example.controle_vendas.model.Produto;

import java.sql.SQLException;
import java.util.List;

public class ProdutoService {
    private final ProdutoDAO produtoDAO;

    public ProdutoService(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    public void cadastrarProduto(Produto produto) throws SQLException {
        if (produto.getNomeProduto() == null || produto.getNomeProduto().isBlank()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }
        if (produto.getPrecoVenda() <= 0) {
            throw new IllegalArgumentException("Preço de venda deve ser maior que zero");
        }
        if (produto.getCategoriaId() <= 0) { // Validação para categoria
            throw new IllegalArgumentException("Categoria do produto é obrigatória");
        }
        if (produto.getUnidadeMedida() == null || produto.getUnidadeMedida().isBlank()) {
            throw new IllegalArgumentException("Unidade de medida do produto é obrigatória");
        }

        produtoDAO.inserir(produto);
    }

    public List<Produto> listarProdutosAtivos() throws SQLException {
        return produtoDAO.listarTodosAtivos();
    }

    public List<Produto> listarProdutos() throws SQLException {
        return produtoDAO.listarTodos();
    }

    // NOVO: Atualizar Produto
    public void atualizarProduto(Produto produto) throws SQLException {
        if (produto.getProdutoId() <= 0) {
            throw new IllegalArgumentException("ID do produto inválido para atualização.");
        }
        if (produto.getNomeProduto() == null || produto.getNomeProduto().isBlank()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório para atualização.");
        }
        if (produto.getPrecoVenda() <= 0) {
            throw new IllegalArgumentException("Preço de venda deve ser maior que zero para atualização.");
        }
        if (produto.getCategoriaId() <= 0) {
            throw new IllegalArgumentException("Categoria do produto é obrigatória para atualização.");
        }
        if (produto.getUnidadeMedida() == null || produto.getUnidadeMedida().isBlank()) {
            throw new IllegalArgumentException("Unidade de medida do produto é obrigatória para atualização.");
        }
        produtoDAO.atualizar(produto);
    }

    // NOVO: Buscar Produto por ID
    public Produto buscarPorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do produto inválido para busca.");
        }
        return produtoDAO.buscarPorId(id);
    }

    // NOVO: Inativar Produto (Soft Delete)
    public void inativarProduto(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do produto inválido para inativação.");
        }
        // Poderia adicionar validações aqui antes de inativar, se necessário
        produtoDAO.inativar(id);
    }

    public Produto buscarProdutoMaisVendido() throws SQLException {
        return produtoDAO.buscarProdutoMaisVendido();
    }
}