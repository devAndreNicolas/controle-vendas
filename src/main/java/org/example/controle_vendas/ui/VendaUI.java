package org.example.controle_vendas.ui;

import com.formdev.flatlaf.FlatClientProperties;
import org.example.controle_vendas.dao.ClienteDAO;
import org.example.controle_vendas.dao.FuncionarioDAO;
import org.example.controle_vendas.dao.ProdutoDAO;
import org.example.controle_vendas.dao.VendaDAO;
import org.example.controle_vendas.model.Cliente;
import org.example.controle_vendas.model.Funcionario;
import org.example.controle_vendas.model.ItemVenda;
import org.example.controle_vendas.model.Produto;
import org.example.controle_vendas.model.Venda;
import org.example.controle_vendas.service.ClienteService;
import org.example.controle_vendas.service.FuncionarioService;
import org.example.controle_vendas.service.ProdutoService;
import org.example.controle_vendas.service.VendaService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class VendaUI extends JFrame {
    private final VendaService vendaService;
    private final ProdutoService produtoService;
    private final ClienteService clienteService;
    private final FuncionarioService funcionarioService;
    private JFrame telaPrincipal;

    private JComboBox<Cliente> cbClientes;
    private JComboBox<Funcionario> cbFuncionarios;
    private JComboBox<Produto> cbProdutos;
    private JSpinner spQuantidade;
    private JButton btnAddItem;
    private JLabel lblPrecoUnitario;
    private JLabel lblTotalVendaAtual;

    private JSpinner spDataConsulta;
    private JButton btnConsultarTotalVendas;
    private JLabel lblTotalVendasPorData;

    private JTable tabelaItens;
    private DefaultTableModel tabelaModelItens;
    private JButton btnSalvarVenda;
    private JTable tabelaVendas;
    private DefaultTableModel tabelaModelVendas;

    private Venda vendaAtual;
    private Map<Integer, Produto> produtosMap;

    public VendaUI(Connection connection, JFrame telaPrincipal) {
        this.vendaService = new VendaService(new VendaDAO(connection));
        this.funcionarioService = new FuncionarioService(new FuncionarioDAO(connection));
        this.produtoService = new ProdutoService(new ProdutoDAO(connection));
        this.clienteService = new ClienteService(new ClienteDAO(connection));
        this.telaPrincipal = telaPrincipal;

        this.vendaAtual = new Venda();
        this.produtosMap = new HashMap<>();

        initComponents();
        carregarComboClientes();
        carregarComboFuncionarios();
        carregarComboProdutos();
        carregarVendas();
        atualizarTotalVendaAtual();
        exibirPrecoProdutoSelecionado();
    }

    private void initComponents() {
        setTitle("Registro de Vendas");
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

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // --- PAINEL DE CONSULTA DE TOTAL DE VENDAS (TOPO) ---
        JPanel painelConsultaTotalVendas = criarPainelConsultaTotalVendas();
        mainPanel.add(painelConsultaTotalVendas, BorderLayout.NORTH);

        // --- PAINEL DE NOVA VENDA (ESQUERDA) ---
        JPanel painelNovaVenda = criarPainelNovaVenda();
        mainPanel.add(painelNovaVenda, BorderLayout.WEST);

        // --- PAINEL CENTRAL COM TABELAS ---
        JPanel painelCentro = criarPainelCentroComTabelas();
        mainPanel.add(painelCentro, BorderLayout.CENTER);

        add(mainPanel);
    }

    // Método para criar o painel de consulta (topo)
    private JPanel criarPainelConsultaTotalVendas() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));

        Font fonteTitulo = new Font("Segoe UI", Font.BOLD, 16);
        Font fonteTexto = new Font("Segoe UI", Font.PLAIN, 14);

        painel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                "Consulta: Total de Vendas por Data",
                TitledBorder.LEFT, TitledBorder.TOP,
                fonteTitulo,
                Color.DARK_GRAY
        ));

        JLabel lblData = new JLabel("Data:");
        lblData.setFont(fonteTexto);
        painel.add(lblData);

        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        spDataConsulta = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spDataConsulta, "yyyy-MM-dd");
        spDataConsulta.setEditor(dateEditor);
        spDataConsulta.setPreferredSize(new Dimension(130, 30));
        painel.add(spDataConsulta);

        btnConsultarTotalVendas = new JButton("Consultar");
        btnConsultarTotalVendas.putClientProperty(FlatClientProperties.BUTTON_TYPE, "roundRect");
        btnConsultarTotalVendas.setFont(fonteTexto);
        btnConsultarTotalVendas.setPreferredSize(new Dimension(130, 30));
        btnConsultarTotalVendas.addActionListener(e -> consultarTotalVendasPorData());
        painel.add(btnConsultarTotalVendas);

        lblTotalVendasPorData = new JLabel("Total de vendas: -");
        lblTotalVendasPorData.setFont(fonteTexto);
        lblTotalVendasPorData.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        painel.add(lblTotalVendasPorData);

        return painel;
    }

    // Método para criar o painel de nova venda (esquerda)
    private JPanel criarPainelNovaVenda() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(10, 10, 10, 10),
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY)
                ),
                "Nova Venda e Itens", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), Color.LIGHT_GRAY
        ));
        painel.setPreferredSize(new Dimension(500, painel.getPreferredSize().height));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Cliente
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        painel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        cbClientes = new JComboBox<>();
        painel.add(cbClientes, gbc);

        // Funcionário
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.anchor = GridBagConstraints.EAST;
        painel.add(new JLabel("Funcionário:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        cbFuncionarios = new JComboBox<>();
        painel.add(cbFuncionarios, gbc);

        // Produto
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.anchor = GridBagConstraints.EAST;
        painel.add(new JLabel("Produto:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        cbProdutos = new JComboBox<>();
        cbProdutos.addActionListener(e -> exibirPrecoProdutoSelecionado());
        painel.add(cbProdutos, gbc);

        // Preço unitário
        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0; gbc.anchor = GridBagConstraints.WEST;
        lblPrecoUnitario = new JLabel("Preço Unit.: R$ 0,00");
        painel.add(lblPrecoUnitario, gbc);

        // Quantidade
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; gbc.anchor = GridBagConstraints.EAST;
        painel.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 999, 1);
        spQuantidade = new JSpinner(spinnerModel);
        ((JSpinner.DefaultEditor) spQuantidade.getEditor()).getTextField().setEditable(true);
        painel.add(spQuantidade, gbc);

        // Botões de ação para itens
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        JPanel itemButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));

        btnAddItem = new JButton("Adicionar Item");
        btnAddItem.putClientProperty(FlatClientProperties.BUTTON_TYPE, "roundRect");
        btnAddItem.addActionListener(e -> adicionarItem());
        itemButtonsPanel.add(btnAddItem);

        JButton btnRemoverItem = new JButton("Remover Item Selecionado");
        btnRemoverItem.putClientProperty(FlatClientProperties.BUTTON_TYPE, "roundRect");
        btnRemoverItem.addActionListener(e -> removerItemSelecionado());
        itemButtonsPanel.add(btnRemoverItem);

        JButton btnProdutoMaisVendido = new JButton("Verificar Produto Mais Vendido");
        btnProdutoMaisVendido.putClientProperty(FlatClientProperties.BUTTON_TYPE, "roundRect");
        btnProdutoMaisVendido.addActionListener(e -> mostrarProdutoMaisVendido());
        itemButtonsPanel.add(btnProdutoMaisVendido);

        painel.add(itemButtonsPanel, gbc);

        // Total da venda atual
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(20, 5, 5, 5);
        lblTotalVendaAtual = new JLabel("TOTAL DA VENDA: R$ 0,00");
        lblTotalVendaAtual.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotalVendaAtual.setForeground(Color.GREEN);
        painel.add(lblTotalVendaAtual, gbc);

        // Botão salvar venda
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 5, 5, 5);
        btnSalvarVenda = new JButton("FINALIZAR VENDA");
        btnSalvarVenda.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnSalvarVenda.putClientProperty(FlatClientProperties.BUTTON_TYPE, "square");
        btnSalvarVenda.addActionListener(e -> salvarVenda());
        painel.add(btnSalvarVenda, gbc);

        return painel;
    }

    // Método para criar painel central com as tabelas
    private JPanel criarPainelCentroComTabelas() {
        tabelaModelItens = new DefaultTableModel(new String[]{"Produto", "Quantidade", "Preço Unit.", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabelaItens = new JTable(tabelaModelItens);
        configurarRenderizadorTabelaItens();
        JScrollPane spItens = new JScrollPane(tabelaItens);
        spItens.setBorder(BorderFactory.createTitledBorder("Itens da Venda Atual"));

        tabelaModelVendas = new DefaultTableModel(new String[]{"Cliente", "CPF/CNPJ", "Email", "Telefone", "Funcionário", "Data", "Produto", "Valor Total", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabelaVendas = new JTable(tabelaModelVendas);
        configurarRenderizadorTabelaVendas();
        JScrollPane spVendas = new JScrollPane(tabelaVendas);
        spVendas.setBorder(BorderFactory.createTitledBorder("Histórico de Vendas"));

        JSplitPane splitPaneTabelas = new JSplitPane(JSplitPane.VERTICAL_SPLIT, spItens, spVendas);
        splitPaneTabelas.setResizeWeight(0.5);
        splitPaneTabelas.setDividerSize(8);

        JPanel painelCentro = new JPanel(new BorderLayout(0, 10));
        painelCentro.add(splitPaneTabelas, BorderLayout.CENTER);

        return painelCentro;
    }

    // --- MÉTODOS AUXILIARES PARA UI/UX ---

    private void exibirPrecoProdutoSelecionado() {
        Produto produto = (Produto) cbProdutos.getSelectedItem();
        if (produto != null) {
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            lblPrecoUnitario.setText("Preço Unit.: " + currencyFormat.format(produto.getPrecoVenda()));
        } else {
            lblPrecoUnitario.setText("Preço Unit.: R$ 0,00");
        }
    }

    private void atualizarTotalVendaAtual() {
        double total = 0.0;
        if (vendaAtual != null && vendaAtual.getItens() != null) {
            for (ItemVenda item : vendaAtual.getItens()) {
                total += item.getSubtotalItem();
            }
        }
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        lblTotalVendaAtual.setText("TOTAL DA VENDA: " + currencyFormat.format(total));
    }

    private void removerItemSelecionado() {
        int selectedRow = tabelaItens.getSelectedRow();
        if (selectedRow == -1) {
            showError("Selecione um item para remover da venda.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja realmente remover este item da venda atual?", "Confirmar Remoção",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (vendaAtual.getItens() != null && selectedRow < vendaAtual.getItens().size()) {
                vendaAtual.getItens().remove(selectedRow);
            }
            atualizarTabelaItens();
            atualizarTotalVendaAtual();
            JOptionPane.showMessageDialog(this, "Item removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void setupCurrencyRenderer(JTable table, int columnIndex) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Number) {
                    value = currencyFormat.format(value);
                }
                setHorizontalAlignment(SwingConstants.RIGHT);
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
        table.getColumnModel().getColumn(columnIndex).setCellRenderer(renderer);
    }

    private void setupDateRenderer(JTable table, int columnIndex) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Date) {
                    value = dateFormat.format((Date) value);
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
        table.getColumnModel().getColumn(columnIndex).setCellRenderer(renderer);
    }

    private void configurarRenderizadorTabelaItens() {
        setupCurrencyRenderer(tabelaItens, 2);
        setupCurrencyRenderer(tabelaItens, 3);
        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(JLabel.RIGHT);
        tabelaItens.getColumnModel().getColumn(1).setCellRenderer(rightAlign);
    }

    private void configurarRenderizadorTabelaVendas() {
        setupDateRenderer(tabelaVendas, 3);
        setupCurrencyRenderer(tabelaVendas, 4);
    }

    // --- MÉTODOS DE LÓGICA DE NEGÓCIO (Ajustados para o que existe) ---

    private void carregarComboClientes() {
        try {
            List<Cliente> clientes = clienteService.listarClientes();
            DefaultComboBoxModel<Cliente> model = new DefaultComboBoxModel<>();
            for (Cliente c : clientes) model.addElement(c);
            cbClientes.setModel(model);
        } catch (SQLException e) {
            showError("Erro ao carregar clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void carregarComboFuncionarios() {
        try {
            List<Funcionario> funcionarios = funcionarioService.listarFuncionarios();
            DefaultComboBoxModel<Funcionario> model = new DefaultComboBoxModel<>();
            for (Funcionario f : funcionarios) model.addElement(f);
            cbFuncionarios.setModel(model);
        } catch (SQLException e) {
            showError("Erro ao carregar funcionários: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void carregarComboProdutos() {
        try {
            List<Produto> produtos = produtoService.listarProdutosAtivos();
            DefaultComboBoxModel<Produto> model = new DefaultComboBoxModel<>();
            produtosMap.clear();
            for (Produto p : produtos) {
                model.addElement(p);
                produtosMap.put(p.getProdutoId(), p);
            }
            cbProdutos.setModel(model);
        } catch (SQLException e) {
            showError("Erro ao carregar produtos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void adicionarItem() {
        Produto produto = (Produto) cbProdutos.getSelectedItem();
        if (produto == null) {
            showError("Selecione um produto.");
            return;
        }

        int quantidade = (Integer) spQuantidade.getValue();

        boolean itemExistente = false;
        for (ItemVenda itemExistenteNaVenda : vendaAtual.getItens()) {
            if (itemExistenteNaVenda.getProdutoId() == produto.getProdutoId()) {
                itemExistenteNaVenda.setQuantidadeVendida(itemExistenteNaVenda.getQuantidadeVendida() + quantidade);
                itemExistente = true;
                break;
            }
        }

        if (!itemExistente) {
            ItemVenda item = new ItemVenda();
            item.setProdutoId(produto.getProdutoId());
            item.setQuantidadeVendida(quantidade);
            item.setPrecoUnitarioVendido(produto.getPrecoVenda());
            vendaAtual.adicionarItem(item);
        }

        atualizarTabelaItens();
        atualizarTotalVendaAtual();
        spQuantidade.setValue(1);
        cbProdutos.setSelectedIndex(-1);
        exibirPrecoProdutoSelecionado();
        JOptionPane.showMessageDialog(this, "Item adicionado/atualizado na venda!", "Item Adicionado", JOptionPane.INFORMATION_MESSAGE);
    }

    private void atualizarTabelaItens() {
        tabelaModelItens.setRowCount(0);
        if (vendaAtual != null && vendaAtual.getItens() != null) {
            for (ItemVenda item : vendaAtual.getItens()) {
                Produto p = produtosMap.get(item.getProdutoId());
                String nomeProduto = (p != null) ? p.getNomeProduto() : "Produto ID " + item.getProdutoId() + " (não encontrado)";
                tabelaModelItens.addRow(new Object[]{
                        nomeProduto,
                        item.getQuantidadeVendida(),
                        item.getPrecoUnitarioVendido(),
                        item.getSubtotalItem()
                });
            }
        }
    }

    private void salvarVenda() {
        Cliente cliente = (Cliente) cbClientes.getSelectedItem();
        Funcionario funcionario = (Funcionario) cbFuncionarios.getSelectedItem();

        if (cliente == null) {
            showError("Selecione um cliente para a venda.");
            return;
        }
        if (funcionario == null) {
            showError("Selecione um funcionário para a venda.");
            return;
        }
        if (vendaAtual.getItens().isEmpty()) {
            showError("Adicione pelo menos um item à venda antes de salvar.");
            return;
        }

        vendaAtual.setClienteId(cliente.getClienteId());
        vendaAtual.setFuncionarioId(funcionario.getFuncionarioId());

        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja realmente finalizar esta venda?", "Confirmar Venda",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                vendaService.cadastrarVenda(vendaAtual);
                finalizarVenda();
                resetarFormularioVenda();
                carregarVendas();
            } catch (Exception e) {
                showError("Erro ao salvar venda: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void carregarVendas() {
        try {
            List<Venda> vendas = vendaService.listarVendas();
            List<Produto> produtos = produtoService.listarProdutos();
            produtosMap.clear();
            for (Produto p : produtos) {
                produtosMap.put(p.getProdutoId(), p);
            }

            Map<Integer, Cliente> clientesCache = new HashMap<>();
            Map<Integer, Funcionario> funcionariosCache = new HashMap<>();

            tabelaModelVendas.setRowCount(0);

            for (Venda v : vendas) {
                Cliente cliente = clientesCache.computeIfAbsent(v.getClienteId(), id -> {
                    try { return clienteService.buscarPorId(id); }
                    catch (SQLException ex) { ex.printStackTrace(); return null; }
                });

                Funcionario funcionario = funcionariosCache.computeIfAbsent(v.getFuncionarioId(), id -> {
                    try { return funcionarioService.buscarPorId(id); }
                    catch (SQLException ex) { ex.printStackTrace(); return null; }
                });

                // Monta a lista de nomes dos produtos separados por ", "
                String nomesProdutos = "-";
                if (v.getItens() != null && !v.getItens().isEmpty()) {
                    nomesProdutos = String.join(", ",
                            v.getItens().stream()
                                    .map(item -> {
                                        Produto p = produtosMap.get(item.getProdutoId());
                                        return (p != null) ? p.getNomeProduto() : "Produto ID " + item.getProdutoId();
                                    })
                                    .toList()
                    );
                }

                tabelaModelVendas.addRow(new Object[]{
                        cliente != null ? cliente.getNome() : "Cliente ID " + v.getClienteId(),
                        cliente != null ? cliente.getCpfCnpj() : "-",
                        cliente != null ? cliente.getEmail() : "-",
                        cliente != null ? cliente.getTelefone() : "-",
                        funcionario != null ? funcionario.getNome() : "-",
                        v.getData(),
                        nomesProdutos,
                        v.getValorTotal(),
                        v.getStatus()
                });
            }
        } catch (SQLException e) {
            showError("Erro ao listar vendas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarProdutoMaisVendido() {
        try {
            Produto produtoMaisVendido = produtoService.buscarProdutoMaisVendido();
            if (produtoMaisVendido != null) {
                JOptionPane.showMessageDialog(this,
                        "Produto mais vendido:\n" +
                                produtoMaisVendido.getNomeProduto(),
                        "Produto Mais Vendido",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Nenhum produto vendido até o momento.",
                        "Produto Mais Vendido",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            showError("Erro ao buscar produto mais vendido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void finalizarVenda() {
        try {
            vendaService.atualizarStatusVenda(vendaAtual.getVendaId(), "FINALIZADA");
            JOptionPane.showMessageDialog(this, "Venda finalizada com sucesso!");
            carregarVendas();
        } catch (SQLException e) {
            showError("Erro ao atualizar status da venda: " + e.getMessage());
        }
    }

    private void consultarTotalVendasPorData() {
        try {
            Date dataSelecionada = (Date) spDataConsulta.getValue();
            java.time.LocalDate dataLocal = dataSelecionada.toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate();

            int total = vendaService.contarVendasPorData(dataLocal);

            lblTotalVendasPorData.setText("Total de vendas na data selecionada" + ": " + total);
        } catch (SQLException e) {
            showError("Erro ao consultar total de vendas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void resetarFormularioVenda() {
        vendaAtual = new Venda();
        tabelaModelItens.setRowCount(0);
        cbClientes.setSelectedIndex(-1);
        cbFuncionarios.setSelectedIndex(-1);
        cbProdutos.setSelectedIndex(-1);
        spQuantidade.setValue(1);
        exibirPrecoProdutoSelecionado();
        atualizarTotalVendaAtual();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}