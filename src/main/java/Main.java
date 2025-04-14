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
    }

}
