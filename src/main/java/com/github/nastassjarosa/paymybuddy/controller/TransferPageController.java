package com.github.nastassjarosa.paymybuddy.controller;


import com.github.nastassjarosa.paymybuddy.service.TransactionService;
import com.github.nastassjarosa.paymybuddy.service.UserConnectionService;
import com.github.nastassjarosa.paymybuddy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Controller
public class TransferPageController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final UserConnectionService userConnectionService;

    public TransferPageController(TransactionService transactionService, UserService userService, UserConnectionService userConnectionService) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.userConnectionService = userConnectionService;
    }

    @GetMapping("/transfer")
    public String transferPage(Model model, Principal principal) {
        String senderEmail = principal.getName();

        model.addAttribute("relations", userConnectionService.listConnections(senderEmail));
        model.addAttribute("history", transactionService.getUserHistory(senderEmail));
        return "transfer";
    }

    @PostMapping("/transfer")
    public String doTransfer(@RequestParam String receiverEmail,
                             @RequestParam double amount,
                             @RequestParam(required = false) String description,
                             Principal principal) {

        String senderEmail = principal.getName();
        transactionService.sendMoney(senderEmail, receiverEmail, amount, description);
        return "redirect:/transfer";
    }
}
