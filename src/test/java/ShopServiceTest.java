import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void addOrderTest() {
        //GIVEN
        ShopService shopService = new ShopService();
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
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1", "2");

        //THEN
        assertThrows(IllegalArgumentException.class, () -> shopService.addOrder(productsIds));
    }

    @Test
    void getOrdersByOrderStatusTest_whenNoOrdersWithStatus_expectEmptyList() {
        //GIVEN
        ShopService shopService = new ShopService();
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
        ShopService shopService = new ShopService();
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
}
