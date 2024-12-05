package com.bahadir.pos.utils;

import com.bahadir.pos.entity.Category;
import com.bahadir.pos.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApiInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public ApiInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Kategoriler tablosunda kayıt var mı kontrol et
        if (categoryRepository.count() == 0) {
            // Kayıt yoksa, yeni bir kategori ekle
            Category category = new Category();
            category.setName("POS");
            categoryRepository.save(category);
            System.out.println("Default category: 'POS' created!");
        }
    }
}