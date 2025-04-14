import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void addOrderTest() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo(), new UUIDIdGenerator());
        Map<String, BigDecimal> amounts = new HashMap<>();
        amounts.put("1", BigDecimal.ONE);

        //WHEN
        Order actual = shopService.addOrder(amounts);

        //THEN
        Order expected = new Order("-1", List.of(new Product("1", "Apfel", BigDecimal.TEN)), OrderStatus.PROCESSING);
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectThrow() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo(), new UUIDIdGenerator());
        Map<String, BigDecimal> amounts = new HashMap<>();
        amounts.put("1", BigDecimal.ONE);
        amounts.put("2", BigDecimal.ONE);


        //THEN
        assertThrows(IllegalArgumentException.class, () -> shopService.addOrder(amounts));
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
        Map<String, BigDecimal> amounts = new HashMap<>();
        amounts.put("1", BigDecimal.ONE);
        Order addedOrder = shopService.addOrder(amounts);

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
        Map<String, BigDecimal> amounts = new HashMap<>();
        amounts.put("1", BigDecimal.ONE);
        Order addedOrder = shopService.addOrder(amounts);
        OrderStatus newStatus = OrderStatus.IN_DELIVERY;
        String orderId = addedOrder.id();

        //WHEN
        Order actual = shopService.updateOrderStatus(orderId, newStatus);

        //THEN
        Order expected = new Order(orderId, List.of(new Product("1", "Apfel", BigDecimal.TEN)), addedOrder.createdAt(), newStatus);
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
        Map<String, BigDecimal> amounts = new HashMap<>();
        amounts.put("1", BigDecimal.ONE);
        Order addedOrder = shopService.addOrder(amounts);
        OrderStatus newStatus = null;
        String orderId = addedOrder.id();

        //THEN
        assertThrows(NullPointerException.class, () -> shopService.updateOrderStatus(orderId, newStatus));
    }

    @Test
    void updateOrderStatusTest_whenNullOrderId_expectThrow() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderMapRepo(), new UUIDIdGenerator());
        Map<String, BigDecimal> amounts = new HashMap<>();
        amounts.put("1", BigDecimal.ONE);
        Order addedOrder = shopService.addOrder(amounts);
        OrderStatus newStatus = OrderStatus.IN_DELIVERY;
        String orderId = null;

        //THEN
        assertThrows(NullPointerException.class, () -> shopService.updateOrderStatus(orderId, newStatus));
    }

}
