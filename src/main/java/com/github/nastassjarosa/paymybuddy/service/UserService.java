package com.github.nastassjarosa.paymybuddy.service;

import com.github.nastassjarosa.paymybuddy.model.User;
import com.github.nastassjarosa.paymybuddy.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Transactional
    public User registerUser(String username, String email, String rawPassword) {
        if (repo.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already used");
        }
        String hash = encoder.encode(rawPassword);
        User u = new User(username, email, hash);
        return repo.save(u);
    }

    @Transactional
    public User changePassword(Integer userId, String newRawPassword) {
        User u = repo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        u.setPassword(encoder.encode(newRawPassword));
        return repo.save(u);
    }
}
