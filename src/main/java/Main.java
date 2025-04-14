import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    
    public static void main(String[] args) {
        ProductRepo productRepo = new ProductRepo();
        OrderRepo orderRepo = new OrderMapRepo();
        IdGenerator idGenerator = new UUIDIdGenerator();
        ShopService shopService = new ShopService(productRepo, orderRepo, idGenerator);
        productRepo.addProduct(new Product("2", "Birne"));
        productRepo.addProduct(new Product("3", "Kirsche"));
        productRepo.addProduct(new Product("4", "Banane"));


        Order order1 = shopService.addOrder(List.of("1", "2"));
        Order order2 = shopService.addOrder(List.of("3"));
        Order order3 = shopService.addOrder(List.of("4"));

        shopService.updateOrderStatus(order1.id(), OrderStatus.IN_DELIVERY);
        shopService.updateOrderStatus(order2.id(), OrderStatus.IN_DELIVERY);
        shopService.updateOrderStatus(order3.id(), OrderStatus.IN_DELIVERY);

        Order order4 = shopService.addOrder(List.of("1", "3"));
        Order order5 = shopService.addOrder(List.of("2", "4"));
        Order order6 = shopService.addOrder(List.of("3", "4", "1"));

        shopService.updateOrderStatus(order4.id(), OrderStatus.COMPLETED);
        shopService.updateOrderStatus(order5.id(), OrderStatus.COMPLETED);
        shopService.updateOrderStatus(order6.id(), OrderStatus.COMPLETED);

        Order order7 = shopService.addOrder(List.of("2"));
        Order order8 = shopService.addOrder(List.of("1", "4"));
        Order oldestDelivery = shopService.getOldestOrderPerStatus(OrderStatus.IN_DELIVERY).orElse(null);
        System.out.println(oldestDelivery);

        Order oldestProcessing = shopService.getOldestOrderPerStatus(OrderStatus.PROCESSING).orElse(null);
        System.out.println(oldestProcessing);

        Order oldestCompleted = shopService.getOldestOrderPerStatus(OrderStatus.COMPLETED).orElse(null);
        System.out.println(oldestCompleted);

        TransactionProcessor transactionProcessor = new TransactionProcessor(shopService);
        try {
            transactionProcessor.process(Files.readString(Path.of("src/main/resources/transactions.txt")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
