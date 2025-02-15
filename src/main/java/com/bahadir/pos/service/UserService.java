package com.bahadir.pos.service;

import com.bahadir.pos.entity.user.User;
import com.bahadir.pos.entity.user.UserRole;
import com.bahadir.pos.exception.ApiException;
import com.bahadir.pos.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User registerUser(String email, String username, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new ApiException("Email is already taken!");
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .email(email)
                .username(username)
                .password(encodedPassword)
                .authRole(UserRole.USER)
                .build();

        return userRepository.save(user);
    }

//    public Optional<User> findByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }

    @Cacheable(value = "users", key = "#email")
    public Optional<User> findByEmailWithDetails(String email) {
        return userRepository.findUserByEmailWithDetails(email);
    }

    public boolean validatePassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    // Tüm kullanicilari silme
    @CacheEvict(value = "users")
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }
}
