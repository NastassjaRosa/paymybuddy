package com.github.nastassjarosa.paymybuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * Contrôleur MVC responsable de l'affichage de la page de connexion.
 */
@Controller
public class LoginController {
    /**
     * Retourne la vue Thymeleaf de la page de connexion.
     *
     * L'authentification est gérée par Spring Security.
     *
     * @return nom du template
     */
    @GetMapping("/login")
    public String loginPage() {
        // Le contrôleur renvoie uniquement la vue, sans logique d'authentification.
        return "login";
    }
}
