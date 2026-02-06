package com.github.nastassjarosa.paymybuddy.repo;

import com.github.nastassjarosa.paymybuddy.model.Transaction;
import com.github.nastassjarosa.paymybuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Repository JPA pour l'accès aux transactions.
 * Fournit des méthodes de lecture et des agrégations utilisées pour le calcul du solde.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findBySender(User sender);

    List<Transaction> findByReceiver(User receiver);

    List<Transaction> findBySenderOrReceiver(User sender, User receiver);
    /**
     * Calcule le total des montants reçus par un utilisateur.
     *
     * @param userId identifiant de l'utilisateur
     * @return somme des montants reçus
     */
    @org.springframework.data.jpa.repository.Query("""
    SELECT COALESCE(SUM(t.amount), 0)
    FROM Transaction t
    WHERE t.receiver.id = :userId
""")
    double sumReceived(@org.springframework.data.repository.query.Param("userId") Integer userId);
    /**
     * Calcule le total des montants envoyés par un utilisateur.
     *
     * @param userId identifiant de l'utilisateur
     * @return somme des montants envoyés
     */
    @org.springframework.data.jpa.repository.Query("""
    SELECT COALESCE(SUM(t.amount), 0)
    FROM Transaction t
    WHERE t.sender.id = :userId
""")
    double sumSent(@org.springframework.data.repository.query.Param("userId") Integer userId);


}

