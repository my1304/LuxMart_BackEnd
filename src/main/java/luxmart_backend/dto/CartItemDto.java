package luxmart_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long productId;

    private Boolean orderPay;

    @NotNull
    private Long quantity;
}

