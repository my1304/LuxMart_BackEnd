package luxmart_backend.repositories;

import luxmart_backend.domain.User;
import luxmart_backend.models.Cart;
import luxmart_backend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
   // Optional<Product> findByProductId(Long  ProductId);
    List<Product> findByTitleContainingIgnoreCase(String title);
    List<Product> findByCategory_Id(Long categoryId);
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
    List<Product> findProductsByTitleAndPriceBetween(String title, Double minPrice, Double maxPrice);
}
