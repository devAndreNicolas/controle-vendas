package controle_vendas_test.service_test;

import controle_vendas_test.service_test.mock_DAO.VendaDAOFake;
import org.example.controle_vendas.service.VendaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

public class VendaServiceTest {

    @Test
    public void testContarVendasPorData() throws SQLException {
        // Arrange
        VendaDAOFake vendaDAOFake = new VendaDAOFake();
        VendaService vendaService = new VendaService(vendaDAOFake);

        LocalDate dataTeste = LocalDate.of(2025, 6, 17);

        // Act
        int totalVendas = vendaService.contarVendasPorData(dataTeste);

        // Assert
        Assertions.assertEquals(5, totalVendas, "Deve retornar 5 vendas para a data especificada");

        // Testar outra data com 0 vendas
        int vendasOutraData = vendaService.contarVendasPorData(LocalDate.of(2025, 6, 19));
        Assertions.assertEquals(0, vendasOutraData, "Deve retornar 0 vendas para data sem registro");
    }
}