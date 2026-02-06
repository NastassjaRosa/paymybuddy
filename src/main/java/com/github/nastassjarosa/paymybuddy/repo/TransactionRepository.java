package com.github.nastassjarosa.paymybuddy.repo;

import com.github.nastassjarosa.paymybuddy.model.Transaction;
import com.github.nastassjarosa.paymybuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findBySender(User sender);

    List<Transaction> findByReceiver(User receiver);

    List<Transaction> findBySenderOrReceiver(User sender, User receiver);

    @org.springframework.data.jpa.repository.Query("""
    SELECT COALESCE(SUM(t.amount), 0)
    FROM Transaction t
    WHERE t.receiver.id = :userId
""")
    double sumReceived(@org.springframework.data.repository.query.Param("userId") Integer userId);

    @org.springframework.data.jpa.repository.Query("""
    SELECT COALESCE(SUM(t.amount), 0)
    FROM Transaction t
    WHERE t.sender.id = :userId
""")
    double sumSent(@org.springframework.data.repository.query.Param("userId") Integer userId);


}

