package repository;

import model.Product;
import util.CsvHandler;

import java.util.List;

// Implementacao que guarda os produtos em arquivo CSV.
public class CsvProductRepository implements ProductRepository {
    private final String filePath;

    public CsvProductRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Product> findAll() {
        return CsvHandler.load(filePath);
    }

    @Override
    public void saveAll(List<Product> products) {
        CsvHandler.save(filePath, products);
    }
}
