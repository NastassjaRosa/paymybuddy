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
    public String transferPage(Model model,
                               Principal principal,
                               @RequestParam(required = false) String error,
                               @RequestParam(required = false) String success) {

        String currentEmail = principal.getName();

        model.addAttribute("currentEmail", currentEmail);
        model.addAttribute("relations", userConnectionService.listConnections(currentEmail));
        model.addAttribute("history", transactionService.getUserHistory(currentEmail));

        if (error != null) {
            model.addAttribute("error", error);
        }
        if (success != null) {
            model.addAttribute("success", true);
        }

        return "transfer";
    }


    @PostMapping("/transfer")
    public String doTransfer(@RequestParam String receiverEmail,
                             @RequestParam double amount,
                             @RequestParam(required = false) String description,
                             Principal principal) {

        String senderEmail = principal.getName();

        try {
            transactionService.sendMoney(senderEmail, receiverEmail, amount, description);
            return "redirect:/transfer?success";
        } catch (IllegalArgumentException | IllegalStateException e) {
            return "redirect:/transfer?error=" + java.net.URLEncoder.encode(e.getMessage(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }

}
