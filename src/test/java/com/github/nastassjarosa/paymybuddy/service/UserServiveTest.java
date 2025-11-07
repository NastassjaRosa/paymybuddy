package com.github.nastassjarosa.paymybuddy.service;

import com.github.nastassjarosa.paymybuddy.model.User;
import com.github.nastassjarosa.paymybuddy.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for UserService: verifies that passwords are hashed before save.
 */
class UserServiceTest {

    @Test
    void registerUser_hashesPasswordAndSaves() {
        // Arrange
        UserRepository repo = mock(UserRepository.class);
        when(repo.findByEmail(anyString())).thenReturn(Optional.empty());
        // return the same user instance when save is called
        when(repo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        UserService svc = new UserService(repo, encoder);

        String rawPassword = "Secret123!";

        // Act
        User created = svc.registerUser("alice", "alice@example.com", rawPassword);

        // Assert
        assertNotNull(created);
        assertNotNull(created.getPassword(), "Le champ password ne doit pas être nul après l'enregistrement");
        assertNotEquals(rawPassword, created.getPassword(), "Le mot de passe en base ne doit pas être le mot de passe en clair");
        assertTrue(encoder.matches(rawPassword, created.getPassword()), "Le hash doit correspondre au mot de passe d'origine");

        // verify save called
        verify(repo, times(1)).save(any(User.class));
    }
}
