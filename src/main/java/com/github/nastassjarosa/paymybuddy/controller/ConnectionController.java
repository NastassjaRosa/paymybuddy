package com.github.nastassjarosa.paymybuddy.controller;

import com.github.nastassjarosa.paymybuddy.model.User;
import com.github.nastassjarosa.paymybuddy.service.UserConnectionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


import java.util.List;

@RestController
@RequestMapping("/api/connections")
public class ConnectionController {

    private final UserConnectionService service;

    public ConnectionController(UserConnectionService service) {
        this.service = service;
    }

    public record AddConnectionRequest(String buddyEmail) {
    }

    @PostMapping
    public void add(@RequestBody AddConnectionRequest req, Authentication authentication) {
        String userEmail = authentication.getName();
        service.addConnection(userEmail, req.buddyEmail());
    }


    @GetMapping("/{email}")
    public List<User> list(@PathVariable String email) {
        return service.listConnections(email);
    }
}

