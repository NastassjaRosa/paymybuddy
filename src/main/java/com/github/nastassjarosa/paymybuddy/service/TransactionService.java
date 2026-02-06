package com.github.nastassjarosa.paymybuddy.service;

import com.github.nastassjarosa.paymybuddy.model.Transaction;
import com.github.nastassjarosa.paymybuddy.model.User;
import com.github.nastassjarosa.paymybuddy.repo.TransactionRepository;
import com.github.nastassjarosa.paymybuddy.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * Service métier responsable des transferts et du calcul de solde.
 * Valide les règles de transfert, enregistre la transaction et journalise l'opération.
 */
@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final UserConnectionService connectionService;

    private final BillingService billingService;

    public TransactionService(UserRepository userRepository,
                              TransactionRepository transactionRepository,
                              UserConnectionService connectionService,
                              BillingService billingService) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.connectionService = connectionService;
        this.billingService = billingService;
    }

    /**
     * Effectue un transfert d'argent entre deux utilisateurs.
     *
     * Valide le montant, interdit l'auto-transfert, vérifie l'existence des utilisateurs,
     * vérifie la relation, contrôle le solde disponible, persiste la transaction et journalise l'opération.
     *
     * @param senderEmail email de l'expéditeur
     * @param receiverEmail email du destinataire
     * @param amount montant du transfert
     * @param description description associée
     * @return transaction persistée
     */
    @Transactional
    public Transaction sendMoney(String senderEmail, String receiverEmail, double amount, String description) {
// Validation du montant.
        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be > 0");
// Interdiction d'envoyer de l'argent à soi-même.
        if (senderEmail.equals(receiverEmail))
            throw new IllegalArgumentException("Cannot send money to yourself");
        // Chargement de l'expéditeur.
        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        // Chargement du destinataire.
        User receiver = userRepository.findByEmail(receiverEmail)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));
        // Vérification de la relation entre les deux utilisateurs.
        if (!connectionService.areConnected(sender, receiver))
            throw new IllegalArgumentException("Users are not connected");
// Construction de la transaction.
        Transaction tx = new Transaction(sender, receiver, amount, description);
        // Contrôle du solde disponible de l'expéditeur avant enregistrement.
        double balance = getBalance(sender.getId());
        if (balance < amount) {
            throw new IllegalStateException("Insufficient balance");
        }
// Persistance de la transaction.
        Transaction saved = transactionRepository.save(tx);
        billingService.logPayment(saved);
        return saved;

    }
    /**
     * Retourne l'historique des transactions envoyées et reçues par un utilisateur.
     *
     * @param userEmail email de l'utilisateur
     * @return liste des transactions
     */
    public List<Transaction> getUserHistory(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
// Lecture des transactions où l'utilisateur est expéditeur ou destinataire.
        return transactionRepository.findBySenderOrReceiver(user, user);
    }
    /**
     * Calcule le solde d'un utilisateur.
     *
     * Solde = total reçu - total envoyé.
     *
     * @param userId identifiant de l'utilisateur
     * @return solde calculé
     */
    public double getBalance(Integer userId) {
        return transactionRepository.sumReceived(userId) - transactionRepository.sumSent(userId);
    }


}
