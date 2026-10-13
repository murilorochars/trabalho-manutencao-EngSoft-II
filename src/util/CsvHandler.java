package util;

import model.Product;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles CSV file persistence for Product data.
 */
public class CsvHandler {

    public static List<Product> load(String filepath) {
        List<Product> products = new ArrayList<>();
        Path path = Paths.get(filepath);
        if (!Files.exists(path)) return products;
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            boolean header = true;
            while ((line = reader.readLine()) != null) {
                if (header) { header = false; continue; } // skip header
                if (!line.trim().isEmpty()) {
                    products.add(fromCsv(line.trim()));
                }
            }
        } catch (IOException e) {
            System.err.println("[CsvHandler] Failed to load: " + e.getMessage());
        }
        return products;
    }

    public static void save(String filepath, List<Product> products) {
        try {
            Files.createDirectories(Paths.get(filepath).getParent());
            try (PrintWriter writer = new PrintWriter(new FileWriter(filepath))) {
                writer.println("id,name,category,price,quantity");
                for (Product p : products) {
                    writer.println(toCsv(p));
                }
            }
        } catch (IOException e) {
            System.err.println("[CsvHandler] Failed to save: " + e.getMessage());
        }
    }

    // Metodos movidos da classe Product: converter para CSV e responsabilidade desta classe
    private static String toCsv(Product p) {
        return p.getId() + "," + p.getName() + "," + p.getCategory() + "," + p.getPrice() + "," + p.getQuantity();
    }

    private static Product fromCsv(String line) {
        String[] parts = line.split(",");
        return new Product(parts[0], parts[1], parts[2],
                Double.parseDouble(parts[3]), Integer.parseInt(parts[4]));
    }
}
