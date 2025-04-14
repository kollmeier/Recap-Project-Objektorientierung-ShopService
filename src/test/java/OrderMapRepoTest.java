import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapRepoTest {

    @Test
    void getOrders() {
        //GIVEN
        OrderMapRepo repo = new OrderMapRepo();

        Product product = new Product("1", "Apfel");
        Order newOrder = new Order("1", List.of(product), OrderStatus.PROCESSING);
        Order addedOrder = repo.addOrder(newOrder);

        //WHEN
        List<Order> actual = repo.getOrders();

        //THEN
        List<Order> expected = new ArrayList<>();
        Product product1 = new Product("1", "Apfel");
        expected.add(new Order("1", List.of(product1), addedOrder.createdAt(), OrderStatus.PROCESSING));

        assertEquals(actual, expected);
    }

    @Test
    void getOrderById() {
        //GIVEN
        OrderMapRepo repo = new OrderMapRepo();

        Product product = new Product("1", "Apfel");
        Order newOrder = new Order("1", List.of(product), OrderStatus.PROCESSING);
        repo.addOrder(newOrder);

        //WHEN
        Order actual = repo.getOrderById("1");

        //THEN
        Product product1 = new Product("1", "Apfel");
        Order expected = new Order("1", List.of(product1), actual.createdAt(), OrderStatus.PROCESSING);

        assertEquals(actual, expected);
    }

    @Test
    void addOrder() {
        //GIVEN
        OrderMapRepo repo = new OrderMapRepo();
        Product product = new Product("1", "Apfel");
        Order newOrder = new Order("1", List.of(product), OrderStatus.PROCESSING);

        //WHEN
        Order actual = repo.addOrder(newOrder);

        //THEN
        Product product1 = new Product("1", "Apfel");
        Order expected = new Order("1", List.of(product1), actual.createdAt(), OrderStatus.PROCESSING);
        assertEquals(actual, expected);
        assertEquals(repo.getOrderById("1"), expected);
    }

    @Test
    void removeOrder() {
        //GIVEN
        OrderMapRepo repo = new OrderMapRepo();

        //WHEN
        repo.removeOrder("1");

        //THEN
        assertNull(repo.getOrderById("1"));
    }

    @Test
    void updateOrderThrowsNullPointerExceptionIfParameterIsNull() {
        //GIVEN
        OrderMapRepo repo = new OrderMapRepo();

        //WHEN & THEN
        assertThrows(NullPointerException.class, () -> repo.updateOrder(null));
    }

    @Test
    void updateOrderThrowsIllegalArgumentExceptionIfOrderNotExists() {
        //GIVEN
        OrderMapRepo repo = new OrderMapRepo();

        Product product = new Product("1", "Apfel");
        Order order = new Order("1", List.of(product), OrderStatus.PROCESSING);

        //WHEN & THEN
        assertThrows(IllegalArgumentException.class, () -> repo.updateOrder(order));
    }

    @Test
    void updateOrderReturnsUpdatedOrder() {
        //GIVEN
        OrderMapRepo repo = new OrderMapRepo();

        Product product = new Product("1", "Apfel");
        Order order = new Order("1", List.of(product), OrderStatus.PROCESSING);
        repo.addOrder(order);

        Product product2 = new Product("2", "Banane");
        Order updatedOrder = new Order("1", List.of(product2), OrderStatus.PROCESSING);

        //WHEN
        repo.updateOrder(updatedOrder);
        Order actual = repo.getOrderById("1");

        //THEN
        assertEquals(actual, updatedOrder);
    }
}
