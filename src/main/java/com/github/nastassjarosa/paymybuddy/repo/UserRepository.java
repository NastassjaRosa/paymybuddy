package com.github.nastassjarosa.paymybuddy.repo;

import com.github.nastassjarosa.paymybuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository JPA pour l'accès aux utilisateurs.
 * Permet la recherche par email et par username.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Recherche un utilisateur par email.
     *
     * @param email email unique
     * @return utilisateur optionnel
     */
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}
