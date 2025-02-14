package luxmart_backend.repositories;

import luxmart_backend.models.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PicturesRepository extends JpaRepository<Picture, Long> {
    @Query("SELECT p.url FROM Picture p WHERE p.product.id = :productId")
    List<String> findUrlsByProductId(@Param("productId") Long productId);
}
