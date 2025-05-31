package org.example.controle_vendas.ui;

import org.example.controle_vendas.dao.ProdutoDAO;
import org.example.controle_vendas.model.Produto;
import org.example.controle_vendas.service.ProdutoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class ProdutoUI extends JFrame {
    private final ProdutoService produtoService;

    private JTextField tfCategoriaId, tfNomeProduto, tfPrecoVenda, tfPrecoCusto, tfUnidadeMedida;
    private JTable tabelaProdutos;
    private DefaultTableModel tabelaModel;

    public ProdutoUI(ProdutoService produtoService) {
        this.produtoService = produtoService;
        initComponents();
        carregarProdutos();
    }

    private void initComponents() {
        setTitle("Cadastro de Produtos");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelFormulario = new JPanel(new GridLayout(6, 2, 5, 5));
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Novo Produto"));

        painelFormulario.add(new JLabel("ID Categoria:"));
        tfCategoriaId = new JTextField();
        painelFormulario.add(tfCategoriaId);

        painelFormulario.add(new JLabel("Nome Produto:"));
        tfNomeProduto = new JTextField();
        painelFormulario.add(tfNomeProduto);

        painelFormulario.add(new JLabel("Preço Venda:"));
        tfPrecoVenda = new JTextField();
        painelFormulario.add(tfPrecoVenda);

        painelFormulario.add(new JLabel("Preço Custo:"));
        tfPrecoCusto = new JTextField();
        painelFormulario.add(tfPrecoCusto);

        painelFormulario.add(new JLabel("Unidade Medida:"));
        tfUnidadeMedida = new JTextField();
        painelFormulario.add(tfUnidadeMedida);

        JButton btnSalvar = new JButton("Salvar Produto");
        btnSalvar.addActionListener(e -> salvarProduto());
        painelFormulario.add(btnSalvar);

        painelFormulario.add(new JLabel()); // espaço

        tabelaModel = new DefaultTableModel(new String[]{"ID", "Categoria ID", "Nome", "Preço Venda", "Preço Custo", "Unidade Medida"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaProdutos = new JTable(tabelaModel);
        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);

        setLayout(new BorderLayout(10, 10));
        add(painelFormulario, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void salvarProduto() {
        try {
            Produto produto = new Produto();
            produto.setCategoriaId(Integer.parseInt(tfCategoriaId.getText()));
            produto.setNomeProduto(tfNomeProduto.getText());
            produto.setPrecoVenda(Integer.parseInt(tfPrecoVenda.getText()));
            produto.setPrecoCusto(Integer.parseInt(tfPrecoCusto.getText()));
            produto.setUnidadeMedida(tfUnidadeMedida.getText());

            produtoService.cadastrarProduto(produto);

            JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
            limparFormulario();
            carregarProdutos();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valores numéricos inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro no banco: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarProdutos() {
        try {
            List<Produto> produtos = produtoService.listarProdutos();
            tabelaModel.setRowCount(0);
            for (Produto p : produtos) {
                tabelaModel.addRow(new Object[]{
                        p.getProdutoId(),
                        p.getCategoriaId(),
                        p.getNomeProduto(),
                        p.getPrecoVenda(),
                        p.getPrecoCusto(),
                        p.getUnidadeMedida()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        tfCategoriaId.setText("");
        tfNomeProduto.setText("");
        tfPrecoVenda.setText("");
        tfPrecoCusto.setText("");
        tfUnidadeMedida.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Connection connection = DriverManager.getConnection(System.getenv("DB_URL"), System.getenv("DB_USER"), System.getenv("DB_PASSWORD"));
                ProdutoDAO produtoDAO = new ProdutoDAO(connection);
                ProdutoService produtoService = new ProdutoService(produtoDAO);

                ProdutoUI ui = new ProdutoUI(produtoService);
                ui.setVisible(true);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Falha ao conectar com o banco: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}