package org.example.controle_vendas.ui;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.example.controle_vendas.dao.CategoriaDAO;
import org.example.controle_vendas.dao.ClienteDAO;
import org.example.controle_vendas.dao.FuncionarioDAO;
import org.example.controle_vendas.dao.ProdutoDAO;
import org.example.controle_vendas.service.CategoriaService;
import org.example.controle_vendas.service.ClienteService;
import org.example.controle_vendas.service.FuncionarioService;
import org.example.controle_vendas.service.ProdutoService;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TelaPrincipalUI extends JFrame {

    private Connection connection;

    public TelaPrincipalUI() {
        super("Sistema Controle de Vendas"); // Título da janela
        try {
            String dbUrl = System.getenv("DB_URL");
            String dbUser = System.getenv("DB_USER");
            String dbPassword = System.getenv("DB_PASSWORD");

            if (dbUrl == null || dbUser == null || dbPassword == null) {
                JOptionPane.showMessageDialog(this,
                        "Variáveis de ambiente DB_URL, DB_USER, DB_PASSWORD não configuradas.",
                        "Erro de Configuração", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            System.out.println("Conexão com o banco de dados estabelecida com sucesso.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }

        initComponents();
    }

    private void initComponents() {
        setSize(800,600); // Aumentar um pouco o tamanho
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Painel principal usando BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding externo

        // Título centralizado no topo
        JLabel titleLabel = new JLabel("Controle de Vendas", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Fonte maior e em negrito
        titleLabel.setBorder(new EmptyBorder(10, 0, 30, 0)); // Espaçamento abaixo do título
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Painel para os botões usando GridBagLayout para flexibilidade
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaçamento entre os botões
        gbc.fill = GridBagConstraints.HORIZONTAL; // Faz os botões preencherem horizontalmente
        gbc.weightx = 1.0; // Distribui o espaço horizontal uniformemente

        // Criar e adicionar os botões
        // Sugestão: Use ImageIcon para adicionar ícones aos botões (ex: new ImageIcon("path/to/icon.png"))
        // Você precisaria ter os arquivos de imagem (.png) em um diretório acessível.

        // Botão Funcionários
        JButton btnFuncionarios = new JButton("Funcionários");
        // btnFuncionarios.setIcon(new ImageIcon("path/to/employee_icon.png"));
        btnFuncionarios.putClientProperty( FlatClientProperties.BUTTON_TYPE, "square" );
        btnFuncionarios.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnFuncionarios.addActionListener(e -> abrirFuncionarioUI());
        gbc.gridy = 0;
        buttonPanel.add(btnFuncionarios, gbc);

        // Botão Clientes
        JButton btnClientes = new JButton("Clientes"); // Texto mais conciso
        // btnClientes.setIcon(new ImageIcon("path/to/customer_icon.png")); // Exemplo de ícone
        btnClientes.putClientProperty( FlatClientProperties.BUTTON_TYPE, "square" ); // Estilo FlatLaf (opcional)
        btnClientes.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Fonte maior para botões
        btnClientes.addActionListener(e -> abrirClienteUI());
        gbc.gridy = 1;
        buttonPanel.add(btnClientes, gbc);

        // Botão Categorias
        JButton btnCategorias = new JButton("Categorias");
        // btnCategorias.setIcon(new ImageIcon("path/to/category_icon.png"));
        btnCategorias.putClientProperty( FlatClientProperties.BUTTON_TYPE, "square" );
        btnCategorias.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnCategorias.addActionListener(e -> abrirCategoriaUI());
        gbc.gridy = 2;
        buttonPanel.add(btnCategorias, gbc);

        // Botão Produtos
        JButton btnProdutos = new JButton("Produtos");
        // btnProdutos.setIcon(new ImageIcon("path/to/product_icon.png"));
        btnProdutos.putClientProperty( FlatClientProperties.BUTTON_TYPE, "square" );
        btnProdutos.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnProdutos.addActionListener(e -> abrirProdutoUI());
        gbc.gridy = 3;
        buttonPanel.add(btnProdutos, gbc);

        // Botão Vendas
        JButton btnVendas = new JButton("Vendas");
        // btnVendas.setIcon(new ImageIcon("path/to/sale_icon.png"));
        btnVendas.putClientProperty( FlatClientProperties.BUTTON_TYPE, "square" );
        btnVendas.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnVendas.addActionListener(e -> abrirVendaUI());
        gbc.gridy = 4;
        buttonPanel.add(btnVendas, gbc);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void abrirClienteUI() {
        ClienteService clienteService = new ClienteService(new ClienteDAO(connection));
        SwingUtilities.invokeLater(() -> {
            ClienteUI clienteUI = new ClienteUI(clienteService, this);
            clienteUI.setVisible(true);
            this.setVisible(false);
        });
    }

    private void abrirProdutoUI() {
        ProdutoService produtoService = new ProdutoService(new ProdutoDAO(connection));
        CategoriaDAO categoriaDAO = new CategoriaDAO(connection);
        SwingUtilities.invokeLater(() -> {
            ProdutoUI produtoUI = new ProdutoUI(connection, this);
            produtoUI.setVisible(true);
            this.setVisible(false);
        });
    }

    private void abrirCategoriaUI() {
        CategoriaService categoriaService = new CategoriaService(new CategoriaDAO(connection));
        SwingUtilities.invokeLater(() -> {
            CategoriaUI categoriaUI = new CategoriaUI(categoriaService, this);
            categoriaUI.setVisible(true);
            this.setVisible(false);
        });
    }

    private void abrirFuncionarioUI() {
        FuncionarioService funcionarioService = new FuncionarioService(new FuncionarioDAO(connection));
        SwingUtilities.invokeLater(() -> {
            FuncionarioUI funcionarioUI = new FuncionarioUI(funcionarioService, this);
            funcionarioUI.setVisible(true);
            this.setVisible(false);
        });
    }

    private void abrirVendaUI() {
        SwingUtilities.invokeLater(() -> {
            VendaUI vendaUI = new VendaUI(connection, this);
            vendaUI.setVisible(true);
            this.setVisible(false);
        });
    }

    public static void main(String[] args) {
        try {
            FlatLaf.setup( new FlatDarculaLaf()); // Ou FlatDarkLaf, FlatDarculaLaf, etc.
        } catch ( Exception ex ) {
            System.err.println( "Failed to initialize FlatLaf" );
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            TelaPrincipalUI principal = new TelaPrincipalUI();
            principal.setVisible(true);
        });
    }
}