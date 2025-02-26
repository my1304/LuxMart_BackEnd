package luxmart_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDataDto {
    private Long id;
    @NotNull
    private Long orderId;
    @NotNull
    private Long productId;
    private String title;
    @NotNull
    private Long quantity;
    private Double price;
    private String description;
}

