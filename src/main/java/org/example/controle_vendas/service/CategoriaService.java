package org.example.controle_vendas.service;

import org.example.controle_vendas.dao.CategoriaDAO;
import org.example.controle_vendas.model.Categoria;

import java.sql.SQLException;
import java.util.List;

public class CategoriaService {
    private final CategoriaDAO categoriaDAO;

    public CategoriaService(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    public void cadastrarCategoria(Categoria categoria) throws SQLException {
        if (categoria.getNome() == null || categoria.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome da categoria é obrigatório");
        }
        categoriaDAO.inserir(categoria);
    }

    public List<Categoria> listarCategorias() throws SQLException {
        return categoriaDAO.listarTodos();
    }

    public void atualizarCategoria(Categoria categoria) throws SQLException {
        if (categoria.getCategoriaId() <= 0) {
            throw new IllegalArgumentException("ID da categoria inválido para atualização.");
        }
        if (categoria.getNome() == null || categoria.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome da categoria é obrigatório para atualização.");
        }
        categoriaDAO.atualizar(categoria);
    }

    public Categoria buscarPorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID da categoria inválido para busca.");
        }
        return categoriaDAO.buscarPorId(id);
    }

    // MÉTODO ALTERADO: Agora chama 'inativar' no DAO
    public void inativarCategoria(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID da categoria inválido para inativação.");
        }
        // Você pode adicionar uma validação para verificar se a categoria pode ser inativada
        // Ex: verificar se há produtos ou vendas ativas vinculadas, se desejar.
        categoriaDAO.inativar(id);
    }
}