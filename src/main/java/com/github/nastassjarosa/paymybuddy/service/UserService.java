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
    public void changePassword(Integer userId, String newRawPassword) {
        User user = repo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setPassword(encoder.encode(newRawPassword));
        repo.save(user);
    }


    @Transactional
    public boolean updateProfile(String currentEmail, String newUsername, String newEmail) {

        User user = repo.findByEmail(currentEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setUsername(newUsername);

        boolean emailChanged = !newEmail.equalsIgnoreCase(user.getEmail());
        if (emailChanged) {
            repo.findByEmail(newEmail)
                    .ifPresent(u -> { throw new IllegalArgumentException("Email already used"); });
            user.setEmail(newEmail);
        }

        repo.save(user);
        return emailChanged;
    }



}
