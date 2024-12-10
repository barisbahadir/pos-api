package com.bahadir.pos.service;

import com.bahadir.pos.entity.user.User;
import com.bahadir.pos.entity.user.UserRole;
import com.bahadir.pos.exception.ApiException;
import com.bahadir.pos.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String email, String password){
        if(userRepository.findByEmail(email).isPresent()){
            throw new ApiException("Email is already taken!");
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .role(UserRole.USER)
                .build();

        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public boolean validatePassword(User user, String rawPassword){
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    // Tüm kullanicilari silme
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }
}
