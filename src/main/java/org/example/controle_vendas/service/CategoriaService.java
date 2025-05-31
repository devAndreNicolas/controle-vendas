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
}