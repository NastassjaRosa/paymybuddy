package com.github.nastassjarosa.paymybuddy.controller;

import com.github.nastassjarosa.paymybuddy.model.User;
import com.github.nastassjarosa.paymybuddy.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST responsable de l'inscription des utilisateurs.
 * Expose un endpoint dédié à la création de compte.
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    // La logique d'inscription est centralisée dans le service.
    // Le service gère la création de l'utilisateur et le traitement sécurisé du mot de passe.
    private final UserService userService;

    /**
     * Construit le contrôleur avec le service d'utilisateurs.
     *
     * @param userService service métier d'inscription
     */

    public AuthController(UserService userService) {

        this.userService = userService;
    }

    /**
     * Crée un nouvel utilisateur à partir des données d'inscription.
     * <p>
     * Le mot de passe n'est pas stocké tel quel : il est transformé par la couche service
     * avant d'être enregistré en base de données.
     *
     * @param req données d'inscription
     * @return réponse HTTP confirmant la création
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        // Création du compte via le service : validations + préparation du mot de passe + persistance.
        User created = userService.registerUser(req.username(), req.email(), req.password());
        return ResponseEntity.ok("User created with id=" + created.getId());
    }

    /**
     * Requête JSON attendue pour enregistrer un utilisateur.
     * <p>
     * Champs :
     * - username : nom affiché
     * - email : identifiant unique
     * - password : mot de passe en clair côté client (sera haché côté serveur via UserService)
     */
    public static record RegisterRequest(String username, String email, String password) {
    }
}
