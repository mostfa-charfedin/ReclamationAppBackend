package sicam.compltickets_backend.Services;

import java.util.List;
import java.util.Optional;

import sicam.compltickets_backend.Entities.Category;

public interface CategoryService {
    List<Category> getAllCategories();
    Optional<Category> getCategoryById(String id);
    Optional<Category> getCategoryByNom(String nom);
    Category createCategory(Category category);
    Category updateCategory(String id, Category category);
    void deleteCategory(String id);
    List<Category> getActiveCategories();
}
