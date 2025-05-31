package org.example.controle_vendas.ui;

import org.example.controle_vendas.dao.FuncionarioDAO;
import org.example.controle_vendas.model.Funcionario;
import org.example.controle_vendas.service.FuncionarioService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class FuncionarioUI extends JFrame {
    private final FuncionarioService funcionarioService;

    private JTextField tfNome, tfCpf;
    private JTable tabelaFuncionarios;
    private DefaultTableModel tabelaModel;

    public FuncionarioUI(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
        initComponents();
        carregarFuncionarios();
    }

    private void initComponents() {
        setTitle("Cadastro de Funcionários");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelFormulario = new JPanel(new GridLayout(3, 2, 5, 5));
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Novo Funcionário"));

        painelFormulario.add(new JLabel("Nome:"));
        tfNome = new JTextField();
        painelFormulario.add(tfNome);

        painelFormulario.add(new JLabel("CPF:"));
        tfCpf = new JTextField();
        painelFormulario.add(tfCpf);

        JButton btnSalvar = new JButton("Salvar Funcionário");
        btnSalvar.addActionListener(e -> salvarFuncionario());
        painelFormulario.add(btnSalvar);

        painelFormulario.add(new JLabel()); // espaço para alinhamento

        tabelaModel = new DefaultTableModel(new String[]{"ID", "Nome", "CPF"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaFuncionarios = new JTable(tabelaModel);
        JScrollPane scrollPane = new JScrollPane(tabelaFuncionarios);

        setLayout(new BorderLayout(10, 10));
        add(painelFormulario, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void salvarFuncionario() {
        try {
            Funcionario funcionario = new Funcionario();
            funcionario.setNome(tfNome.getText());
            funcionario.setCpf(tfCpf.getText());

            funcionarioService.cadastrarFuncionario(funcionario);

            JOptionPane.showMessageDialog(this, "Funcionário cadastrado com sucesso!");
            limparFormulario();
            carregarFuncionarios();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro no banco: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarFuncionarios() {
        try {
            List<Funcionario> funcionarios = funcionarioService.listarFuncionarios();
            tabelaModel.setRowCount(0);
            for (Funcionario f : funcionarios) {
                tabelaModel.addRow(new Object[]{
                        f.getFuncionarioId(),
                        f.getNome(),
                        f.getCpf()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar funcionários: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        tfNome.setText("");
        tfCpf.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Connection connection = DriverManager.getConnection(System.getenv("DB_URL"), System.getenv("DB_USER"), System.getenv("DB_PASSWORD"));
                FuncionarioDAO funcionarioDAO = new FuncionarioDAO(connection);
                FuncionarioService funcionarioService = new FuncionarioService(funcionarioDAO);

                FuncionarioUI ui = new FuncionarioUI(funcionarioService);
                ui.setVisible(true);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Falha ao conectar com o banco: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}