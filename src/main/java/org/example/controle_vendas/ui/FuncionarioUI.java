package org.example.controle_vendas.ui;

import com.formdev.flatlaf.FlatClientProperties; // Importar para estilos FlatLaf
import org.example.controle_vendas.dao.FuncionarioDAO; // Mantenha, necessário para o serviço
import org.example.controle_vendas.model.Funcionario;
import org.example.controle_vendas.service.FuncionarioService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class FuncionarioUI extends JFrame {
    private final FuncionarioService funcionarioService;
    private JFrame telaPrincipal;

    private JTextField tfNome, tfCpf;
    private JTable tabelaFuncionarios;
    private DefaultTableModel tabelaModel;
    private JButton btnSalvarAtualizar; // Botão que muda de texto (Salvar/Atualizar)
    private JButton btnEditar;
    private JButton btnInativar; // RENOMEADO para "Inativar"
    private JButton btnLimpar; // Novo botão Limpar

    private Funcionario funcionarioEmEdicao; // Novo: Para controlar o funcionário sendo editado

    public FuncionarioUI(FuncionarioService funcionarioService, JFrame telaPrincipal) {
        this.funcionarioService = funcionarioService;
        this.telaPrincipal = telaPrincipal;
        this.funcionarioEmEdicao = null; // Inicializa sem funcionário em edição

        initComponents();
        carregarFuncionarios();
        atualizarBotoesAcao(); // Atualiza o estado dos botões ao iniciar
    }

    private void initComponents() {
        setTitle("Cadastro e Gerenciamento de Funcionários"); // Título mais descritivo
        setSize(700, 500); // Aumentar tamanho para melhor visualização
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

        // ====================================================================================
        // PAINEL PRINCIPAL com BorderLayout e padding
        // ====================================================================================
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // ====================================================================================
        // PAINEL DE FORMULÁRIO (Usando GridBagLayout para flexibilidade)
        // ====================================================================================
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(10, 10, 10, 10),
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY)
                ),
                "Detalhes do Funcionário", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), Color.DARK_GRAY
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0: Nome
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        painelFormulario.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        tfNome = new JTextField();
        tfNome.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nome completo do funcionário");
        painelFormulario.add(tfNome, gbc);

        // Linha 1: CPF
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        painelFormulario.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        tfCpf = new JTextField();
        tfCpf.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ex: 123.456.789-00");
        painelFormulario.add(tfCpf, gbc);

        // ====================================================================================
        // PAINEL DE BOTÕES DE AÇÃO DO FORMULÁRIO
        // ====================================================================================
        JPanel painelBotoesFormulario = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));

        btnSalvarAtualizar = new JButton("Salvar Funcionário");
        btnSalvarAtualizar.putClientProperty(FlatClientProperties.BUTTON_TYPE, "roundRect");
        btnSalvarAtualizar.addActionListener(e -> salvarAtualizarFuncionario());
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

        // ====================================================================================
        // TABELA DE FUNCIONÁRIOS
        // ====================================================================================
        tabelaModel = new DefaultTableModel(new String[]{"ID", "Nome", "CPF"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaFuncionarios = new JTable(tabelaModel);
        tabelaFuncionarios.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabelaFuncionarios.getColumnModel().getColumn(0).setMaxWidth(80);
        tabelaFuncionarios.getColumnModel().getColumn(0).setMinWidth(50);

        JScrollPane scrollPane = new JScrollPane(tabelaFuncionarios);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Funcionários Cadastrados (Ativos)"));

        tabelaFuncionarios.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    atualizarBotoesAcao();
                }
            }
        });

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ====================================================================================
        // PAINEL DE BOTÕES DE AÇÃO DA TABELA (Editar/Inativar)
        // ====================================================================================
        JPanel painelBotoesTabela = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        btnEditar = new JButton("Editar Funcionário");
        btnEditar.putClientProperty(FlatClientProperties.BUTTON_TYPE, "roundRect");
        btnEditar.addActionListener(e -> editarFuncionario());
        painelBotoesTabela.add(btnEditar);

        btnInativar = new JButton("Inativar Funcionário");
        btnInativar.putClientProperty(FlatClientProperties.BUTTON_TYPE, "roundRect");
        btnInativar.addActionListener(e -> inativarFuncionario());
        painelBotoesTabela.add(btnInativar);

        mainPanel.add(painelBotoesTabela, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // --- MÉTODOS DE LÓGICA DE NEGÓCIO E CONTROLE DA UI ---

    private void salvarAtualizarFuncionario() {
        String nome = tfNome.getText().trim();
        String cpf = tfCpf.getText().trim();

        try {
            Funcionario funcionario = (funcionarioEmEdicao == null) ? new Funcionario() : funcionarioEmEdicao;
            funcionario.setNome(nome);
            funcionario.setCpf(cpf);

            if (funcionarioEmEdicao == null) {
                funcionarioService.cadastrarFuncionario(funcionario);
                showInfo("Funcionário cadastrado com sucesso!");
            } else {
                funcionarioService.atualizarFuncionario(funcionario);
                showInfo("Funcionário atualizado com sucesso!");
            }
            limparFormulario();
            carregarFuncionarios();
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

    private void editarFuncionario() {
        int selectedRow = tabelaFuncionarios.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Selecione um funcionário na tabela para editar.");
            return;
        }

        int funcionarioId = (Integer) tabelaModel.getValueAt(selectedRow, 0);

        try {
            Funcionario funcionario = funcionarioService.buscarPorId(funcionarioId);
            if (funcionario != null) {
                funcionarioEmEdicao = funcionario;
                tfNome.setText(funcionario.getNome());
                tfCpf.setText(funcionario.getCpf());

                btnSalvarAtualizar.setText("Atualizar Funcionário");
                atualizarBotoesAcao();
            } else {
                showError("Funcionário não encontrado.");
                limparFormulario();
            }
        } catch (SQLException e) {
            showError("Erro ao buscar funcionário para edição: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void inativarFuncionario() {
        int selectedRow = tabelaFuncionarios.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Selecione um funcionário na tabela para inativar.");
            return;
        }

        int funcionarioId = (Integer) tabelaModel.getValueAt(selectedRow, 0);
        String funcionarioNome = (String) tabelaModel.getValueAt(selectedRow, 1);

        int confirmResult = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja INATIVAR o funcionário '" + funcionarioNome + "'?\n" +
                        "Esta ação irá ocultá-lo das listas ativas, mas não o removerá permanentemente.",
                "Confirmar Inativação", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmResult == JOptionPane.YES_OPTION) {
            try {
                funcionarioService.inativarFuncionario(funcionarioId);
                showInfo("Funcionário inativado com sucesso!");
                carregarFuncionarios();
                limparFormulario();
            } catch (SQLException e) {
                showError("Erro ao inativar funcionário: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                showError("Ocorreu um erro inesperado ao inativar: " + e.getMessage());
                e.printStackTrace();
            }
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
            showError("Erro ao carregar funcionários: " + e.getMessage());
            e.printStackTrace();
        } finally {
            atualizarBotoesAcao();
        }
    }

    private void limparFormulario() {
        tfNome.setText("");
        tfCpf.setText("");
        funcionarioEmEdicao = null;
        btnSalvarAtualizar.setText("Salvar Funcionário");
        tabelaFuncionarios.clearSelection();
        atualizarBotoesAcao();
    }

    private void atualizarBotoesAcao() {
        boolean rowSelected = tabelaFuncionarios.getSelectedRow() != -1;
        btnEditar.setEnabled(rowSelected && funcionarioEmEdicao == null);
        btnInativar.setEnabled(rowSelected);

        if (funcionarioEmEdicao != null) {
            btnSalvarAtualizar.setText("Atualizar Funcionário");
            btnEditar.setEnabled(false);
        } else {
            btnSalvarAtualizar.setText("Salvar Funcionário");
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