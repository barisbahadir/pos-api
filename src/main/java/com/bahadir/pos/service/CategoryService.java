package com.bahadir.pos.service;

import com.bahadir.pos.entity.Category;
import com.bahadir.pos.entity.OrderUpdateItemDto;
import com.bahadir.pos.repository.CategoryRepository;
import com.bahadir.pos.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository, EntityManager entityManager) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.entityManager = entityManager;
    }

    // Kategorileri listele
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Yeni kategori oluştur
    public Category createCategory(Category category) {
        category.setOrderValue(1);
        return categoryRepository.save(category);
    }

    // Kategori güncelleme
    public Category updateCategory(Long id, Category updatedCategory) {
        Optional<Category> existingCategoryOpt = categoryRepository.findById(id);

        if (existingCategoryOpt.isPresent()) {
            Category existingCategory = existingCategoryOpt.get();
            existingCategory.setName(updatedCategory.getName()); // Diğer alanlar için de benzer şekilde setter kullanabilirsiniz
            return categoryRepository.save(existingCategory);
        } else {
            throw new IllegalArgumentException("Kategori bulunamadı: " + id);
        }
    }

    @Transactional
    public Boolean updateOrderValues(List<OrderUpdateItemDto> updates) {

        Boolean hasNullValues = updates.stream()
                .anyMatch(update -> update.getId() == null || update.getOrderValue() == null);

        if (hasNullValues || updates == null || updates.isEmpty()) {
            return false;
        } else {
            StringBuilder query = new StringBuilder("UPDATE category SET order_value = CASE ");
            List<Long> ids = new ArrayList<>();

            for (OrderUpdateItemDto update : updates) {
                query.append("WHEN id = ").append(update.getId()).append(" THEN ").append(update.getOrderValue()).append(" ");
                ids.add(update.getId());
            }

            // Verilmeyen id'ler için order_value'yu 1 olarak ayarlama
            query.append("ELSE 1 ");
            query.append("END");

            // Sorguyu çalıştır
            Query nativeQuery = entityManager.createNativeQuery(query.toString());
            nativeQuery.executeUpdate();

            return true;
        }
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        Optional<Category> existingCategoryOpt = categoryRepository.findById(categoryId);

        if (existingCategoryOpt.isPresent()) {

            // Kategoriye ait tüm ürünleri sil
            productRepository.deleteByCategoryId(categoryId);

            // Kategoriyi sil
            categoryRepository.deleteById(categoryId);
        } else {
            throw new IllegalArgumentException("Kategori bulunamadı: " + categoryId);
        }
    }

    // Tüm kategorileri silme
    public void deleteAllCategories() {
        categoryRepository.deleteAll();
    }
}
