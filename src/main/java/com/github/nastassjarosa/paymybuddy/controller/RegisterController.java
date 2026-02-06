package com.github.nastassjarosa.paymybuddy.controller;

import com.github.nastassjarosa.paymybuddy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 * Contrôleur MVC responsable de l'inscription via formulaire.
 * Affiche la page d'inscription et crée le compte via la couche service.
 */
@Controller
public class RegisterController {

    private final UserService userService;
    /**
     * Construit le contrôleur avec le service d'utilisateurs.
     *
     * @param userService service métier d'inscription
     */
    public RegisterController(UserService userService) {
        this.userService = userService;
    }
    /**
     * Affiche la page d'inscription.
     *
     * @return nom du template Thymeleaf
     */
    @org.springframework.web.bind.annotation.GetMapping("/register")
    public String registerPage() {
        return "register";
    }
    /**
     * Crée un utilisateur à partir du formulaire d'inscription.
     *
     * Le service applique les règles de création de compte et traite le mot de passe avant stockage.
     * Un message de statut est transmis via les attributs de redirection.
     *
     * @param username nom affiché
     * @param email email utilisé comme identifiant
     * @param password mot de passe saisi
     * @param ra attributs de redirection
     * @return redirection vers login ou retour sur register
     */
    @PostMapping("/register")
    public String doRegister(@RequestParam String username,
                             @RequestParam String email,
                             @RequestParam String password,
                             RedirectAttributes ra) {
        try {
            // Création du compte via le service : validations et persistance.
            userService.registerUser(username, email, password);
            // Message affichable après redirection vers la page de connexion.
            ra.addFlashAttribute("success", "Compte créé. Vous pouvez vous connecter.");
            return "redirect:/login";
        } catch (Exception e) {
            // Message affichable après redirection vers la page d'inscription.
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
}
