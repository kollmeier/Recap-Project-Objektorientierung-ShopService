import lombok.With;

import java.time.ZonedDateTime;
import java.util.List;

@With
public record Order(
        String id,
        List<Product> products,
        ZonedDateTime createdAt,
        OrderStatus status
) {
    public Order(String id, List<Product> products, OrderStatus status) {
        this(id, products, null, status);
    }
}
