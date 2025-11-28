package com.github.nastassjarosa.paymybuddy.controller;

import com.github.nastassjarosa.paymybuddy.model.Transaction;
import com.github.nastassjarosa.paymybuddy.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    public record SendRequest(String senderEmail, String receiverEmail, double amount, String description) {}

    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody SendRequest req) {
        Transaction tx = service.sendMoney(req.senderEmail(), req.receiverEmail(), req.amount(), req.description());
        return ResponseEntity.ok("Transaction completed. ID=" + tx.getId());
    }

    @GetMapping("/history/{email}")
    public ResponseEntity<?> history(@PathVariable String email) {
        return ResponseEntity.ok(service.getUserHistory(email));
    }
}
