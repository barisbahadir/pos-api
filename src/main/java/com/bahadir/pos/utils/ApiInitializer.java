package com.bahadir.pos.utils;

import com.bahadir.pos.entity.category.Category;
import com.bahadir.pos.entity.user.User;
import com.bahadir.pos.entity.user.UserRole;
import com.bahadir.pos.repository.CategoryRepository;
import com.bahadir.pos.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ApiInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiInitializer(CategoryRepository categoryRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        // Kullanicilar tablosunda kayıt var mı kontrol et
        if (userRepository.count() == 0) {
            // Kayıt yoksa, yeni bir kullanici ekle
            User user = new User();
            user.setEmail("bahadir");
            user.setPassword(passwordEncoder.encode("bahadir"));
            user.setRole(UserRole.USER);
            userRepository.save(user);
            System.out.println("Default user: 'BAHADIR' created!");
        }
    }
}