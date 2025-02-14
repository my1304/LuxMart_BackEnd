package luxmart_backend.services;

import luxmart_backend.models.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(Long id);
    Category saveCategory(Category category);
    void deleteCategory(Long id);
    List<Category> findCategoriesByName(String name);
}