package luxmart_backend.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luxmart_backend.domain.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders_data")
public class OrderData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "title")
    private String title;

    @Column(nullable = false)
    private Long quantity;

    @Schema(description = "Product's price", example = "123,00")
    @NotNull(message = "Product's price is mandatory")
    @Column(name = "price", nullable = false)
    private Double price;

    @Schema(description = "Product's description", example = "Product's description")
    @NotBlank(message = "Product's description is mandatory")
    @Column(name = "description", nullable = false)
    private String description;
}