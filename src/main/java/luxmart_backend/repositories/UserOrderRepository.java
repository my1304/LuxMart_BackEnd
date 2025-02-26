package luxmart_backend.repositories;

import luxmart_backend.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOrderRepository extends JpaRepository<Order, Long> {
}