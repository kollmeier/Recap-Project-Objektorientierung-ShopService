import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.*;

@RequiredArgsConstructor
public class ShopService {
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final IdGenerator idGenerator;

    public Order addOrder(Map<String, BigDecimal> amounts) {
        List<Product> products = new ArrayList<>();
        for (String productId : amounts.keySet()) {
            Product product = productRepo.getProductById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product mit der Id: " + productId + " konnte nicht bestellt werden!"));
            if (!productRepo.isInStock(productId)) {
                throw new IllegalArgumentException("Product mit der Id: " + productId + " ist nicht im Lager!");
            }
            productRepo.decreaseQuantity(productId, amounts.get(productId));
            products.add(product);
        }

        Order newOrder = new Order(idGenerator.generateId(), products, OrderStatus.PROCESSING);

        return orderRepo.addOrder(newOrder);
    }

    public Order updateOrderStatus(@NonNull String orderId, @NonNull OrderStatus newStatus) {
        Order order = orderRepo.getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order mit der Id: " + orderId + " konnte nicht aktualisiert werden!");
        }
        return orderRepo.updateOrder(order.withStatus(newStatus));
    }

    public List<Order> getOrdersByOrderStatus(final OrderStatus orderStatus) {
        return orderRepo.getOrders().stream().filter(order -> order.status().equals(orderStatus)).toList();
    }

    public Optional<Order> getOldestOrderPerStatus(final OrderStatus orderStatus) {
        List<Order> orders = getOrdersByOrderStatus(orderStatus);
        if (orders.isEmpty()) {
            return Optional.empty();
        }
        return orders.stream().min(Comparator.comparing(Order::createdAt));
    }
}
