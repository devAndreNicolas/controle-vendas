package controle_vendas_test.service_test;

import controle_vendas_test.service_test.mock_DAO.ClienteDAOFake;
import org.example.controle_vendas.model.Cliente;
import org.example.controle_vendas.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ClienteServiceTest {

    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        clienteService = new ClienteService(new ClienteDAOFake());
    }

    @Test
    void deveRetornarDadosDoClientePorId() throws SQLException {
        Cliente cliente = clienteService.buscarPorId(1);

        assertNotNull(cliente);
        assertEquals(1, cliente.getClienteId());
        assertEquals("João Silva", cliente.getNome());
        assertEquals("000.000.000-00", cliente.getCpfCnpj());
        assertEquals("joao@gmail.com", cliente.getEmail());
        assertEquals("(82) 99999-9999", cliente.getTelefone());
        assertEquals("Rua A, 123", cliente.getEndereco());
    }
}