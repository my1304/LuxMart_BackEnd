package luxmart_backend.services;

import luxmart_backend.dto.OrderDataDto;
import luxmart_backend.dto.OrderItemDto;
import java.util.List;

public interface OrderService {
    List<OrderItemDto> getOrdersByUserId(Integer userId);
    List<OrderDataDto> getOrderDataByOrderId(Long orderId);
}
