package controle_vendas_test.service_test;

import controle_vendas_test.service_test.mock_DAO.ProdutoDAOFake;
import org.example.controle_vendas.model.Produto;
import org.example.controle_vendas.service.ProdutoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class ProdutoServiceTest {

    @Test
    public void testBuscarProdutoMaisVendido() throws SQLException {
        ProdutoDAOFake produtoDAOFake = new ProdutoDAOFake();
        ProdutoService produtoService = new ProdutoService(produtoDAOFake);

        Produto produtoMaisVendido = produtoService.buscarProdutoMaisVendido();

        Assertions.assertNotNull(produtoMaisVendido, "Produto mais vendido não deve ser nulo");
        Assertions.assertEquals("Produto Mais Vendido Fake", produtoMaisVendido.getNomeProduto());
    }
}