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
}
