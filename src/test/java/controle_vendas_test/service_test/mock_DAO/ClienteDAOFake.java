package controle_vendas_test.service_test.mock_DAO;
import org.example.controle_vendas.dao.ClienteDAO;
import org.example.controle_vendas.model.Cliente;

import java.util.HashMap;
import java.util.Map;

public class ClienteDAOFake extends ClienteDAO {
    private final Map<Integer, Cliente> clientes = new HashMap<>();

    public ClienteDAOFake() {
        super(null);  // aqui você passa null porque não usará a conexão de verdade
        Cliente cliente = new Cliente();
        cliente.setClienteId(1);
        cliente.setNome("João Silva");
        cliente.setCpfCnpj("000.000.000-00");
        cliente.setEmail("joao@gmail.com");
        cliente.setTelefone("(82) 99999-9999");
        cliente.setEndereco("Rua A, 123");

        clientes.put(1, cliente);
    }

    @Override
    public Cliente buscarPorId(int id) {
        return clientes.get(id);
    }
}
