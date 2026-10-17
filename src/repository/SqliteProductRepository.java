package repository;

import model.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Implementacao que guarda os produtos em um banco SQLite.
// Substitui o CsvProductRepository sem que o InventoryService precise mudar.
public class SqliteProductRepository implements ProductRepository {
    private final String url;

    public SqliteProductRepository(String dbFile) {
        this.url = "jdbc:sqlite:" + dbFile;
        criarTabela();
    }

    private void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS products ("
                + "id TEXT PRIMARY KEY,"
                + "name TEXT NOT NULL,"
                + "category TEXT,"
                + "price REAL,"
                + "quantity INTEGER)";
        try (Connection conn = DriverManager.getConnection(url);
             Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            System.err.println("[SQLite] Falha ao criar tabela: " + e.getMessage());
        }
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, name, category, price, quantity FROM products ORDER BY id";
        try (Connection conn = DriverManager.getConnection(url);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                products.add(new Product(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")));
            }
        } catch (SQLException e) {
            System.err.println("[SQLite] Falha ao carregar: " + e.getMessage());
        }
        return products;
    }

    // Regrava a tabela inteira, mesmo comportamento que o CSV ja tinha.
    @Override
    public void saveAll(List<Product> products) {
        String insert = "INSERT INTO products (id, name, category, price, quantity) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);
            try (Statement st = conn.createStatement()) {
                st.execute("DELETE FROM products");
            }
            try (PreparedStatement ps = conn.prepareStatement(insert)) {
                for (Product p : products) {
                    ps.setString(1, p.getId());
                    ps.setString(2, p.getName());
                    ps.setString(3, p.getCategory());
                    ps.setDouble(4, p.getPrice());
                    ps.setInt(5, p.getQuantity());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            conn.commit();
        } catch (SQLException e) {
            System.err.println("[SQLite] Falha ao salvar: " + e.getMessage());
        }
    }
}
