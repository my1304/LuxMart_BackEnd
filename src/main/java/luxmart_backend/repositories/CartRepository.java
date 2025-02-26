package luxmart_backend.repositories;

import luxmart_backend.domain.User;
import luxmart_backend.models.Cart;
import luxmart_backend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUser(User user);
    Optional<Cart> findByProductAndUser(Product product, User user);
}