import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class Main {
    
    public static void main(String[] args) {
        ProductRepo productRepo = new ProductRepo();
        OrderRepo orderRepo = new OrderMapRepo();
        IdGenerator idGenerator = new UUIDIdGenerator();
        ShopService shopService = new ShopService(productRepo, orderRepo, idGenerator);
        productRepo.addProduct(new Product("2", "Birne"));
        productRepo.addProduct(new Product("3", "Kirsche"));
        productRepo.addProduct(new Product("4", "Banane"));

        productRepo.increaseQuantity("1", BigDecimal.TEN);
        productRepo.increaseQuantity("2", new BigDecimal("50"));
        productRepo.increaseQuantity("3", BigDecimal.TEN);
        productRepo.increaseQuantity("4", BigDecimal.TEN);

        Order order1 = shopService.addOrder(Map.of("1", BigDecimal.ONE, "2", BigDecimal.TEN));
        Order order2 = shopService.addOrder(Map.of("3", BigDecimal.TWO));
        Order order3 = shopService.addOrder(Map.of("4", BigDecimal.ONE));

        shopService.updateOrderStatus(order1.id(), OrderStatus.IN_DELIVERY);
        shopService.updateOrderStatus(order2.id(), OrderStatus.IN_DELIVERY);
        shopService.updateOrderStatus(order3.id(), OrderStatus.IN_DELIVERY);

        Order order4 = shopService.addOrder(Map.of("1", BigDecimal.ONE, "3", BigDecimal.ONE));
        Order order5 = shopService.addOrder(Map.of("2", BigDecimal.TWO, "4", BigDecimal.ONE));
        Order order6 = shopService.addOrder(Map.of("3", BigDecimal.TWO, "4", BigDecimal.ONE, "1", BigDecimal.ONE));

        shopService.updateOrderStatus(order4.id(), OrderStatus.COMPLETED);
        shopService.updateOrderStatus(order5.id(), OrderStatus.COMPLETED);
        shopService.updateOrderStatus(order6.id(), OrderStatus.COMPLETED);

        Order order7 = shopService.addOrder(Map.of("2", BigDecimal.TWO));
        Order order8 = shopService.addOrder(Map.of("1", BigDecimal.TWO, "4", BigDecimal.ONE));

        System.out.println("\nIn Stock:");
        System.out.println(productRepo.getProducts());

        System.out.println("\nOrders:");
        System.out.println(orderRepo.getOrders());

        System.out.println("\nNext to process per Status:");
        Order oldestDelivery = shopService.getOldestOrderPerStatus(OrderStatus.IN_DELIVERY).orElse(null);
        System.out.println(oldestDelivery);

        Order oldestProcessing = shopService.getOldestOrderPerStatus(OrderStatus.PROCESSING).orElse(null);
        System.out.println(oldestProcessing);

        Order oldestCompleted = shopService.getOldestOrderPerStatus(OrderStatus.COMPLETED).orElse(null);
        System.out.println(oldestCompleted);

        System.out.println("\n\nRunning transactions");
        TransactionProcessor transactionProcessor = new TransactionProcessor(shopService);
        try {
            transactionProcessor.process(Files.readString(Path.of("src/main/resources/transactions.txt")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
