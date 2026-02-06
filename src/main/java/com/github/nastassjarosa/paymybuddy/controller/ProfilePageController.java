package com.github.nastassjarosa.paymybuddy.controller;

import com.github.nastassjarosa.paymybuddy.model.User;
import com.github.nastassjarosa.paymybuddy.repo.UserRepository;
import com.github.nastassjarosa.paymybuddy.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Contrôleur MVC responsable de l'affichage et de la mise à jour du profil utilisateur.
 */
@Controller
public class ProfilePageController {

    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * Construit le contrôleur avec les dépendances nécessaires.
     *
     * @param userRepository accès aux utilisateurs
     * @param userService    service métier utilisateur
     */
    public ProfilePageController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * Affiche la page profil de l'utilisateur connecté.
     *
     * @param authentication utilisateur authentifié
     * @param model          modèle Thymeleaf
     * @return nom du template Thymeleaf
     */
    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        String email = authentication.getName();
        // Lecture du profil à partir de l'identité authentifiée.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
// Données utilisateur exposées à la vue.
        model.addAttribute("user", user);
        return "profile";
    }


    /**
     * Met à jour le profil de l'utilisateur connecté.
     * <p>
     * Une modification de l'email entraîne une déconnexion afin de réinitialiser l'identité de session.
     *
     * @param auth     utilisateur authentifié
     * @param username nouveau nom affiché
     * @param email    nouvel email
     * @return redirection vers profil ou déconnexion
     */
    @PostMapping("/profile")
    public String updateProfile(Authentication auth,
                                @RequestParam String username,
                                @RequestParam String email) {
// Mise à jour via le service : validations et persistance.
        boolean emailChanged = userService.updateProfile(auth.getName(), username, email);
// Déconnexion si l'identifiant de connexion a changé.
        if (emailChanged) {
            return "redirect:/logout";
        }

        return "redirect:/profile?success";
    }


}
