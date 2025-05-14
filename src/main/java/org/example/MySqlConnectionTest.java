package org.example;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class MySqlConnectionTest {
    private static final String URL = System.getenv("DB_URL");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    @Test
    public void testConnection() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            assertNotNull(conn, "A conexão deve ser criada com sucesso");
            System.out.println("Conexão com MySQL estabelecida com sucesso!");
        } catch (SQLException e) {
            fail("Falha ao conectar com o banco: " + e.getMessage());
        }
    }
}
