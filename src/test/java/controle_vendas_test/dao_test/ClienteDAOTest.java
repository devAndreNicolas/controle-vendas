package controle_vendas_test.dao_test;

import org.example.controle_vendas.dao.ClienteDAO;
import org.example.controle_vendas.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ClienteDAOTest extends BaseDAOTest{
    private ClienteDAO clienteDAO;

    @BeforeEach
    public void init() {
        clienteDAO = new ClienteDAO(connection);
    }

    @Test
    public void testInserirEListar() throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setNome("Maria Silva");
        cliente.setCpfCnpj("12345678900");
        cliente.setEmail("maria@email.com");
        cliente.setTelefone("999999999");
        cliente.setEndereco("Rua A, 123");
        clienteDAO.inserir(cliente);

        assertTrue(cliente.getClienteId() > 0);

        List<Cliente> clientes = clienteDAO.listarTodos();
        assertFalse(clientes.isEmpty());
        assertEquals("Maria Silva", clientes.get(0).getNome());
    }

    @Test
    public void testBuscarPorId() throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setNome("João Pedro");
        cliente.setCpfCnpj("11122233344");
        cliente.setEmail("joao@email.com");
        cliente.setTelefone("888888888");
        cliente.setEndereco("Rua B, 456");
        clienteDAO.inserir(cliente);

        Cliente buscado = clienteDAO.buscarPorId(cliente.getClienteId());
        assertNotNull(buscado);
        assertEquals("João Pedro", buscado.getNome());
    }

    @Test
    public void testAtualizar() throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setNome("Ana Clara");
        cliente.setCpfCnpj("99988877766");
        cliente.setEmail("ana@email.com");
        cliente.setTelefone("777777777");
        cliente.setEndereco("Rua C, 789");
        clienteDAO.inserir(cliente);

        cliente.setNome("Ana Clara Atualizada");
        clienteDAO.atualizar(cliente);

        Cliente atualizado = clienteDAO.buscarPorId(cliente.getClienteId());
        assertEquals("Ana Clara Atualizada", atualizado.getNome());
    }

    @Test
    public void testInativar() throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setNome("Carlos");
        cliente.setCpfCnpj("55566677788");
        cliente.setEmail("carlos@email.com");
        cliente.setTelefone("666666666");
        cliente.setEndereco("Rua D, 101");
        clienteDAO.inserir(cliente);

        clienteDAO.inativar(cliente.getClienteId());

        List<Cliente> clientes = clienteDAO.listarTodos();
        assertTrue(clientes.stream().noneMatch(c -> c.getClienteId() == cliente.getClienteId()));
    }
}
