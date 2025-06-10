package controle_vendas_test.dao_test;

import org.example.controle_vendas.dao.FuncionarioDAO;
import org.example.controle_vendas.model.Funcionario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FuncionarioDAOTest extends BaseDAOTest {
    private FuncionarioDAO funcionarioDAO;

    @BeforeEach
    public void init() {
        funcionarioDAO = new FuncionarioDAO(connection);
    }

    @Test
    public void testInserirEListar() throws SQLException {
        Funcionario f = new Funcionario();
        f.setNome("Carlos Silva");
        f.setCpf("12345678900");

        funcionarioDAO.inserir(f);
        assertTrue(f.getFuncionarioId() > 0);

        List<Funcionario> ativos = funcionarioDAO.listarTodos();
        assertFalse(ativos.isEmpty());
        assertEquals("Carlos Silva", ativos.get(0).getNome());
    }

    @Test
    public void testBuscarPorId() throws SQLException {
        Funcionario f = new Funcionario();
        f.setNome("Ana Paula");
        f.setCpf("98765432100");

        funcionarioDAO.inserir(f);

        Funcionario buscado = funcionarioDAO.buscarPorId(f.getFuncionarioId());
        assertNotNull(buscado);
        assertEquals("Ana Paula", buscado.getNome());
    }

    @Test
    public void testAtualizar() throws SQLException {
        Funcionario f = new Funcionario();
        f.setNome("Pedro");
        f.setCpf("11122233344");
        funcionarioDAO.inserir(f);

        f.setNome("Pedro Atualizado");
        funcionarioDAO.atualizar(f);

        Funcionario atualizado = funcionarioDAO.buscarPorId(f.getFuncionarioId());
        assertEquals("Pedro Atualizado", atualizado.getNome());
    }

    @Test
    public void testInativar() throws SQLException {
        Funcionario f = new Funcionario();
        f.setNome("Lucas");
        f.setCpf("55566677788");
        funcionarioDAO.inserir(f);

        funcionarioDAO.inativar(f.getFuncionarioId());

        List<Funcionario> ativos = funcionarioDAO.listarTodos();
        assertTrue(ativos.stream().noneMatch(func -> func.getFuncionarioId() == f.getFuncionarioId()));
    }
}
