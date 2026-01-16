package com.github.nastassjarosa.paymybuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConnectionPageController {

    @GetMapping("/connections")
    public String connectionsPage() {
        return "connections"; // templates/connections.html
    }
}

