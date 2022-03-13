package com.ppdev.securityapp.controller;

import com.ppdev.securityapp.exception.ResourceNotFoundException;
import com.ppdev.securityapp.service.impl.RegistrationService;
import com.ppdev.securityapp.entity.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping
    public String register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

    @GetMapping("/confirm-reset-password")
    public String confirmResetPassword(@RequestParam("token") String token) {
        return registrationService.confirmResetToken(token);
    }

    @PostMapping("/forgot-password")
    @PreAuthorize("#email == authentication.principal.username")
    public ResponseEntity<String> sendResetToken(@RequestParam("email") String email) {
        registrationService.forgotPassword(email);
        return new ResponseEntity<>("Check your email to confirm it", HttpStatus.OK);

    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptUser(@RequestBody RegistrationRequest request) {
        registrationService.acceptUser(request);
        return new ResponseEntity<>("User successfully registered", HttpStatus.OK);
    }

    @PostMapping("/decline")
    public ResponseEntity<String> declineUser(@RequestParam String email) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!(currentUserEmail.equals("4ever2pak@mail.ru"))) {
            return new ResponseEntity<>("Only ADMIN can do it", HttpStatus.FORBIDDEN);
        }
        registrationService.declineUser(email);
        return new ResponseEntity<>("Registration declined. Check your email", HttpStatus.OK);
    }

    @PutMapping("/reset")
    public String resetPassword(@RequestParam String token,
                                @RequestParam String password) throws ResourceNotFoundException {
        return registrationService.resetPassword(token, password);
    }
}
