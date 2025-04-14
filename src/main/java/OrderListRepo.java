import lombok.NonNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OrderListRepo implements OrderRepo{
    private List<Order> orders = new ArrayList<>();

    public List<Order> getOrders() {
        return orders;
    }

    public Order getOrderById(String id) {
        for (Order order : orders) {
            if (order.id().equals(id)) {
                return order;
            }
        }
        return null;
    }

    public Order addOrder(Order newOrder) {
        Order placedOrder = newOrder.withCreatedAt(Instant.now());
        orders.add(placedOrder);
        return placedOrder;
    }

    public void removeOrder(String id) {
        for (Order order : orders) {
            if (order.id().equals(id)) {
                orders.remove(order);
                return;
            }
        }
    }

    @Override
    public Order updateOrder(@NonNull Order order) {
        for (Order existingOrder : orders) {
            if (existingOrder.id().equals(order.id())) {
                orders.remove(existingOrder);
                orders.add(order);
                return order;
            }
        }
        throw new IllegalArgumentException("Order mit der Id: " + order.id() + " konnte nicht aktualisiert werden!");
    }
}
