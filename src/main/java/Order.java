import lombok.With;

import java.time.Instant;
import java.util.List;

@With
public record Order(
        String id,
        List<Product> products,
        Instant createdAt,
        OrderStatus status
) {
    public Order(String id, List<Product> products, OrderStatus status) {
        this(id, products, null, status);
    }
}
