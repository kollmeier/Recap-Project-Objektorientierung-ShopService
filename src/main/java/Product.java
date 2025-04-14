import lombok.With;

import java.math.BigDecimal;

@With
public record Product(
        String id,
        String name,
        BigDecimal quantity
) {
    public Product(String id, String name) {
        this(id, name, BigDecimal.ZERO);
    }
}
