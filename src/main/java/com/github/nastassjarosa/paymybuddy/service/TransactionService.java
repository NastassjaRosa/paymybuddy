package com.github.nastassjarosa.paymybuddy.service;

import com.github.nastassjarosa.paymybuddy.model.Transaction;
import com.github.nastassjarosa.paymybuddy.model.User;
import com.github.nastassjarosa.paymybuddy.repo.TransactionRepository;
import com.github.nastassjarosa.paymybuddy.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


    @Transactional
    public Transaction sendMoney(String senderEmail, String receiverEmail, double amount, String description) {

        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be > 0");

        if (senderEmail.equals(receiverEmail))
            throw new IllegalArgumentException("Cannot send money to yourself");

        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

        User receiver = userRepository.findByEmail(receiverEmail)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        if (!connectionService.areConnected(sender, receiver))
            throw new IllegalArgumentException("Users are not connected");

        Transaction tx = new Transaction(sender, receiver, amount, description);
        double balance = getBalance(sender.getId());
        if (balance < amount) {
            throw new IllegalStateException("Insufficient balance");
        }

        Transaction saved = transactionRepository.save(tx);
        billingService.logPayment(saved);
        return saved;

    }

    public List<Transaction> getUserHistory(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return transactionRepository.findBySenderOrReceiver(user, user);
    }

    public double getBalance(Integer userId) {
        return transactionRepository.sumReceived(userId) - transactionRepository.sumSent(userId);
    }


}
