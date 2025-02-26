package luxmart_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartFullItemDto {
    //Таблица Cart
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long productId;

    private Boolean orderPay;

    @NotNull
    private Long quantity;

    //Таблица Product
    private String title;

    private Double price;

    private String description;

    private Long categoryId;

    private List<String> imageUrls;

    //Таблица Category
    private String categoryName;
}