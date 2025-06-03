package org.example.controle_vendas.ui;

import com.formdev.flatlaf.FlatClientProperties; // Importar para estilos FlatLaf
import org.example.controle_vendas.dao.CategoriaDAO;
import org.example.controle_vendas.dao.ProdutoDAO;
import org.example.controle_vendas.model.Categoria;
import org.example.controle_vendas.model.Produto;
import org.example.controle_vendas.model.UnidadeMedida;
import org.example.controle_vendas.service.ProdutoService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.NumberFormat; // Para formatar números
import java.util.List;
import java.util.Locale; // Para formatar números

public class ProdutoUI extends JFrame {
    private final ProdutoService produtoService;
    private final CategoriaDAO categoriaDAO;
    private JFrame telaPrincipal;

    private JComboBox<Categoria> cbCategorias;
    private JComboBox<UnidadeMedida> cbUnidadeMedida;
    private JTextField tfNomeProduto;
    private JFormattedTextField tfPrecoVenda, tfPrecoCusto; // Mudado para JFormattedTextField
    private JTable tabelaProdutos;
    private DefaultTableModel tabelaModel;
    private JButton btnSalvarAtualizar; // Botão que muda de texto (Salvar/Atualizar)
    private JButton btnEditar;
    private JButton btnInativar; // RENOMEADO para "Inativar"
    private JButton btnLimpar; // Novo botão Limpar

    private Produto produtoEmEdicao; // Novo: Para controlar o produto sendo editado

    public ProdutoUI(ProdutoService produtoService, CategoriaDAO categoriaDAO, JFrame telaPrincipal) {
        this.produtoService = produtoService;
        this.categoriaDAO = categoriaDAO;
        this.telaPrincipal = telaPrincipal;
        this.produtoEmEdicao = null; // Inicializa sem produto em edição

        initComponents();
        carregarCategorias();
        carregarUnidadesMedida();
        carregarProdutos();
        atualizarBotoesAcao(); // Atualiza o estado dos botões ao iniciar
    }

    private void initComponents() {
        setTitle("Cadastro e Gerenciamento de Produtos"); // Título mais descritivo
        setSize(900, 650); // Aumentar tamanho para melhor visualização
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
                "Detalhes do Produto", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), Color.DARK_GRAY
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Linha 0: Categoria
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        painelFormulario.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        cbCategorias = new JComboBox<>();
        painelFormulario.add(cbCategorias, gbc);

        // Linha 1: Nome Produto
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        painelFormulario.add(new JLabel("Nome Produto:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        tfNomeProduto = new JTextField();
        tfNomeProduto.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nome do produto (ex: Camiseta Algodão)");
        painelFormulario.add(tfNomeProduto, gbc);

        // Linha 2: Preço Venda (JFormattedTextField)
        NumberFormat currencyFormat = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
        currencyFormat.setMinimumFractionDigits(2);
        currencyFormat.setMaximumFractionDigits(2);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        painelFormulario.add(new JLabel("Preço Venda (R$):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        tfPrecoVenda = new JFormattedTextField(currencyFormat);
        tfPrecoVenda.setColumns(10); // Tamanho preferencial
        tfPrecoVenda.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "0,00");
        painelFormulario.add(tfPrecoVenda, gbc);

        // Linha 3: Preço Custo (JFormattedTextField)
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        painelFormulario.add(new JLabel("Preço Custo (R$):"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        tfPrecoCusto = new JFormattedTextField(currencyFormat);
        tfPrecoCusto.setColumns(10); // Tamanho preferencial
        tfPrecoCusto.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "0,00");
        painelFormulario.add(tfPrecoCusto, gbc);

        // Linha 4: Unidade Medida
        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST;
        painelFormulario.add(new JLabel("Unidade Medida:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1.0;
        cbUnidadeMedida = new JComboBox<>();
        painelFormulario.add(cbUnidadeMedida, gbc);

        // ====================================================================================
        // PAINEL DE BOTÕES DE AÇÃO DO FORMULÁRIO
        // ====================================================================================
        JPanel painelBotoesFormulario = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));

        btnSalvarAtualizar = new JButton("Salvar Produto");
        btnSalvarAtualizar.putClientProperty(FlatClientProperties.BUTTON_TYPE, "roundRect");
        btnSalvarAtualizar.addActionListener(e -> salvarAtualizarProduto());
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
        // TABELA DE PRODUTOS
        // ====================================================================================
        tabelaModel = new DefaultTableModel(new String[]{"ID", "Categoria", "Nome", "Preço Venda", "Preço Custo", "Unidade Medida"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaProdutos = new JTable(tabelaModel);
        // Ajustar largura da coluna ID
        tabelaProdutos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabelaProdutos.getColumnModel().getColumn(0).setMaxWidth(80);
        tabelaProdutos.getColumnModel().getColumn(0).setMinWidth(50);

        // Configurar renderizadores para colunas de preço
        setupCurrencyRenderer(tabelaProdutos, 3); // Preço Venda
        setupCurrencyRenderer(tabelaProdutos, 4); // Preço Custo

        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Produtos Cadastrados (Ativos)"));

        tabelaProdutos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
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

        btnEditar = new JButton("Editar Produto");
        btnEditar.putClientProperty(FlatClientProperties.BUTTON_TYPE, "roundRect");
        btnEditar.addActionListener(e -> editarProduto());
        painelBotoesTabela.add(btnEditar);

        btnInativar = new JButton("Inativar Produto");
        btnInativar.putClientProperty(FlatClientProperties.BUTTON_TYPE, "roundRect");
        btnInativar.addActionListener(e -> inativarProduto());
        painelBotoesTabela.add(btnInativar);

        mainPanel.add(painelBotoesTabela, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // --- MÉTODOS AUXILIARES PARA UI/UX ---

    // Configura renderizador para formatar colunas de moeda e alinhar à direita
    private void setupCurrencyRenderer(JTable table, int columnIndex) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Number) {
                    value = currencyFormat.format(value);
                }
                setHorizontalAlignment(SwingConstants.RIGHT); // Alinha o texto à direita
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
        table.getColumnModel().getColumn(columnIndex).setCellRenderer(renderer);
    }


    // --- MÉTODOS DE LÓGICA DE NEGÓCIO E CONTROLE DA UI ---

    private void carregarCategorias() {
        try {
            List<Categoria> categorias = categoriaDAO.listarTodos();
            cbCategorias.removeAllItems();
            if (categorias.isEmpty()) {
                // Adiciona um item placeholder se não houver categorias
                cbCategorias.addItem(new Categoria(0, "Nenhuma Categoria Ativa", ""));
                cbCategorias.setEnabled(false); // Desabilita o combobox
                showWarning("Nenhuma categoria ativa encontrada. Cadastre categorias primeiro.");
            } else {
                for (Categoria c : categorias) {
                    cbCategorias.addItem(c);
                }
                cbCategorias.setEnabled(true);
            }
        } catch (SQLException e) {
            showError("Erro ao carregar categorias: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void carregarUnidadesMedida() {
        cbUnidadeMedida.removeAllItems();
        for (UnidadeMedida um : UnidadeMedida.values()) {
            cbUnidadeMedida.addItem(um);
        }
        cbUnidadeMedida.setSelectedIndex(-1); // Nenhuma seleção inicial
    }

    private void salvarAtualizarProduto() {
        String nomeProduto = tfNomeProduto.getText().trim();
        double precoVenda;
        double precoCusto;

        // Validação de campos obrigatórios
        if (nomeProduto.isEmpty()) {
            showWarning("O campo 'Nome Produto' não pode estar vazio.");
            tfNomeProduto.requestFocusInWindow();
            return;
        }

        Categoria categoriaSelecionada = (Categoria) cbCategorias.getSelectedItem();
        if (categoriaSelecionada == null || categoriaSelecionada.getCategoriaId() == 0) { // Verifica placeholder
            showWarning("Selecione uma categoria válida para o produto.");
            cbCategorias.requestFocusInWindow();
            return;
        }

        UnidadeMedida unidadeSelecionada = (UnidadeMedida) cbUnidadeMedida.getSelectedItem();
        if (unidadeSelecionada == null) {
            showWarning("Selecione uma unidade de medida para o produto.");
            cbUnidadeMedida.requestFocusInWindow();
            return;
        }

        try {
            // Obtém valores formatados e converte para double
            precoVenda = ((Number) tfPrecoVenda.getValue()).doubleValue();
            precoCusto = ((Number) tfPrecoCusto.getValue()).doubleValue();

            if (precoVenda <= 0) {
                throw new IllegalArgumentException("Preço de venda deve ser maior que zero.");
            }
            // Preço de custo pode ser zero ou negativo em alguns cenários, mas geralmente é >= 0
            if (precoCusto < 0) {
                throw new IllegalArgumentException("Preço de custo não pode ser negativo.");
            }

        } catch (NumberFormatException | NullPointerException e) { // NullPointerException para getValue() se campo vazio
            showWarning("Preços de venda e custo devem ser números válidos.");
            tfPrecoVenda.requestFocusInWindow();
            return;
        } catch (IllegalArgumentException e) {
            showWarning("Erro de validação: " + e.getMessage());
            return;
        }


        try {
            Produto produto = (produtoEmEdicao == null) ? new Produto() : produtoEmEdicao;
            produto.setCategoriaId(categoriaSelecionada.getCategoriaId());
            produto.setNomeProduto(nomeProduto);
            produto.setPrecoVenda(precoVenda);
            produto.setPrecoCusto(precoCusto);
            produto.setUnidadeMedida(unidadeSelecionada.name()); // Salva o nome do enum (ex: "UN", "KG")

            if (produtoEmEdicao == null) {
                produtoService.cadastrarProduto(produto);
                showInfo("Produto cadastrado com sucesso!");
            } else {
                produtoService.atualizarProduto(produto);
                showInfo("Produto atualizado com sucesso!");
            }
            limparFormulario();
            carregarProdutos();
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

    private void editarProduto() {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Selecione um produto na tabela para editar.");
            return;
        }

        int produtoId = (Integer) tabelaModel.getValueAt(selectedRow, 0);

        try {
            Produto produto = produtoService.buscarPorId(produtoId);
            if (produto != null) {
                produtoEmEdicao = produto;
                // Seleciona a categoria correta no ComboBox
                for (int i = 0; i < cbCategorias.getItemCount(); i++) {
                    if (cbCategorias.getItemAt(i).getCategoriaId() == produto.getCategoriaId()) {
                        cbCategorias.setSelectedIndex(i);
                        break;
                    }
                }
                tfNomeProduto.setText(produto.getNomeProduto());
                tfPrecoVenda.setValue(produto.getPrecoVenda()); // Usa setValue para JFormattedTextField
                tfPrecoCusto.setValue(produto.getPrecoCusto()); // Usa setValue para JFormattedTextField

                // Seleciona a unidade de medida correta no ComboBox
                UnidadeMedida um = UnidadeMedida.fromDescricao(produto.getUnidadeMedida());
                if (um != null) {
                    cbUnidadeMedida.setSelectedItem(um);
                } else {
                    cbUnidadeMedida.setSelectedIndex(-1); // Limpa se não encontrar
                }

                btnSalvarAtualizar.setText("Atualizar Produto");
                atualizarBotoesAcao();
            } else {
                showError("Produto não encontrado.");
                limparFormulario();
            }
        } catch (SQLException e) {
            showError("Erro ao buscar produto para edição: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void inativarProduto() {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Selecione um produto na tabela para inativar.");
            return;
        }

        int produtoId = (Integer) tabelaModel.getValueAt(selectedRow, 0);
        String produtoNome = (String) tabelaModel.getValueAt(selectedRow, 2); // Nome do produto

        int confirmResult = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja INATIVAR o produto '" + produtoNome + "'?\n" +
                        "Esta ação irá ocultá-lo das listas ativas, mas não o removerá permanentemente.",
                "Confirmar Inativação", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmResult == JOptionPane.YES_OPTION) {
            try {
                produtoService.inativarProduto(produtoId);
                showInfo("Produto inativado com sucesso!");
                carregarProdutos();
                limparFormulario();
            } catch (SQLException e) {
                showError("Erro ao inativar produto: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                showError("Ocorreu um erro inesperado ao inativar: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void carregarProdutos() {
        try {
            List<Produto> produtos = produtoService.listarProdutos(); // Este método já filtra por 'Ativo'
            tabelaModel.setRowCount(0);
            for (Produto p : produtos) {
                String nomeCategoria = "ID " + p.getCategoriaId();
                try {
                    Categoria cat = categoriaDAO.buscarPorId(p.getCategoriaId());
                    if (cat != null) {
                        nomeCategoria = cat.getNome();
                    }
                } catch (SQLException e) {
                    System.err.println("Erro ao buscar nome da categoria para produto " + p.getProdutoId() + ": " + e.getMessage());
                }

                String descricaoUnidadeMedida = p.getUnidadeMedida();
                UnidadeMedida um = UnidadeMedida.fromDescricao(p.getUnidadeMedida());
                if (um != null) {
                    descricaoUnidadeMedida = um.getDescricao();
                }

                tabelaModel.addRow(new Object[]{
                        p.getProdutoId(),
                        nomeCategoria,
                        p.getNomeProduto(),
                        p.getPrecoVenda(),
                        p.getPrecoCusto(),
                        descricaoUnidadeMedida
                });
            }
        } catch (SQLException e) {
            showError("Erro ao carregar produtos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            atualizarBotoesAcao();
        }
    }

    private void limparFormulario() {
        cbCategorias.setSelectedIndex(-1);
        cbUnidadeMedida.setSelectedIndex(-1);
        tfNomeProduto.setText("");
        tfPrecoVenda.setValue(0.0); // Reseta para 0.00
        tfPrecoCusto.setValue(0.0); // Reseta para 0.00
        produtoEmEdicao = null;
        btnSalvarAtualizar.setText("Salvar Produto");
        tabelaProdutos.clearSelection();
        atualizarBotoesAcao();
    }

    private void atualizarBotoesAcao() {
        boolean rowSelected = tabelaProdutos.getSelectedRow() != -1;
        btnEditar.setEnabled(rowSelected && produtoEmEdicao == null);
        btnInativar.setEnabled(rowSelected);

        if (produtoEmEdicao != null) {
            btnSalvarAtualizar.setText("Atualizar Produto");
            btnEditar.setEnabled(false);
        } else {
            btnSalvarAtualizar.setText("Salvar Produto");
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