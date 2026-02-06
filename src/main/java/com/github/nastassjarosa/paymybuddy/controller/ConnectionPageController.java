package com.github.nastassjarosa.paymybuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Contrôleur MVC responsable de l'affichage de la page "connections".
 */
@Controller
public class ConnectionPageController {
    /**
     * Retourne la vue Thymeleaf de la page d'ajout de relations.
     *
     * @return nom du template
     */
    @GetMapping("/connections")
    public String connectionsPage() {
        // Le rendu est géré par Thymeleaf via le template "connections".
        return "connections";
    }
}

