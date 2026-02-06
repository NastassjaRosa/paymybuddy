package com.github.nastassjarosa.paymybuddy.controller;

import com.github.nastassjarosa.paymybuddy.model.User;
import com.github.nastassjarosa.paymybuddy.service.UserConnectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


import java.util.List;

/**
 * Contrôleur REST responsable de la gestion des relations entre utilisateurs.
 * Permet d'ajouter une relation et de lister les relations d'un utilisateur.
 */

@RestController
@RequestMapping("/api/connections")
public class ConnectionController {
    // La logique métier des relations est centralisée dans le service.
    // Le service applique les règles et effectue l'enregistrement en base.
    private final UserConnectionService service;
    /**
     * Construit le contrôleur avec le service de relations.
     *
     * @param service service métier des relations
     */
    public ConnectionController(UserConnectionService service) {
        this.service = service;
    }
    /**
     * Objet de requête pour ajouter une relation.
     *
     * @param buddyEmail email de l'utilisateur à ajouter en relation
     */
    public record AddConnectionRequest(String buddyEmail) {
    }
    /**
     * Ajoute une relation pour l'utilisateur connecté.
     *
     * L'utilisateur source est identifié via l'authentification.
     *
     * @param req requête contenant l'email du buddy
     * @param authentication contexte de sécurité de l'utilisateur connecté
     */
    @PostMapping
    public void add(@RequestBody AddConnectionRequest req, Authentication authentication) {
        // Identité de l'utilisateur connecté fournie par Spring Security.
        String userEmail = authentication.getName();
        // Ajout de la relation via le service : contrôles métier + persistance.
        service.addConnection(userEmail, req.buddyEmail());
    }
    /**
     * Retourne la liste des relations associées à un email.
     *
     * @param email email de l'utilisateur
     * @return liste des relations
     */
    @GetMapping("/{email}")
    public List<User> list(@PathVariable String email) {
        // Lecture des relations via le service
        return service.listConnections(email);
    }
    /**
     * Intercepte les erreurs de type IllegalArgumentException levées par la couche service.
     *
     * Cette méthode transforme une erreur métier en réponse HTTP exploitable par le client.
     * Elle évite la propagation d'une erreur interne et la génération d'une réponse 500.
     *
     * @param ex exception levée lors d'une règle métier invalide
     * @return réponse HTTP 400 contenant le message d'erreur
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        // Conversion d'une erreur métier en réponse HTTP
        return ResponseEntity.badRequest().body(new ApiError(ex.getMessage()));
    }
    /**
     * Objet de réponse représentant une erreur fonctionnelle.
     *
     * Contient uniquement le message destiné à l'affichage côté client.
     *
     * @param message description de l'erreur
     */
    public record ApiError(String message) {}

}

