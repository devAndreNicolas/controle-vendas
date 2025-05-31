package org.example.controle_vendas.ui;

import org.example.controle_vendas.dao.ClienteDAO;
import org.example.controle_vendas.model.Cliente;
import org.example.controle_vendas.service.ClienteService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class ClienteUI extends JFrame {
    private final ClienteService clienteService;

    private JTextField tfNome, tfCpfCnpj, tfEmail, tfTelefone, tfEndereco;
    private JTable tabelaClientes;
    private DefaultTableModel tabelaModel;

    public ClienteUI(ClienteService clienteService) {
        this.clienteService = clienteService;
        initComponents();
        carregarClientes();
    }

    private void initComponents() {
        setTitle("Cadastro de Clientes");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Formulário
        JPanel painelFormulario = new JPanel(new GridLayout(6, 2, 5, 5));
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Novo Cliente"));

        painelFormulario.add(new JLabel("Nome:"));
        tfNome = new JTextField();
        painelFormulario.add(tfNome);

        painelFormulario.add(new JLabel("CPF/CNPJ:"));
        tfCpfCnpj = new JTextField();
        painelFormulario.add(tfCpfCnpj);

        painelFormulario.add(new JLabel("Email:"));
        tfEmail = new JTextField();
        painelFormulario.add(tfEmail);

        painelFormulario.add(new JLabel("Telefone:"));
        tfTelefone = new JTextField();
        painelFormulario.add(tfTelefone);

        painelFormulario.add(new JLabel("Endereço:"));
        tfEndereco = new JTextField();
        painelFormulario.add(tfEndereco);

        JButton btnSalvar = new JButton("Salvar Cliente");
        btnSalvar.addActionListener(e -> salvarCliente());
        painelFormulario.add(btnSalvar);

        // Painel vazio só para alinhamento
        painelFormulario.add(new JLabel());

        // Tabela
        tabelaModel = new DefaultTableModel(new String[]{"ID", "Nome", "CPF/CNPJ", "Email", "Telefone", "Endereço"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false; // tabela somente leitura
            }
        };
        tabelaClientes = new JTable(tabelaModel);
        JScrollPane scrollPane = new JScrollPane(tabelaClientes);

        // Layout principal
        setLayout(new BorderLayout(10, 10));
        add(painelFormulario, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void salvarCliente() {
        try {
            Cliente cliente = new Cliente();
            cliente.setNome(tfNome.getText());
            cliente.setCpfCnpj(tfCpfCnpj.getText());
            cliente.setEmail(tfEmail.getText());
            cliente.setTelefone(tfTelefone.getText());
            cliente.setEndereco(tfEndereco.getText());

            clienteService.cadastrarCliente(cliente);

            JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
            limparFormulario();
            carregarClientes();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar no banco: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarClientes() {
        try {
            List<Cliente> clientes = clienteService.listarClientes();
            tabelaModel.setRowCount(0);
            for (Cliente c : clientes) {
                tabelaModel.addRow(new Object[]{
                        c.getClienteId(),
                        c.getNome(),
                        c.getCpfCnpj(),
                        c.getEmail(),
                        c.getTelefone(),
                        c.getEndereco()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar clientes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        tfNome.setText("");
        tfCpfCnpj.setText("");
        tfEmail.setText("");
        tfTelefone.setText("");
        tfEndereco.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Ajuste usuário, senha e URL do banco conforme seu ambiente
                Connection connection = DriverManager.getConnection(System.getenv("DB_URL"), System.getenv("DB_USER"), System.getenv("DB_PASSWORD"));
                ClienteDAO clienteDAO = new ClienteDAO(connection);
                ClienteService clienteService = new ClienteService(clienteDAO);

                ClienteUI ui = new ClienteUI(clienteService);
                ui.setVisible(true);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Falha ao conectar com o banco: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}