package luxmart_backend.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luxmart_backend.domain.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "absender")
    private String absender;

    @Column(name = "post")
    private String post;

    @Column(name = "name_bank")
    private String nameBank;

    @Column(name = "carte")
    private String carte;

    @Column(name = "date_order")
    private LocalDateTime dateOrder;

    @Schema(description = "Order's sum", example = "123.00")
    @NotNull(message = "Order's price is mandatory")
    @Column(name = "sum_order", nullable = false)
    private Double sumOrder;
}