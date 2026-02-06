package com.github.nastassjarosa.paymybuddy.service;

import com.github.nastassjarosa.paymybuddy.model.Transaction;

public interface BillingService {
    void logPayment(Transaction transaction);
}
