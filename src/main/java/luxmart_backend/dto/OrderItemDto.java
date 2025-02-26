package luxmart_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Long id;

    @NotNull
    private Long userId;
    private String absender;
    private String post;
    private String nameBank;
    private String carte;
    private LocalDateTime dateOrder;
    private Double sumOrder;
}