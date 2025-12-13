package sicam.compltickets_backend.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import sicam.compltickets_backend.Entities.Category;
import sicam.compltickets_backend.Repositories.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(String id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Optional<Category> getCategoryByNom(String nom) {
        return categoryRepository.findByNom(nom);
    }

    @Override
    public Category createCategory(Category category) {
        if (categoryRepository.findByNom(category.getNom()).isPresent()) {
            throw new RuntimeException("Catégorie avec le nom '" + category.getNom() + "' existe déjà");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(String id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée: " + id));

        category.setNom(categoryDetails.getNom());
        category.setDescription(categoryDetails.getDescription());
        category.setActive(categoryDetails.isActive());

        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(String id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Catégorie non trouvée: " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Category> getActiveCategories() {
        return categoryRepository.findByActive(true);
    }
}
