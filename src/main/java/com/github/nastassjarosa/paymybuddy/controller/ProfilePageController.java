package com.github.nastassjarosa.paymybuddy.controller;

import com.github.nastassjarosa.paymybuddy.model.User;
import com.github.nastassjarosa.paymybuddy.repo.UserRepository;
import com.github.nastassjarosa.paymybuddy.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfilePageController {

    private final UserRepository userRepository;
    private final UserService userService;

    public ProfilePageController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

        model.addAttribute("user", user);
        return "profile";
    }

//    @PostMapping("/profile")
//    public String updateProfile(
//            Authentication authentication,
//            @RequestParam String username,
//            @RequestParam String email,
//            @RequestParam(required = false) String password
//    ) {
//        String currentEmail = authentication.getName();
//
//        boolean mustLogout = userService.updateProfile(
//                currentEmail,
//                username,
//                email,
//                password
//        );
//
//        if (mustLogout || (password != null && !password.isBlank())) {
//            return "redirect:/logout";
//        }
//
//        return "redirect:/profile";
//    }

    @PostMapping("/profile")
    public String updateProfile(Authentication auth,
                                @RequestParam String username,
                                @RequestParam String email) {

        boolean emailChanged = userService.updateProfile(auth.getName(), username, email);

        if (emailChanged) {
            return "redirect:/logout";
        }

        return "redirect:/profile?success";
    }



}
