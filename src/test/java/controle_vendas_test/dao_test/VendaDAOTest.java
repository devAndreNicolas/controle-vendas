package controle_vendas_test.dao_test;

import org.example.controle_vendas.dao.VendaDAO;
import org.example.controle_vendas.model.ItemVenda;
import org.example.controle_vendas.model.Venda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VendaDAOTest extends BaseDAOTest {
    private VendaDAO vendaDAO;

    @BeforeEach
    public void init() {
        vendaDAO = new VendaDAO(connection);
    }

    @Test
    public void testInserirVendaEListar() throws SQLException {
        Venda venda = new Venda();
        venda.setClienteId(1);
        venda.setFuncionarioId(1);
        venda.setData(LocalDateTime.now());
        venda.setValorTotal(100.0);
        venda.setStatus("Em Aberto");

        int vendaId = vendaDAO.inserirVenda(venda);
        assertTrue(vendaId > 0);

        List<Venda> vendas = vendaDAO.listarVendas();
        assertFalse(vendas.isEmpty());

        Venda vendaBuscada = vendas.stream()
                .filter(v -> v.getVendaId() == vendaId)
                .findFirst()
                .orElse(null);
        assertNotNull(vendaBuscada);
        assertEquals(100.0, vendaBuscada.getValorTotal());
    }

    @Test
    public void testInserirItemVendaEListar() throws SQLException {
        Venda venda = new Venda();
        venda.setClienteId(1);
        venda.setFuncionarioId(1);
        venda.setData(LocalDateTime.now());
        venda.setValorTotal(0.0);
        venda.setStatus("Em Aberto");
        int vendaId = vendaDAO.inserirVenda(venda);

        ItemVenda item = new ItemVenda();
        item.setVendaId(vendaId);
        item.setProdutoId(1);
        item.setQuantidadeVendida(3);
        item.setPrecoUnitarioVendido(10.0);

        vendaDAO.inserirItemVenda(item);
        assertTrue(item.getItemVendaId() > 0);

        List<ItemVenda> itens = vendaDAO.listarItensPorVenda(vendaId);
        assertFalse(itens.isEmpty());
        assertEquals(3, itens.get(0).getQuantidadeVendida());
    }
}
