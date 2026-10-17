package util;

import model.Product;
import repository.CsvProductRepository;
import repository.SqliteProductRepository;

import java.util.List;

// Utilitario executado uma unica vez para levar os dados do CSV antigo
// para o banco SQLite novo.
public class MigracaoCsvSqlite {
    public static void main(String[] args) {
        String csv = "data/inventory.csv";
        String db = "data/inventory.db";

        List<Product> produtos = new CsvProductRepository(csv).findAll();
        new SqliteProductRepository(db).saveAll(produtos);

        System.out.println("Migrados " + produtos.size() + " produtos de " + csv + " para " + db);
    }
}
