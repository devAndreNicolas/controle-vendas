package org.example.controle_vendas.ui;

import com.formdev.flatlaf.FlatClientProperties; // Importar para estilos FlatLaf
import org.example.controle_vendas.model.Cliente;
import org.example.controle_vendas.service.ClienteService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ClienteUI extends JFrame {
    private final ClienteService clienteService;
    private JFrame telaPrincipal;

    private JTextField tfNome, tfCpfCnpj, tfEmail, tfTelefone, tfEndereco;
    private JTable tabelaClientes;
    private DefaultTableModel tabelaModel;
    private JButton btnSalvarAtualizar;
    private JButton btnEditar;
    private JButton btnInativar;
    private JButton btnLimpar;

    private Cliente clienteEmEdicao;

    public ClienteUI(ClienteService clienteService, JFrame telaPrincipal) {
        this.clienteService = clienteService;
        this.telaPrincipal = telaPrincipal;
        this.clienteEmEdicao = null;

        initComponents();
        carregarClientes();
        atualizarBotoesAcao();
    }

    private void initComponents() {
        setTitle("Cadastro e Gerenciamento de Clientes");
        setSize(1440, 900);
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
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15)); // Espaçamento maior
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Padding externo

        // ====================================================================================
        // PAINEL DE FORMULÁRIO (Usando GridBagLayout para flexibilidade)
        // ====================================================================================
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(10, 10, 10, 10),
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY)
                ),
                "Detalhes do Cliente", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), Color.LIGHT_GRAY
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaçamento entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL; // Faz o componente preencher o espaço horizontal

        // Linha 0: Nome
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        painelFormulario.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        tfNome = new JTextField();
        tfNome.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nome completo do cliente");
        painelFormulario.add(tfNome, gbc);

        // Linha 1: CPF/CNPJ
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        painelFormulario.add(new JLabel("CPF/CNPJ:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        tfCpfCnpj = new JTextField();
        tfCpfCnpj.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ex: 123.456.789-00 ou 12.345.678/0001-90");
        painelFormulario.add(tfCpfCnpj, gbc);

        // Linha 2: Email
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        painelFormulario.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        tfEmail = new JTextField();
        tfEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "email@exemplo.com");
        painelFormulario.add(tfEmail, gbc);

        // Linha 3: Telefone
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        painelFormulario.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        tfTelefone = new JTextField();
        tfTelefone.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ex: (XX) XXXX-XXXX");
        painelFormulario.add(tfTelefone, gbc);

        // Linha 4: Endereço
        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST;
        painelFormulario.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1.0;
        tfEndereco = new JTextField();
        tfEndereco.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Rua, Número, Bairro, Cidade - UF");
        painelFormulario.add(tfEndereco, gbc);

        // ====================================================================================
        // PAINEL DE BOTÕES DE AÇÃO DO FORMULÁRIO
        // ====================================================================================
        JPanel painelBotoesFormulario = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));

        btnSalvarAtualizar = new JButton("Salvar Cliente");
        btnSalvarAtualizar.putClientProperty(FlatClientProperties.BUTTON_TYPE, "roundRect");
        btnSalvarAtualizar.addActionListener(e -> salvarAtualizarCliente());
        painelBotoesFormulario.add(btnSalvarAtualizar);

        btnLimpar = new JButton("Limpar Campos");
        btnLimpar.putClientProperty(FlatClientProperties.BUTTON_TYPE, "roundRect");
        btnLimpar.addActionListener(e -> limparFormulario());
        painelBotoesFormulario.add(btnLimpar);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 5, 5, 5);
        painelFormulario.add(painelBotoesFormulario, gbc);

        mainPanel.add(painelFormulario, BorderLayout.NORTH);

        // ====================================================================================
        // TABELA DE CLIENTES
        // ====================================================================================
        tabelaModel = new DefaultTableModel(new String[]{"ID", "Nome", "CPF/CNPJ", "Email", "Telefone", "Endereço"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaClientes = new JTable(tabelaModel);
        // Ajustar largura da coluna ID
        tabelaClientes.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabelaClientes.getColumnModel().getColumn(0).setMaxWidth(80);
        tabelaClientes.getColumnModel().getColumn(0).setMinWidth(50);

        JScrollPane scrollPane = new JScrollPane(tabelaClientes);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Clientes Cadastrados (Ativos)"));

        tabelaClientes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
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

        btnEditar = new JButton("Editar Cliente");
        btnEditar.putClientProperty(FlatClientProperties.BUTTON_TYPE, "roundRect");
        btnEditar.addActionListener(e -> editarCliente());
        painelBotoesTabela.add(btnEditar);

        btnInativar = new JButton("Inativar Cliente");
        btnInativar.putClientProperty(FlatClientProperties.BUTTON_TYPE, "roundRect");
        btnInativar.addActionListener(e -> inativarCliente());
        painelBotoesTabela.add(btnInativar);

        mainPanel.add(painelBotoesTabela, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // --- MÉTODOS DE LÓGICA DE NEGÓCIO E CONTROLE DA UI ---

    private void salvarAtualizarCliente() {
        String nome = tfNome.getText().trim();
        String cpfCnpj = tfCpfCnpj.getText().trim();
        String email = tfEmail.getText().trim();
        String telefone = tfTelefone.getText().trim();
        String endereco = tfEndereco.getText().trim();

        try {
            Cliente cliente = (clienteEmEdicao == null) ? new Cliente() : clienteEmEdicao;
            cliente.setNome(nome);
            cliente.setCpfCnpj(cpfCnpj);
            cliente.setEmail(email);
            cliente.setTelefone(telefone);
            cliente.setEndereco(endereco);

            if (clienteEmEdicao == null) {
                clienteService.cadastrarCliente(cliente);
                showInfo("Cliente cadastrado com sucesso!");
            } else {
                clienteService.atualizarCliente(cliente);
                showInfo("Cliente atualizado com sucesso!");
            }
            limparFormulario();
            carregarClientes();
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

    private void editarCliente() {
        int selectedRow = tabelaClientes.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Selecione um cliente na tabela para editar.");
            return;
        }

        int clienteId = (Integer) tabelaModel.getValueAt(selectedRow, 0);

        try {
            Cliente cliente = clienteService.buscarPorId(clienteId);
            if (cliente != null) {
                clienteEmEdicao = cliente;
                tfNome.setText(cliente.getNome());
                tfCpfCnpj.setText(cliente.getCpfCnpj());
                tfEmail.setText(cliente.getEmail());
                tfTelefone.setText(cliente.getTelefone());
                tfEndereco.setText(cliente.getEndereco());

                btnSalvarAtualizar.setText("Atualizar Cliente");
                atualizarBotoesAcao();
            } else {
                showError("Cliente não encontrado.");
                limparFormulario();
            }
        } catch (SQLException e) {
            showError("Erro ao buscar cliente para edição: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void inativarCliente() {
        int selectedRow = tabelaClientes.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Selecione um cliente na tabela para inativar.");
            return;
        }

        int clienteId = (Integer) tabelaModel.getValueAt(selectedRow, 0);
        String clienteNome = (String) tabelaModel.getValueAt(selectedRow, 1);

        int confirmResult = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja INATIVAR o cliente '" + clienteNome + "'?\n" +
                        "Esta ação irá ocultá-lo das listas ativas, mas não o removerá permanentemente.",
                "Confirmar Inativação", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmResult == JOptionPane.YES_OPTION) {
            try {
                clienteService.inativarCliente(clienteId);
                showInfo("Cliente inativado com sucesso!");
                carregarClientes();
                limparFormulario();
            } catch (SQLException e) {
                showError("Erro ao inativar cliente: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                showError("Ocorreu um erro inesperado ao inativar: " + e.getMessage());
                e.printStackTrace();
            }
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
            showError("Erro ao carregar clientes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            atualizarBotoesAcao();
        }
    }

    private void limparFormulario() {
        tfNome.setText("");
        tfCpfCnpj.setText("");
        tfEmail.setText("");
        tfTelefone.setText("");
        tfEndereco.setText("");
        clienteEmEdicao = null;
        btnSalvarAtualizar.setText("Salvar Cliente");
        tabelaClientes.clearSelection();
        atualizarBotoesAcao();
    }

    private void atualizarBotoesAcao() {
        boolean rowSelected = tabelaClientes.getSelectedRow() != -1;
        btnEditar.setEnabled(rowSelected && clienteEmEdicao == null);
        btnInativar.setEnabled(rowSelected);

        if (clienteEmEdicao != null) {
            btnSalvarAtualizar.setText("Atualizar Cliente");
            btnEditar.setEnabled(false);
        } else {
            btnSalvarAtualizar.setText("Salvar Cliente");
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