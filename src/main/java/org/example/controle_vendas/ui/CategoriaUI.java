package org.example.controle_vendas.ui;

import com.formdev.flatlaf.FlatClientProperties;
import org.example.controle_vendas.model.Categoria;
import org.example.controle_vendas.service.CategoriaService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CategoriaUI extends JFrame {
    private final CategoriaService categoriaService;
    private JFrame telaPrincipal;

    private JTextField tfNome, tfDescricao;
    private JTable tabelaCategorias;
    private DefaultTableModel tabelaModel;
    private JButton btnSalvarAtualizar;
    private JButton btnEditar;
    private JButton btnInativar; // RENOMEADO para "Inativar"
    private JButton btnLimpar;

    private Categoria categoriaEmEdicao;

    public CategoriaUI(CategoriaService categoriaService, JFrame telaPrincipal) {
        this.categoriaService = categoriaService;
        this.telaPrincipal = telaPrincipal;
        this.categoriaEmEdicao = null;

        initComponents();
        carregarCategorias();
        atualizarBotoesAcao();
    }

    private void initComponents() {
        setTitle("Cadastro e Gerenciamento de Categorias");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (telaPrincipal != null) {
                    telaPrincipal.setVisible(true);
                }
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(10, 10, 10, 10),
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY)
                ),
                "Detalhes da Categoria", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), Color.LIGHT_GRAY
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        painelFormulario.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        tfNome = new JTextField();
        tfNome.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Digite o nome da categoria");
        painelFormulario.add(tfNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        painelFormulario.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        tfDescricao = new JTextField();
        tfDescricao.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Breve descrição da categoria");
        painelFormulario.add(tfDescricao, gbc);

        JPanel painelBotoesFormulario = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));

        btnSalvarAtualizar = new JButton("Salvar Categoria");
        btnSalvarAtualizar.putClientProperty(FlatClientProperties.BUTTON_TYPE, "roundRect");
        btnSalvarAtualizar.addActionListener(e -> salvarAtualizarCategoria());
        painelBotoesFormulario.add(btnSalvarAtualizar);

        btnLimpar = new JButton("Limpar Campos");
        btnLimpar.putClientProperty(FlatClientProperties.BUTTON_TYPE, "roundRect");
        btnLimpar.addActionListener(e -> limparFormulario());
        painelBotoesFormulario.add(btnLimpar);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 5, 5, 5);
        painelFormulario.add(painelBotoesFormulario, gbc);

        mainPanel.add(painelFormulario, BorderLayout.NORTH);

        tabelaModel = new DefaultTableModel(new String[]{"ID", "Nome", "Descrição"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaCategorias = new JTable(tabelaModel);
        tabelaCategorias.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabelaCategorias.getColumnModel().getColumn(0).setMaxWidth(80);
        tabelaCategorias.getColumnModel().getColumn(0).setMinWidth(50);

        JScrollPane scrollPane = new JScrollPane(tabelaCategorias);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Categorias Cadastradas (Ativas)")); // Título ajustado

        tabelaCategorias.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    atualizarBotoesAcao();
                }
            }
        });

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel painelBotoesTabela = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        btnEditar = new JButton("Editar Categoria");
        btnEditar.putClientProperty(FlatClientProperties.BUTTON_TYPE, "roundRect");
        btnEditar.addActionListener(e -> editarCategoria());
        painelBotoesTabela.add(btnEditar);

        // RENOMEADO DE btnExcluir para btnInativar
        btnInativar = new JButton("Inativar Categoria");
        btnInativar.putClientProperty(FlatClientProperties.BUTTON_TYPE, "roundRect");
        btnInativar.addActionListener(e -> inativarCategoria()); // MUDANÇA: Chama inativarCategoria()
        painelBotoesTabela.add(btnInativar);

        mainPanel.add(painelBotoesTabela, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void salvarAtualizarCategoria() {
        String nome = tfNome.getText().trim();
        String descricao = tfDescricao.getText().trim();

        if (nome.isEmpty()) {
            showWarning("O campo 'Nome' não pode estar vazio.");
            tfNome.requestFocusInWindow();
            return;
        }

        try {
            if (categoriaEmEdicao == null) {
                Categoria novaCategoria = new Categoria();
                novaCategoria.setNome(nome);
                novaCategoria.setDescricao(descricao);
                categoriaService.cadastrarCategoria(novaCategoria);
                showInfo("Categoria cadastrada com sucesso!");
            } else {
                categoriaEmEdicao.setNome(nome);
                categoriaEmEdicao.setDescricao(descricao);
                categoriaService.atualizarCategoria(categoriaEmEdicao);
                showInfo("Categoria atualizada com sucesso!");
            }
            limparFormulario();
            carregarCategorias();
        } catch (IllegalArgumentException e) {
            showWarning("Erro de validação: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            showError("Erro no banco de dados: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showError("Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void editarCategoria() {
        int selectedRow = tabelaCategorias.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Selecione uma categoria na tabela para editar.");
            return;
        }

        int categoriaId = (Integer) tabelaModel.getValueAt(selectedRow, 0);

        try {
            Categoria categoria = categoriaService.buscarPorId(categoriaId);
            if (categoria != null) {
                categoriaEmEdicao = categoria;
                tfNome.setText(categoria.getNome());
                tfDescricao.setText(categoria.getDescricao());
                btnSalvarAtualizar.setText("Atualizar Categoria");
                atualizarBotoesAcao();
            } else {
                showError("Categoria não encontrada.");
                limparFormulario();
            }
        } catch (SQLException e) {
            showError("Erro ao buscar categoria para edição: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // RENOMEADO DE excluirCategoria para inativarCategoria
    private void inativarCategoria() {
        int selectedRow = tabelaCategorias.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Selecione uma categoria na tabela para inativar.");
            return;
        }

        int categoriaId = (Integer) tabelaModel.getValueAt(selectedRow, 0);
        String categoriaNome = (String) tabelaModel.getValueAt(selectedRow, 1);

        int confirmResult = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja INATIVAR a categoria '" + categoriaNome + "'?\n" +
                        "Esta ação irá ocultá-la das listas ativas, mas não a removerá permanentemente.",
                "Confirmar Inativação", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmResult == JOptionPane.YES_OPTION) {
            try {
                categoriaService.inativarCategoria(categoriaId); // MUDANÇA: Chama inativarCategoria() do Service
                showInfo("Categoria inativada com sucesso!");
                carregarCategorias();
                limparFormulario();
            } catch (SQLException e) {
                showError("Erro ao inativar categoria: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                showError("Ocorreu um erro inesperado ao inativar: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void carregarCategorias() {
        try {
            List<Categoria> categorias = categoriaService.listarCategorias(); // Este método já filtra por 'Ativo'
            tabelaModel.setRowCount(0);
            for (Categoria c : categorias) {
                tabelaModel.addRow(new Object[]{
                        c.getCategoriaId(),
                        c.getNome(),
                        c.getDescricao()
                });
            }
        } catch (SQLException e) {
            showError("Erro ao carregar categorias: " + e.getMessage());
            e.printStackTrace();
        } finally {
            atualizarBotoesAcao();
        }
    }

    private void limparFormulario() {
        tfNome.setText("");
        tfDescricao.setText("");
        categoriaEmEdicao = null;
        btnSalvarAtualizar.setText("Salvar Categoria");
        tabelaCategorias.clearSelection();
        atualizarBotoesAcao();
    }

    private void atualizarBotoesAcao() {
        boolean rowSelected = tabelaCategorias.getSelectedRow() != -1;
        btnEditar.setEnabled(rowSelected && categoriaEmEdicao == null);
        btnInativar.setEnabled(rowSelected); // Habilita inativar se houver seleção

        if (categoriaEmEdicao != null) {
            btnSalvarAtualizar.setText("Atualizar Categoria");
            btnEditar.setEnabled(false);
        } else {
            btnSalvarAtualizar.setText("Salvar Categoria");
        }

        if (tabelaModel.getRowCount() == 0) {
            btnEditar.setEnabled(false);
            btnInativar.setEnabled(false);
        }
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}