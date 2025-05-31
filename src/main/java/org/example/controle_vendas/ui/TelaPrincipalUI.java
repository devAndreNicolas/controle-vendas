package org.example.controle_vendas.ui;

import org.example.controle_vendas.dao.CategoriaDAO;
import org.example.controle_vendas.dao.ClienteDAO;
import org.example.controle_vendas.dao.FuncionarioDAO;
import org.example.controle_vendas.dao.ProdutoDAO;
import org.example.controle_vendas.service.CategoriaService;
import org.example.controle_vendas.service.ClienteService;
import org.example.controle_vendas.service.FuncionarioService;
import org.example.controle_vendas.service.ProdutoService;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TelaPrincipalUI extends JFrame {

    private Connection connection;

    public TelaPrincipalUI() {
        try {
            // Ajuste conforme seu usuário, senha e banco
            connection = DriverManager.getConnection(System.getenv("DB_URL"), System.getenv("DB_USER"), System.getenv("DB_PASSWORD"));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        initComponents();
    }

    private void initComponents() {
        setTitle("Sistema Controle de Vendas - Tela Principal");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel painel = new JPanel(new GridLayout(5, 1, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnClientes = new JButton("Gerenciar Clientes");
        btnClientes.addActionListener(e -> abrirClienteUI());
        painel.add(btnClientes);

        JButton btnProdutos = new JButton("Gerenciar Produtos");
        btnProdutos.addActionListener(e -> abrirProdutoUI());
        painel.add(btnProdutos);

        JButton btnCategorias = new JButton("Gerenciar Categorias");
        btnCategorias.addActionListener(e -> abrirCategoriaUI());
        painel.add(btnCategorias);

        JButton btnFuncionarios = new JButton("Gerenciar Funcionários");
        btnFuncionarios.addActionListener(e -> abrirFuncionarioUI());
        painel.add(btnFuncionarios);

        JButton btnVendas = new JButton("Gerenciar Vendas");
        btnVendas.addActionListener(e -> abrirVendaUI());
        painel.add(btnVendas);

        add(painel);
    }

    private void abrirClienteUI() {
        ClienteDAO clienteDAO = new ClienteDAO(connection);
        ClienteService clienteService = new ClienteService(clienteDAO);
        SwingUtilities.invokeLater(() -> {
            ClienteUI clienteUI = new ClienteUI(clienteService);
            clienteUI.setVisible(true);
        });
    }

    private void abrirProdutoUI() {
        ProdutoDAO produtoDAO = new ProdutoDAO(connection);
        ProdutoService produtoService = new ProdutoService(produtoDAO);
        SwingUtilities.invokeLater(() -> {
            ProdutoUI produtoUI = new ProdutoUI(produtoService);
            produtoUI.setVisible(true);
        });
    }

    private void abrirCategoriaUI() {
        CategoriaDAO categoriaDAO = new CategoriaDAO(connection);
        CategoriaService categoriaService = new CategoriaService(categoriaDAO);
        SwingUtilities.invokeLater(() -> {
            CategoriaUI categoriaUI = new CategoriaUI(categoriaService);
            categoriaUI.setVisible(true);
        });
    }

    private void abrirFuncionarioUI() {
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO(connection);
        FuncionarioService funcionarioService = new FuncionarioService(funcionarioDAO);
        SwingUtilities.invokeLater(() -> {
            FuncionarioUI funcionarioUI = new FuncionarioUI(funcionarioService);
            funcionarioUI.setVisible(true);
        });
    }

    private void abrirVendaUI() {
        SwingUtilities.invokeLater(() -> {
            VendaUI vendaUI = new VendaUI(connection);
            vendaUI.setVisible(true);
        });
    }

    public static void main(String[] args) {
        System.out.println("DB_URL: " + System.getenv("DB_URL"));
        System.out.println("DB_USER: " + System.getenv("DB_USER"));
        System.out.println("DB_PASSWORD: " + System.getenv("DB_PASSWORD"));
        SwingUtilities.invokeLater(() -> {
            TelaPrincipalUI principal = new TelaPrincipalUI();
            principal.setVisible(true);
        });
    }
}