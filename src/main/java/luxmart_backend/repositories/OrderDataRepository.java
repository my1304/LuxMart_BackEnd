package luxmart_backend.repositories;

import luxmart_backend.models.Order;
import luxmart_backend.models.OrderData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDataRepository extends JpaRepository<OrderData, Long> {
    List<OrderData> findByOrder(Order order);
}