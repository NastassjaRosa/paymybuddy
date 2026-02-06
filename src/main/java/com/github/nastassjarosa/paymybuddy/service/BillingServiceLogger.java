package com.github.nastassjarosa.paymybuddy.service;

import com.github.nastassjarosa.paymybuddy.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BillingServiceLogger implements BillingService {
    private static final Logger log = LoggerFactory.getLogger(BillingServiceLogger.class);

    @Override
    public void logPayment(Transaction tx) {
        log.info("BILLING_ORDER txId={} from={} to={} amount={}",
                tx.getId(),
                tx.getSender().getEmail(),
                tx.getReceiver().getEmail(),
                tx.getAmount());
    }
}
