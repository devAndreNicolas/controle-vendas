import static org.junit.Assert.assertEquals;

import org.example.controle_vendas.dao.VendaDAO;
import org.example.controle_vendas.service.VendaService;
import org.example.controle_vendas.model.Venda;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

public class VendaServiceTest {

    private VendaService vendaService;

    @Before
    public void setUp() {        
        VendaDAO vendaDAO = new VendaDAO();
        vendaService = new VendaService(vendaDAO);
    }

    @Test
    public void testBuscarQuantidadeVendasPorData() throws Exception {
        LocalDate data = LocalDate.of(2025, 6, 10);

        List<Venda> vendas = vendaService.buscarVendasPorData(data);
        
        int quantidadeEsperada = 2;

        assertEquals(quantidadeEsperada, vendas.size());
    }
}
