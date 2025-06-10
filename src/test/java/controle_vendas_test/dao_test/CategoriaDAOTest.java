package controle_vendas_test.dao_test;
import org.example.controle_vendas.dao.CategoriaDAO;
import org.example.controle_vendas.model.Categoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CategoriaDAOTest extends BaseDAOTest {
    private CategoriaDAO categoriaDAO;

    @BeforeEach
    public void init() {
        categoriaDAO = new CategoriaDAO(connection);
    }

    @Test
    public void testInserirEListar() throws SQLException {
        Categoria cat = new Categoria();
        cat.setNome("Eletrônicos");
        cat.setDescricao("Produtos eletrônicos variados");
        categoriaDAO.inserir(cat);

        assertTrue(cat.getCategoriaId() > 0, "ID deve ser gerado após inserção");

        List<Categoria> categorias = categoriaDAO.listarTodos();
        assertFalse(categorias.isEmpty(), "Deve listar categorias ativas");
        assertEquals("Eletrônicos", categorias.get(0).getNome());
    }

    @Test
    public void testBuscarPorId() throws SQLException {
        Categoria cat = new Categoria();
        cat.setNome("Informatica");
        cat.setDescricao("Produtos de informática");
        categoriaDAO.inserir(cat);

        Categoria buscada = categoriaDAO.buscarPorId(cat.getCategoriaId());
        assertNotNull(buscada);
        assertEquals("Informatica", buscada.getNome());
    }

    @Test
    public void testAtualizar() throws SQLException {
        Categoria cat = new Categoria();
        cat.setNome("Roupas");
        cat.setDescricao("Vestuário");
        categoriaDAO.inserir(cat);

        cat.setNome("Roupas Atualizado");
        categoriaDAO.atualizar(cat);

        Categoria atualizada = categoriaDAO.buscarPorId(cat.getCategoriaId());
        assertEquals("Roupas Atualizado", atualizada.getNome());
    }

    @Test
    public void testInativar() throws SQLException {
        Categoria cat = new Categoria();
        cat.setNome("Brinquedos");
        cat.setDescricao("Brinquedos infantis");
        categoriaDAO.inserir(cat);

        categoriaDAO.inativar(cat.getCategoriaId());

        List<Categoria> categorias = categoriaDAO.listarTodos();
        assertTrue(categorias.stream().noneMatch(c -> c.getCategoriaId() == cat.getCategoriaId()),
                "Categoria inativada não deve aparecer na listagem");
    }
}
