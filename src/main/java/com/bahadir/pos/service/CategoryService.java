package com.bahadir.pos.service;

import com.bahadir.pos.entity.Category;
import com.bahadir.pos.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
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
