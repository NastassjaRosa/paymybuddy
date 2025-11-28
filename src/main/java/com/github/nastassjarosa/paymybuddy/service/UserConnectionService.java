package com.github.nastassjarosa.paymybuddy.service;

import com.github.nastassjarosa.paymybuddy.model.User;
import com.github.nastassjarosa.paymybuddy.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserConnectionService {

    private final UserRepository userRepository;

    public UserConnectionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void addConnection(String userEmail, String buddyEmail) {
        if (userEmail.equals(buddyEmail))
            throw new IllegalArgumentException("You cannot add yourself as a connection");

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        User buddy = userRepository.findByEmail(buddyEmail)
                .orElseThrow(() -> new IllegalArgumentException("Buddy not found"));

        user.getConnections().add(buddy);
        userRepository.save(user);
    }

    public boolean areConnected(User sender, User receiver) {
        return sender.getConnections().contains(receiver);
    }

    public List<User> listConnections(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getConnections();
    }
}
