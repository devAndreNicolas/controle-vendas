package controle_vendas_test.dao_test;

import org.example.controle_vendas.dao.ProdutoDAO;
import org.example.controle_vendas.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ProdutoDAOTest extends BaseDAOTest {
    private ProdutoDAO produtoDAO;

    @BeforeEach
    public void init() {
        produtoDAO = new ProdutoDAO(connection);
    }

    @Test
    public void testInserirEBuscar() throws SQLException {
        Produto p = new Produto();
        p.setCategoriaId(1);
        p.setNomeProduto("Teclado");
        p.setPrecoVenda(50.0);
        p.setPrecoCusto(30.0);
        p.setUnidadeMedida("un");

        produtoDAO.inserir(p);
        assertTrue(p.getProdutoId() > 0);

        Produto buscado = produtoDAO.buscarPorId(p.getProdutoId());
        assertNotNull(buscado);
        assertEquals("Teclado", buscado.getNomeProduto());
    }

//    @Test
//    public void testAtualizarEInativar() throws SQLException {
//        Produto p = new Produto();
//        p.setCategoriaId(2);
//        p.setNomeProduto("Mouse");
//        p.setPrecoVenda(40.0);
//        p.setPrecoCusto(20.0);
//        p.setUnidadeMedida("un");
//        produtoDAO.inserir(p);
//
//        p.setPrecoVenda(45.0);
//        produtoDAO.atualizar(p);
//
//        Produto atualizado = produtoDAO.buscarPorId(p.getProdutoId());
//        assertEquals(45.0, atualizado.getPrecoVenda());
//
//        produtoDAO.inativar(p.getProdutoId());
//
//        List<Produto> ativos = produtoDAO.listarTodos();
//        assertTrue(ativos.stream().noneMatch(prod -> prod.getProdutoId() == p.getProdutoId()));
//    }
}
