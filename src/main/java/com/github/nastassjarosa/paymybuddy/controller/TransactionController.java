package com.github.nastassjarosa.paymybuddy.controller;

import com.github.nastassjarosa.paymybuddy.model.Transaction;
import com.github.nastassjarosa.paymybuddy.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * Contrôleur REST responsable des opérations liées aux transactions.
 * Expose l'envoi d'argent et la consultation de l'historique.
 */
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService service;
    /**
     * Construit le contrôleur avec le service de transactions.
     *
     * @param service service métier des transactions
     */
    public TransactionController(TransactionService service) {

        this.service = service;
    }
    /**
     * Objet de requête pour initier un transfert.
     *
     * @param senderEmail email de l'expéditeur
     * @param receiverEmail email du destinataire
     * @param amount montant à transférer
     * @param description description associée
     */
    public record SendRequest(String senderEmail, String receiverEmail, double amount, String description) {
    }
    /**
     * Effectue un transfert d'argent entre deux utilisateurs.
     *
     * Les validations métier et l'enregistrement de la transaction sont réalisés par la couche service.
     *
     * @param req requête de transfert
     * @return réponse HTTP confirmant l'opération
     */
    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody SendRequest req) {
        // Le service applique les règles de transfert puis persiste la transaction.
        Transaction tx = service.sendMoney(req.senderEmail(), req.receiverEmail(), req.amount(), req.description());
        return ResponseEntity.ok("Transaction completed. ID=" + tx.getId());
    }
    /**
     * Retourne l'historique des transactions d'un utilisateur.
     *
     * @param email email de l'utilisateur
     * @return liste des transactions envoyées et reçues
     */
    @GetMapping("/history/{email}")
    public ResponseEntity<?> history(@PathVariable String email) {
        // Lecture via le service : centralisation de l'accès aux données et des règles de filtre.
        return ResponseEntity.ok(service.getUserHistory(email));
    }
}
