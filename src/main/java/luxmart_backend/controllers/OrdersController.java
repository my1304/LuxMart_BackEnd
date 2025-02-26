package luxmart_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import luxmart_backend.dto.OrderDataDto;
import luxmart_backend.dto.OrderItemDto;
import luxmart_backend.services.CartService;
import luxmart_backend.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Orders Controller", description = "Controller for managing orders items")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/order")
public class OrdersController {
    private final OrderService ordersService;

    @Operation(summary = "Get all order's user items ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of order items returned successfully"),
            @ApiResponse(responseCode = "404", description = "No items found in order")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderItemDto>> getOrdersWithDetails(@PathVariable Integer userId)  {
        // Получение основной информации о заказе
        return ResponseEntity
            .ok(ordersService.getOrdersByUserId(userId));
    }

    @Operation(summary = "Get all orderdata's order items ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of orderDatas items returned successfully"),
            @ApiResponse(responseCode = "404", description = "No items found in order")
    })
    @GetMapping("/data/{order_id}")
    public ResponseEntity<List<OrderDataDto>> getOrderDatasWithDetails(@PathVariable("order_id") Long orderId) {
        // Получение вспомогательной информации о заказе
        return ResponseEntity
                .ok(ordersService.getOrderDataByOrderId(orderId));
    }
}