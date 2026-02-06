package com.github.nastassjarosa.paymybuddy.controller;

import com.github.nastassjarosa.paymybuddy.model.User;
import com.github.nastassjarosa.paymybuddy.repo.UserRepository;
import com.github.nastassjarosa.paymybuddy.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * Contrôleur MVC responsable du changement de mot de passe.
 * Affiche le formulaire et applique la modification via la couche service.
 */
@Controller
public class PasswordController {

    private final UserRepository userRepository;
    private final UserService userService;
    /**
     * Construit le contrôleur avec les dépendances nécessaires.
     *
     * @param userRepository accès aux utilisateurs
     * @param userService service métier utilisateur
     */
    public PasswordController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }
    /**
     * Affiche la page de changement de mot de passe.
     *
     * @return nom du template Thymeleaf
     */
    @GetMapping("/profile/password")
    public String form() {
        return "change-password";
    }


    /**
     * Modifie le mot de passe de l'utilisateur connecté puis force une déconnexion.
     *
     * Le mot de passe est traité par la couche service avant d'être enregistré.
     * La déconnexion invalide la session en cours après modification.
     *
     * @param auth utilisateur authentifié
     * @param password nouveau mot de passe
     * @param request requête HTTP
     * @param response réponse HTTP
     * @return redirection vers la page de connexion
     */
    @PostMapping("/profile/password")
    public String changePassword(Authentication auth,
                                 @RequestParam String password,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
// Chargement de l'utilisateur courant à partir de l'identité Spring Security.
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        // Mise à jour via le service : traitement du mot de passe et persistance.
        userService.changePassword(user.getId(), password);
// Invalidation de la session après changement de mot de passe.
        new SecurityContextLogoutHandler().logout(request, response, null);

        return "redirect:/login?passwordChanged";
    }
}
