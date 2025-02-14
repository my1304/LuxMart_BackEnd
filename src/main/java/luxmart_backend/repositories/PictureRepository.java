package luxmart_backend.repositories;

import luxmart_backend.models.Picture;
import luxmart_backend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PictureRepository extends JpaRepository<Picture, Long> {
    Set<Picture> findByProduct(Product product);
}