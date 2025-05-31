package org.example.controle_vendas.ui;

import org.example.controle_vendas.dao.CategoriaDAO;
import org.example.controle_vendas.model.Categoria;
import org.example.controle_vendas.service.CategoriaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class CategoriaUI extends JFrame {
    private final CategoriaService categoriaService;

    private JTextField tfNome, tfDescricao;
    private JTable tabelaCategorias;
    private DefaultTableModel tabelaModel;

    public CategoriaUI(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
        initComponents();
        carregarCategorias();
    }

    private void initComponents() {
        setTitle("Cadastro de Categorias");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelFormulario = new JPanel(new GridLayout(3, 2, 5, 5));
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Nova Categoria"));

        painelFormulario.add(new JLabel("Nome:"));
        tfNome = new JTextField();
        painelFormulario.add(tfNome);

        painelFormulario.add(new JLabel("Descrição:"));
        tfDescricao = new JTextField();
        painelFormulario.add(tfDescricao);

        JButton btnSalvar = new JButton("Salvar Categoria");
        btnSalvar.addActionListener(e -> salvarCategoria());
        painelFormulario.add(btnSalvar);

        painelFormulario.add(new JLabel()); // espaço para alinhamento

        tabelaModel = new DefaultTableModel(new String[]{"ID", "Nome", "Descrição"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaCategorias = new JTable(tabelaModel);
        JScrollPane scrollPane = new JScrollPane(tabelaCategorias);

        setLayout(new BorderLayout(10, 10));
        add(painelFormulario, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void salvarCategoria() {
        try {
            Categoria categoria = new Categoria();
            categoria.setNome(tfNome.getText());
            categoria.setDescricao(tfDescricao.getText());

            categoriaService.cadastrarCategoria(categoria);

            JOptionPane.showMessageDialog(this, "Categoria cadastrada com sucesso!");
            limparFormulario();
            carregarCategorias();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro no banco: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarCategorias() {
        try {
            List<Categoria> categorias = categoriaService.listarCategorias();
            tabelaModel.setRowCount(0);
            for (Categoria c : categorias) {
                tabelaModel.addRow(new Object[]{
                        c.getCategoriaId(),
                        c.getNome(),
                        c.getDescricao()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar categorias: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        tfNome.setText("");
        tfDescricao.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Connection connection = DriverManager.getConnection(System.getenv("DB_URL"), System.getenv("DB_USER"), System.getenv("DB_PASSWORD"));
                CategoriaDAO categoriaDAO = new CategoriaDAO(connection);
                CategoriaService categoriaService = new CategoriaService(categoriaDAO);

                CategoriaUI ui = new CategoriaUI(categoriaService);
                ui.setVisible(true);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Falha ao conectar com o banco: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}