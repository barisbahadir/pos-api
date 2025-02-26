package com.bahadir.pos.service;

import com.bahadir.pos.entity.authentication.AuthenticationType;
import com.bahadir.pos.entity.user.User;
import com.bahadir.pos.entity.user.UserRole;
import com.bahadir.pos.exception.ApiException;
import com.bahadir.pos.repository.UserRepository;
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
                .authType(AuthenticationType.NONE)
                .build();

        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {
            updatedUser.setId(id);
//            User existingCategory = existingCategoryOpt.get();
//            existingCategory.setName(updatedCategory.getName()); // Diğer alanlar için de benzer şekilde setter kullanabilirsiniz
//            existingCategory.setDescription(updatedCategory.getDescription());
//            existingCategory.setStatus(updatedCategory.getStatus() != null ? updatedCategory.getStatus() : BaseStatus.ENABLE);
            return userRepository.save(updatedUser);
        } else {
            throw new IllegalArgumentException("Kullanici bulunamadı: " + id);
        }
    }

//    public Optional<User> findByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }

    public Optional<User> findByEmailWithDetails(String email) {
        return userRepository.findUserByEmailWithDetails(email);
    }

    public boolean validatePassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    // Tüm kullanicilari silme
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }
}
