package com.github.nastassjarosa.paymybuddy.service;

import com.github.nastassjarosa.paymybuddy.model.Transaction;

public interface BillingService {
    /**
     * Journalise une transaction persistée.
     *
     * @param transaction transaction enregistrée
     */
    void logPayment(Transaction transaction);
}
