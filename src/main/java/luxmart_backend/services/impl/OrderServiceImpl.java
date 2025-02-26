package luxmart_backend.services.impl;

import lombok.RequiredArgsConstructor;
import luxmart_backend.domain.User;
import luxmart_backend.dto.OrderDataDto;
import luxmart_backend.dto.OrderItemDto;
import luxmart_backend.models.Order;
import luxmart_backend.models.OrderData;
import luxmart_backend.repositories.*;
import luxmart_backend.services.OrderService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderDataRepository orderDataRepository;

    @Override
    public List<OrderItemDto> getOrdersByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Order> orders = orderRepository.findByUser(user);

        return orders.stream()
                .map(order -> new OrderItemDto(order.getId(), order.getUser().getId(),
                        order.getAbsender(),order.getPost(), order.getNameBank(), order.getCarte(),
                        order.getDateOrder(), order.getSumOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDataDto> getOrderDataByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        List<OrderData> orderDatas = orderDataRepository.findByOrder(order);

        return orderDatas.stream()
                .map(orderData -> new OrderDataDto(orderData.getId(), orderData.getOrder().getId(),
                        orderData.getProduct().getId(), orderData.getTitle(), orderData.getQuantity(),
                        orderData.getPrice(), orderData.getDescription()))
                .collect(Collectors.toList());
    }
}
