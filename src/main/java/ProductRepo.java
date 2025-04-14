import lombok.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepo {
    private List<Product> products;

    public ProductRepo() {
        products = new ArrayList<>();
        products.add(new Product("1", "Apfel", BigDecimal.TEN));
    }

    public List<Product> getProducts() {
        return products;
    }

    public Optional<Product> getProductById(String id) {
        for (Product product : products) {
            if (product.id().equals(id)) {
                return Optional.of(product);
            }
        }
        return Optional.empty();
    }

    public Product addProduct(Product newProduct) {
        products.add(newProduct);
        return newProduct;
    }

    public void removeProduct(String id) {
        for (Product product : products) {
           if (product.id().equals(id)) {
               products.remove(product);
               return;
           }
        }
    }

    public void increaseQuantity(@NonNull String id, @NonNull BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must not be negative");
        }
        Product product = getProductById(id).orElseThrow();
        removeProduct(product.id());
        addProduct(product.withQuantity(product.quantity().add(amount)));
    }

    public void decreaseQuantity(@NonNull String id, @NonNull BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must not be negative");
        }
        Product product = getProductById(id).orElseThrow();
        if (product.quantity().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Not enough stock");
        }
        removeProduct(product.id());
        addProduct(product.withQuantity(product.quantity().subtract(amount)));
    }

    public boolean isInStock(@NonNull String id) {
        Product product = getProductById(id).orElseThrow();
        return product.quantity().compareTo(BigDecimal.ZERO) > 0;
    }
}
