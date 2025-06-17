package controle_vendas_test.service_test.mock_DAO;
import org.example.controle_vendas.dao.ProdutoDAO;
import org.example.controle_vendas.model.Produto;

import java.sql.SQLException;

public class ProdutoDAOFake extends ProdutoDAO{

    public ProdutoDAOFake() {
        super(null); // Passa null para o construtor, pois não usaremos conexão real
    }

    @Override
    public Produto buscarProdutoMaisVendido() throws SQLException {
        Produto produto = new Produto();
        produto.setNomeProduto("Produto Mais Vendido Fake");
        return produto;
    }
}
