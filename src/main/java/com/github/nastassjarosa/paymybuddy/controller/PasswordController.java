package com.github.nastassjarosa.paymybuddy.controller;

import com.github.nastassjarosa.paymybuddy.model.User;
import com.github.nastassjarosa.paymybuddy.repo.UserRepository;
import com.github.nastassjarosa.paymybuddy.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordController {

    private final UserRepository userRepository;
    private final UserService userService;

    public PasswordController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/profile/password")
    public String form() {
        return "change-password";
    }

    @PostMapping("/profile/password")
    public String changePassword(Authentication auth,
                                 @RequestParam String password,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        userService.changePassword(user.getId(), password);

        new SecurityContextLogoutHandler().logout(request, response, null);

        return "redirect:/login?passwordChanged";
    }
}
