package luxmart_backend.services.impl;

import luxmart_backend.models.Category;
import luxmart_backend.models.Product;
import luxmart_backend.repositories.CategoryRepository;
import luxmart_backend.repositories.PictureRepository;
import luxmart_backend.repositories.ProductRepository;
import luxmart_backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final PictureRepository pictureRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, PictureRepository pictureRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.pictureRepository = pictureRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
       // Object obj = picturesRepository.findById(1L);
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.orElse(null);
    }
    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> filterProducts(String title, Double price, Double price_min, Double price_max, Long categoryId) {
        List<Product> products = productRepository.findAll();

        // Фильтрация по названию
        if (title != null) {
            products = products.stream()
                    .filter(product -> product.getTitle().toLowerCase().contains(title.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Фильтрация по цене
        if (price != null) {
            products = products.stream()
                    .filter(product -> product.getPrice() == price)
                    .collect(Collectors.toList());
        }

        // Фильтрация по диапазону цен
        if (price_min != null && price_max != null) {
            products = products.stream()
                    .filter(product -> product.getPrice() >= price_min && product.getPrice() <= price_max)
                    .collect(Collectors.toList());
        }

        // Фильтрация по категории
        if (categoryId != null) {
            products = products.stream()
                    .filter(product -> product.getCategory().getId() == categoryId)
                    .collect(Collectors.toList());
        }

        return products;
    }


}
