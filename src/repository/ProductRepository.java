package repository;

import model.Product;

import java.util.List;

// Define como os produtos sao carregados e salvos.
// O InventoryService depende desta interface, e nao mais do CSV direto.
public interface ProductRepository {
    List<Product> findAll();

    void saveAll(List<Product> products);
}
