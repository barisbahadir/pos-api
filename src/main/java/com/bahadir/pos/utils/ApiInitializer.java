package com.bahadir.pos.utils;

import com.bahadir.pos.entity.category.Category;
import com.bahadir.pos.entity.user.User;
import com.bahadir.pos.entity.user.AuthRole;
import com.bahadir.pos.repository.CategoryRepository;
import com.bahadir.pos.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
            List<User> defaultUsers = new ArrayList<>();
            defaultUsers.add(User
                    .builder()
                    .email("admin")
                    .password(passwordEncoder.encode("bb377261"))
                    .authRole(AuthRole.ADMIN)
                    .build());
            defaultUsers.add(User
                    .builder()
                    .email("bahadir")
                    .password(passwordEncoder.encode("bahadir"))
                    .authRole(AuthRole.USER)
                    .build());
            defaultUsers.add(User
                    .builder()
                    .email("zeliha")
                    .password(passwordEncoder.encode("zeliha"))
                    .authRole(AuthRole.USER)
                    .build());
            userRepository.saveAll(defaultUsers);
            System.out.println("Default users: " + defaultUsers.stream().map(User::getEmail).toList() + " created!");
        }
    }
}