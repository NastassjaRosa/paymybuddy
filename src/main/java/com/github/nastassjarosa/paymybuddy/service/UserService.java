package com.github.nastassjarosa.paymybuddy.service;

import com.github.nastassjarosa.paymybuddy.model.User;
import com.github.nastassjarosa.paymybuddy.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service métier responsable des opérations sur les utilisateurs.
 * Crée les comptes, met à jour le profil et modifie le mot de passe.
 */
@Service
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }
    /**
     * Crée un utilisateur après contrôle de l'unicité de l'email.
     * Transforme le mot de passe avant stockage.
     *
     * @param username nom affiché
     * @param email email unique
     * @param rawPassword mot de passe saisi
     * @return utilisateur persisté
     */
    @Transactional
    public User registerUser(String username, String email, String rawPassword) {
        // Contrôle d'unicité de l'email.
        if (repo.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already used");
        }
        // Transformation du mot de passe avant stockage.
        String hash = encoder.encode(rawPassword);
        // Création et persistance.
        User u = new User(username, email, hash);
        return repo.save(u);
    }
    /**
     * Modifie le mot de passe d'un utilisateur.
     * Transforme le mot de passe avant stockage.
     *
     * @param userId identifiant de l'utilisateur
     * @param newRawPassword nouveau mot de passe saisi
     */
    @Transactional
    public void changePassword(Integer userId, String newRawPassword) {
        User user = repo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
// Remplacement du hash stocké.
        user.setPassword(encoder.encode(newRawPassword));
        repo.save(user);
    }

    /**
     * Met à jour le profil d'un utilisateur identifié par son email courant.
     * Met à jour le nom affiché et, si nécessaire, l'email après contrôle d'unicité.
     *
     * @param currentEmail email actuel
     * @param newUsername nouveau nom affiché
     * @param newEmail nouvel email
     * @return true si l'email a été modifié
     */
    @Transactional
    public boolean updateProfile(String currentEmail, String newUsername, String newEmail) {

        User user = repo.findByEmail(currentEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
// Mise à jour du nom affiché.
        user.setUsername(newUsername);

        boolean emailChanged = !newEmail.equalsIgnoreCase(user.getEmail());
        if (emailChanged) {
            // Contrôle d'unicité du nouvel email.
            repo.findByEmail(newEmail)
                    .ifPresent(u -> { throw new IllegalArgumentException("Email already used"); });
            user.setEmail(newEmail);
        }

        repo.save(user);
        return emailChanged;
    }



}
