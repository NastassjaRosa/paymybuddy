package com.github.nastassjarosa.paymybuddy.controller;


import com.github.nastassjarosa.paymybuddy.service.TransactionService;
import com.github.nastassjarosa.paymybuddy.service.UserConnectionService;
import com.github.nastassjarosa.paymybuddy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

/**
 * Contrôleur MVC responsable de la page de transfert d'argent.
 * <p>
 * Gère l'affichage du formulaire de paiement entre utilisateurs connectés.
 * Charge les relations disponibles et l'historique des transactions de l'utilisateur courant.
 * Délègue la logique métier de transfert, de validation du solde et d'enregistrement
 * à la couche service.
 */
@Controller
public class TransferPageController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final UserConnectionService userConnectionService;

    /**
     * Contrôleur MVC responsable de l'affichage de la page de transfert et du traitement du formulaire.
     * Centralise l'accès aux données de la page : relations et historique.
     */
    public TransferPageController(TransactionService transactionService, UserService userService, UserConnectionService userConnectionService) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.userConnectionService = userConnectionService;
    }

    /**
     * Affiche la page de transfert.
     * <p>
     * Charge l'email courant, la liste des relations et l'historique des transactions.
     * Transporte les messages de statut via des paramètres de requête.
     *
     * @param model     modèle Thymeleaf
     * @param principal utilisateur authentifié
     * @param error     message d'erreur
     * @param success   indicateur de succès
     * @return nom du template Thymeleaf
     */
    @GetMapping("/transfer")
    public String transferPage(Model model,
                               Principal principal,
                               @RequestParam(required = false) String error,
                               @RequestParam(required = false) String success) {
        // Identité de l'utilisateur connecté.
        String currentEmail = principal.getName();
        // Données nécessaires à l'affichage du formulaire et du tableau.
        model.addAttribute("currentEmail", currentEmail);
        model.addAttribute("relations", userConnectionService.listConnections(currentEmail));
        model.addAttribute("history", transactionService.getUserHistory(currentEmail));

        // Messages affichables sur la page.
        if (error != null) {
            model.addAttribute("error", error);
        }
        if (success != null) {
            model.addAttribute("success", true);
        }

        return "transfer";
    }

    /**
     * Traite la demande de transfert issue du formulaire.
     * <p>
     * Délègue les validations et l'enregistrement à la couche service.
     * Retourne toujours une redirection afin d'éviter un re-post du formulaire.
     *
     * @param receiverEmail email du destinataire
     * @param amount        montant à transférer
     * @param description   description optionnelle
     * @param principal     utilisateur authentifié
     * @return redirection vers la page de transfert avec statut
     */
    @PostMapping("/transfer")
    public String doTransfer(@RequestParam String receiverEmail,
                             @RequestParam double amount,
                             @RequestParam(required = false) String description,
                             Principal principal) {
        // L'expéditeur est l'utilisateur connecté.
        String senderEmail = principal.getName();

        try {
            // Exécution du transfert : contrôles métier, calcul du solde, persistance, journalisation.
            transactionService.sendMoney(senderEmail, receiverEmail, amount, description);
            return "redirect:/transfer?success";
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Conversion d'une erreur métier en message affichable.
            return "redirect:/transfer?error=" + java.net.URLEncoder.encode(e.getMessage(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }

}
