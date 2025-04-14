import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class TransactionProcessor {
    private final ShopService shopService;
    private final Map<String, Order> aliasedOrders = new HashMap<>();

    public void reset() {
        aliasedOrders.clear();
    }

    public void process(final String transactions) {
        String[] lines = transactions.split("\n");
        for (String line : lines) {
            processTransaction(line);
        }
        reset();
    }

    public void processTransaction(final String transaction) {
        String[] parts = transaction.split(" ");
        String command = parts[0];

        switch (command) {
            case "addOrder":
                processAddOrder(parts);
                break;
            case "setStatus":
                processSetStatus(parts);
                break;
            case "printOrders":
                printOrders();
                break;
            default:
                throw new RuntimeException("Unknown command: " + command);
        }
    }

    private void processAddOrder(final String[] parts) {
        if (parts.length < 3) {
            throw new RuntimeException("Not enough arguments for addOrder command");
        }
        String alias = parts[1];
        String[] productIds = new String[parts.length - 2];
        System.arraycopy(parts, 2, productIds, 0, parts.length - 2);
        Order order = shopService.addOrder(List.of(productIds));
        aliasedOrders.put(alias, order);
    }

    private void processSetStatus(final String[] parts) {
        if (parts.length != 3) {
            throw new RuntimeException("Invalid number of arguments for setStatus command");
        }
        String alias = parts[1];
        String status = parts[2];
        Order order = aliasedOrders.get(alias);
        if (order == null) {
            throw new RuntimeException("Order with alias " + alias + " not found");
        }
        shopService.updateOrderStatus(order.id(), OrderStatus.valueOf(status));

    }

    public void printOrders() {
        for (Order order : aliasedOrders.values()) {
            System.out.println(order);
        }
    }
}
