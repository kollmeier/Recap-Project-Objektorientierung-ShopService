import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void addOrderTest() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo(), new UUIDIdGenerator());
        List<String> productsIds = List.of("1");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING);
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectThrow() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo(), new UUIDIdGenerator());
        List<String> productsIds = List.of("1", "2");

        //THEN
        assertThrows(IllegalArgumentException.class, () -> shopService.addOrder(productsIds));
    }

    @Test
    void getOrdersByOrderStatusTest_whenNoOrdersWithStatus_expectEmptyList() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo(), new UUIDIdGenerator());
        OrderStatus orderStatus = OrderStatus.COMPLETED;

        //WHEN
        List<Order> actual = shopService.getOrdersByOrderStatus(orderStatus);

        //THEN
        List<Order> expected = List.of();
        assertEquals(expected, actual);
    }

    @Test
    void getOrderByOrderStatusTest_whenOrdersWithStatus_expectOnlyOrdersWithStatusInList() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo(), new UUIDIdGenerator());
        OrderStatus orderStatus = OrderStatus.PROCESSING;
        Order addedOrder = shopService.addOrder(List.of("1"));

        //WHEN
        List<Order> actual = shopService.getOrdersByOrderStatus(orderStatus);

        //THEN
        List<Order> expected = List.of(
                addedOrder
        );
        assertEquals(expected, actual);
    }

    @Test
    void updateOrderStatusTest() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo(), new UUIDIdGenerator());
        Order addedOrder = shopService.addOrder(List.of("1"));
        OrderStatus newStatus = OrderStatus.IN_DELIVERY;
        String orderId = addedOrder.id();

        //WHEN
        Order actual = shopService.updateOrderStatus(orderId, newStatus);

        //THEN
        Order expected = new Order(orderId, List.of(new Product("1", "Apfel")), addedOrder.createdAt(), newStatus);
        assertEquals(expected, actual);
    }

    @Test
    void updateOrderStatusTest_whenInvalidOrderId_expectThrow() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo(), new UUIDIdGenerator());
        OrderStatus newStatus = OrderStatus.IN_DELIVERY;
        String orderId = "-1";

        //THEN
        assertThrows(IllegalArgumentException.class, () -> shopService.updateOrderStatus(orderId, newStatus));
    }

    @Test
    void updateOrderStatusTest_whenNullStatus_expectThrow() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo(), new UUIDIdGenerator());
        Order addedOrder = shopService.addOrder(List.of("1"));
        OrderStatus newStatus = null;
        String orderId = addedOrder.id();

        //THEN
        assertThrows(NullPointerException.class, () -> shopService.updateOrderStatus(orderId, newStatus));
    }

    @Test
    void updateOrderStatusTest_whenNullOrderId_expectThrow() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo(), new UUIDIdGenerator());
        Order addedOrder = shopService.addOrder(List.of("1"));
        OrderStatus newStatus = OrderStatus.IN_DELIVERY;
        String orderId = null;

        //THEN
        assertThrows(NullPointerException.class, () -> shopService.updateOrderStatus(orderId, newStatus));
    }

}
