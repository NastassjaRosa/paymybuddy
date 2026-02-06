package com.github.nastassjarosa.paymybuddy.service;

import com.github.nastassjarosa.paymybuddy.model.User;
import com.github.nastassjarosa.paymybuddy.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * Service métier responsable des relations entre utilisateurs.
 * Ajoute une relation, liste les relations et vérifie l'existence d'une relation.
 */
@Service
public class UserConnectionService {

    private final UserRepository userRepository;

    public UserConnectionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    /**
     * Ajoute un buddy à la liste de relations de l'utilisateur.
     *
     * Interdit l'ajout de soi-même et exige l'existence des deux comptes.
     *
     * @param userEmail email de l'utilisateur
     * @param buddyEmail email du buddy à ajouter
     */
    @Transactional
    public void addConnection(String userEmail, String buddyEmail) {
        // Interdiction de créer une relation avec soi-même.
        if (userEmail.equals(buddyEmail))
            throw new IllegalArgumentException("You cannot add yourself as a connection");
// Chargement de l'utilisateur.
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        // Chargement du buddy.
        User buddy = userRepository.findByEmail(buddyEmail)
                .orElseThrow(() -> new IllegalArgumentException("Buddy not found"));
        // Ajout de la relation et persistance.
        user.getConnections().add(buddy);
        userRepository.save(user);
    }
    /**
     * Vérifie si deux utilisateurs sont en relation.
     *
     * @param sender utilisateur expéditeur
     * @param receiver utilisateur destinataire
     * @return true si receiver est présent dans la liste de relations de sender
     */
    public boolean areConnected(User sender, User receiver) {
        return sender.getConnections().contains(receiver);
    }
    /**
     * Retourne la liste des relations d'un utilisateur.
     *
     * @param userEmail email de l'utilisateur
     * @return liste des relations
     */
    public List<User> listConnections(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getConnections();
    }
}
