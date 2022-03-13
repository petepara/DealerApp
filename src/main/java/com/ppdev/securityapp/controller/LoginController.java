package com.ppdev.securityapp.controller;

import org.springframework.web.bind.annotation.*;


@RestController
public class LoginController {

    @GetMapping("/")
    public String mainPage() {
        return "'/login' - login page <br>" +
                "'/logout' - page for logout <br>" +
                "'/auth?request' - send info for authorization <br>" +
                "'/auth/forgot-password?email' - reset password <br>";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password) {
        return "redirect:/";
    }

}
