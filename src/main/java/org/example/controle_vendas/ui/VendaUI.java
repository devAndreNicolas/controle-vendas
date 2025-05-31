package org.example.controle_vendas.ui;

import org.example.controle_vendas.dao.ClienteDAO;
import org.example.controle_vendas.dao.FuncionarioDAO;
import org.example.controle_vendas.dao.ProdutoDAO;
import org.example.controle_vendas.dao.VendaDAO;
import org.example.controle_vendas.model.Cliente;
import org.example.controle_vendas.model.Funcionario;
import org.example.controle_vendas.model.ItemVenda;
import org.example.controle_vendas.model.Produto;
import org.example.controle_vendas.model.Venda;
import org.example.controle_vendas.service.VendaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class VendaUI extends JFrame {
    private final VendaService vendaService;
    private final ClienteDAO clienteDAO;
    private final FuncionarioDAO funcionarioDAO;
    private final ProdutoDAO produtoDAO;

    private JComboBox<Cliente> cbClientes;
    private JComboBox<Funcionario> cbFuncionarios;
    private JComboBox<Produto> cbProdutos;
    private JTextField tfQuantidade;
    private JButton btnAddItem;
    private JTable tabelaItens;
    private DefaultTableModel tabelaModelItens;
    private JButton btnSalvarVenda;
    private JTable tabelaVendas;
    private DefaultTableModel tabelaModelVendas;

    private Venda vendaAtual;

    public VendaUI(Connection connection) {
        this.vendaService = new VendaService(new VendaDAO(connection));
        this.clienteDAO = new ClienteDAO(connection);
        this.funcionarioDAO = new FuncionarioDAO(connection);
        this.produtoDAO = new ProdutoDAO(connection);

        this.vendaAtual = new Venda();

        initComponents();
        carregarComboClientes();
        carregarComboFuncionarios();
        carregarComboProdutos();
        carregarVendas();
    }

    private void initComponents() {
        setTitle("Cadastro de Vendas");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelVenda = new JPanel(new GridLayout(5, 2, 5, 5));
        painelVenda.setBorder(BorderFactory.createTitledBorder("Nova Venda"));

        painelVenda.add(new JLabel("Cliente:"));
        cbClientes = new JComboBox<>();
        painelVenda.add(cbClientes);

        painelVenda.add(new JLabel("Funcionário:"));
        cbFuncionarios = new JComboBox<>();
        painelVenda.add(cbFuncionarios);

        painelVenda.add(new JLabel("Produto:"));
        cbProdutos = new JComboBox<>();
        painelVenda.add(cbProdutos);

        painelVenda.add(new JLabel("Quantidade:"));
        tfQuantidade = new JTextField();
        painelVenda.add(tfQuantidade);

        btnAddItem = new JButton("Adicionar Item");
        btnAddItem.addActionListener(e -> adicionarItem());
        painelVenda.add(btnAddItem);

        btnSalvarVenda = new JButton("Salvar Venda");
        btnSalvarVenda.addActionListener(e -> salvarVenda());
        painelVenda.add(btnSalvarVenda);

        tabelaModelItens = new DefaultTableModel(new String[]{"Produto", "Quantidade", "Preço Unit.", "Subtotal"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabelaItens = new JTable(tabelaModelItens);
        JScrollPane spItens = new JScrollPane(tabelaItens);
        spItens.setBorder(BorderFactory.createTitledBorder("Itens da Venda"));

        tabelaModelVendas = new DefaultTableModel(new String[]{"ID", "Cliente", "Funcionário", "Data", "Valor Total", "Status"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabelaVendas = new JTable(tabelaModelVendas);
        JScrollPane spVendas = new JScrollPane(tabelaVendas);
        spVendas.setBorder(BorderFactory.createTitledBorder("Vendas Realizadas"));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, spItens, spVendas);
        splitPane.setDividerLocation(200);

        setLayout(new BorderLayout(10, 10));
        add(painelVenda, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    private void carregarComboClientes() {
        try {
            List<Cliente> clientes = clienteDAO.listarTodos();
            DefaultComboBoxModel<Cliente> model = new DefaultComboBoxModel<>();
            for (Cliente c : clientes) model.addElement(c);
            cbClientes.setModel(model);
        } catch (SQLException e) {
            showError("Erro ao carregar clientes: " + e.getMessage());
        }
    }

    private void carregarComboFuncionarios() {
        try {
            List<Funcionario> funcionarios = funcionarioDAO.listarTodos();
            DefaultComboBoxModel<Funcionario> model = new DefaultComboBoxModel<>();
            for (Funcionario f : funcionarios) model.addElement(f);
            cbFuncionarios.setModel(model);
        } catch (SQLException e) {
            showError("Erro ao carregar funcionários: " + e.getMessage());
        }
    }

    private void carregarComboProdutos() {
        try {
            List<Produto> produtos = produtoDAO.listarTodos();
            DefaultComboBoxModel<Produto> model = new DefaultComboBoxModel<>();
            for (Produto p : produtos) model.addElement(p);
            cbProdutos.setModel(model);
        } catch (SQLException e) {
            showError("Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void adicionarItem() {
        Produto produto = (Produto) cbProdutos.getSelectedItem();
        if (produto == null) {
            showError("Selecione um produto");
            return;
        }
        int quantidade;
        try {
            quantidade = Integer.parseInt(tfQuantidade.getText());
            if (quantidade <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showError("Quantidade inválida");
            return;
        }

        ItemVenda item = new ItemVenda();
        item.setProdutoId(produto.getProdutoId());
        item.setQuantidadeVendida(quantidade);
        item.setPrecoUnitarioVendido(produto.getPrecoVenda()); // subtotal calculado automaticamente

        vendaAtual.adicionarItem(item);

        tabelaModelItens.addRow(new Object[]{
                produto.getNomeProduto(),
                quantidade,
                produto.getPrecoVenda(),
                item.getSubtotalItem()
        });

        tfQuantidade.setText("");
    }

    private void salvarVenda() {
        Cliente cliente = (Cliente) cbClientes.getSelectedItem();
        Funcionario funcionario = (Funcionario) cbFuncionarios.getSelectedItem();

        if (cliente == null || funcionario == null) {
            showError("Selecione cliente e funcionário");
            return;
        }
        if (vendaAtual.getItens().isEmpty()) {
            showError("Adicione pelo menos um item à venda");
            return;
        }

        vendaAtual.setClienteId(cliente.getClienteId());
        vendaAtual.setFuncionarioId(funcionario.getFuncionarioId());

        try {
            vendaService.cadastrarVenda(vendaAtual);
            JOptionPane.showMessageDialog(this, "Venda salva com sucesso!");
            vendaAtual = new Venda();
            tabelaModelItens.setRowCount(0);
            carregarVendas();
        } catch (Exception e) {
            showError("Erro ao salvar venda: " + e.getMessage());
        }
    }

    private void carregarVendas() {
        try {
            List<Venda> vendas = vendaService.listarVendas();
            tabelaModelVendas.setRowCount(0);
            for (Venda v : vendas) {
                // Para mostrar nomes de cliente e funcionário, ideal seria buscar seus nomes via DAO
                String nomeCliente = "ID " + v.getClienteId();
                String nomeFuncionario = "ID " + v.getFuncionarioId();

                tabelaModelVendas.addRow(new Object[]{
                        v.getVendaId(),
                        nomeCliente,
                        nomeFuncionario,
                        v.getData(),
                        v.getValorTotal(),
                        v.getStatus()
                });
            }
        } catch (SQLException e) {
            showError("Erro ao listar vendas: " + e.getMessage());
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Connection connection = DriverManager.getConnection(System.getenv("DB_URL"), System.getenv("DB_USER"), System.getenv("DB_PASSWORD"));
                VendaUI ui = new VendaUI(connection);
                ui.setVisible(true);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}