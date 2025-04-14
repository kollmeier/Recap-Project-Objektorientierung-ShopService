import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepoTest {

    @org.junit.jupiter.api.Test
    void getProducts() {
        //GIVEN
        ProductRepo repo = new ProductRepo();

        //WHEN
        List<Product> actual = repo.getProducts();

        //THEN
        List<Product> expected = new ArrayList<>();
        expected.add(new Product("1", "Apfel"));
        assertEquals(actual, expected);
    }

    @org.junit.jupiter.api.Test
    void getProductById() {
        //GIVEN
        ProductRepo repo = new ProductRepo();

        //WHEN
        Optional<Product> actualOptional = repo.getProductById("1");

        //THEN
        assertFalse(actualOptional.isEmpty());
        Product expected = new Product("1", "Apfel");
        assertEquals(actualOptional.get(), expected);
    }

    @org.junit.jupiter.api.Test
    void getProductById_whenInvalidId_expectEmptyOptional() {
        //GIVEN
        ProductRepo repo = new ProductRepo();

        //WHEN
        Optional<Product> actualOptional = repo.getProductById("2");

        //THEN
        assertTrue(actualOptional.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void addProduct() {
        //GIVEN
        ProductRepo repo = new ProductRepo();
        Product newProduct = new Product("2", "Banane");

        //WHEN
        Product actual = repo.addProduct(newProduct);

        //THEN
        Product expected = new Product("2", "Banane");
        assertEquals(actual, expected);
        assertEquals(repo.getProductById("2").orElse(null), expected);
    }

    @org.junit.jupiter.api.Test
    void addProduct_whenNewProductIsAdded_thenQuantityIsZero() {
        //GIVEN
        ProductRepo repo = new ProductRepo();
        Product newProduct = new Product("2", "Banane");

        //WHEN
        Product actual = repo.addProduct(newProduct);

        //THEN
        BigDecimal expected = BigDecimal.ZERO;
        assertEquals(repo.getProductById("2").orElse(new Product("-1", "no").withQuantity(BigDecimal.TEN)).quantity(), BigDecimal.ZERO);
    }

    @org.junit.jupiter.api.Test
    void removeProduct() {
        //GIVEN
        ProductRepo repo = new ProductRepo();

        //WHEN
        repo.removeProduct("1");

        //THEN
        assertTrue(repo.getProductById("1").isEmpty());
    }
    

    @org.junit.jupiter.api.Test
    void increaseQuantity() {
        // GIVEN
        ProductRepo repo = new ProductRepo();
        Product existingProduct = new Product("1", "Apfel", BigDecimal.ONE);
        repo.addProduct(existingProduct);

        // WHEN
        repo.increaseQuantity("1", new BigDecimal("2"));

        // THEN
        Product updatedProduct = repo.getProductById("1").orElse(null);
        assertNotNull(updatedProduct);
        assertEquals(new BigDecimal("3"), updatedProduct.quantity());
    }
    
    @Test
    void increaseQuantity_whenIdNull_thenThrowException() {
        // GIVEN
        ProductRepo repo = new ProductRepo();

        // WHEN & THEN
        assertThrows(NullPointerException.class, () -> repo.increaseQuantity(null, new BigDecimal("2")));
    }

    @Test
    void increaseQuantity_whenAmountNull_thenThrowException() {
        // GIVEN
        ProductRepo repo = new ProductRepo();

        // WHEN & THEN
        assertThrows(NullPointerException.class, () -> repo.increaseQuantity("1", null));
    }

    @Test
    void increaseQuantity_whenAmountNegative_thenThrowException() {
        // GIVEN
        ProductRepo repo = new ProductRepo();

        // WHEN & THEN
        assertThrows(IllegalArgumentException.class, () -> repo.increaseQuantity("1", new BigDecimal(-1)));
    }

    @org.junit.jupiter.api.Test
    void decreaseQuantity() {
        // GIVEN
        ProductRepo repo = new ProductRepo();
        Product existingProduct = new Product("1", "Apfel", new BigDecimal("5"));
        repo.addProduct(existingProduct);

        // WHEN
        repo.decreaseQuantity("1", new BigDecimal("2"));

        // THEN
        Product updatedProduct = repo.getProductById("1").orElse(null);
        assertNotNull(updatedProduct);
        assertEquals(new BigDecimal("3"), updatedProduct.quantity());
    }

    @Test
    void decreaseQuantity_whenAmountNull_thenThrowException() {
        // GIVEN
        ProductRepo repo = new ProductRepo();

        // WHEN & THEN
        assertThrows(NullPointerException.class, () -> repo.decreaseQuantity("1", null));
    }

    @Test
    void decreaseQuantity_whenAmountBiggerThanQuantity_thenThrowException() {
        // GIVEN
        ProductRepo repo = new ProductRepo();

        // WHEN & THEN
        assertThrows(NullPointerException.class, () -> repo.decreaseQuantity("1", BigDecimal.TWO));
    }

    @org.junit.jupiter.api.Test
    void isInStock() {
        // GIVEN
        ProductRepo repo = new ProductRepo();
        Product inStockProduct = new Product("1", "Apfel", new BigDecimal("5"));
        Product outOfStockProduct = new Product("2", "Banane", BigDecimal.ZERO);
        repo.addProduct(inStockProduct);
        repo.addProduct(outOfStockProduct);

        // WHEN & THEN
        assertTrue(repo.isInStock("1"));
        assertFalse(repo.isInStock("2"));
    }
}
