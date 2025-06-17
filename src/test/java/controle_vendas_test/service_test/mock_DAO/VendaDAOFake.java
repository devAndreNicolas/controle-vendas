package controle_vendas_test.service_test.mock_DAO;
import org.example.controle_vendas.dao.VendaDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class VendaDAOFake extends VendaDAO {
    private final Map<LocalDate, Integer> vendasPorData = new HashMap<>();

    public VendaDAOFake() {
        super(null); // Sem conexão real
        // Exemplo: 5 vendas no dia 2025-06-17
        vendasPorData.put(LocalDate.of(2025, 6, 17), 5);
        vendasPorData.put(LocalDate.of(2025, 6, 18), 3);
    }

    @Override
    public int contarVendasPorData(LocalDate data) throws SQLException {
        return vendasPorData.getOrDefault(data, 0);
    }
}
