package com.bahadir.pos.service;

import com.bahadir.pos.entity.Category;
import com.bahadir.pos.entity.OrderUpdateDto;
import com.bahadir.pos.repository.CategoryRepository;
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
    private final EntityManager entityManager;

    public CategoryService(CategoryRepository categoryRepository, EntityManager entityManager) {
        this.categoryRepository = categoryRepository;
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
    public Boolean updateOrderValues(List<OrderUpdateDto> updates) {

        Boolean hasNullValues = updates.stream()
                .anyMatch(update -> update.getId() == null || update.getOrderValue() == null);

        if (!hasNullValues) {
            // Sorguyu dinamik oluştur
            StringBuilder query = new StringBuilder("UPDATE categories SET order_value = CASE ");
            List<Long> ids = new ArrayList<>();

            for (OrderUpdateDto update : updates) {
                query.append("WHEN id = ").append(update.getId()).append(" THEN ").append(update.getOrderValue()).append(" ");
                ids.add(update.getId());
            }

            query.append("END WHERE id IN (:ids)");

            // Sorguyu çalıştır
            Query nativeQuery = entityManager.createNativeQuery(query.toString());
            nativeQuery.setParameter("ids", ids);
            nativeQuery.executeUpdate();

            return true;
        } else {
            return false;
        }
    }

    // Kategori silme
    public void deleteCategory(Long id) {
        Optional<Category> existingCategoryOpt = categoryRepository.findById(id);

        if (existingCategoryOpt.isPresent()) {
            categoryRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Kategori bulunamadı: " + id);
        }
    }

    // Tüm kategorileri silme
    public void deleteAllCategories() {
        categoryRepository.deleteAll();
    }
}
