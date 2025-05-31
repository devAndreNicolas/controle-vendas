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
        // Outras validações podem ser adicionadas

        produtoDAO.inserir(produto);
    }

    public List<Produto> listarProdutos() throws SQLException {
        return produtoDAO.listarTodos();
    }
}