package com.ppdev.securityapp.service.impl;

import com.ppdev.securityapp.entity.*;
import com.ppdev.securityapp.exception.ResourceNotFoundException;
import com.ppdev.securityapp.service.EmailSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class RegistrationService {
    private final UserServiceImpl userServiceImpl;
    private final ConfirmationTokenService confirmationTokenService;
    private final ResetPasswordServiceImpl resetPasswordService;
    private final EmailSender emailSender;
    private static final String ADMIN_EMAIL = "4ever2pak@mail.ru";

    public String resetPassword(String token, String newPassword) throws ResourceNotFoundException {
        ResetPasswordToken resetPasswordToken = resetPasswordService.getToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid token."));
        if (resetPasswordToken.getConfirmedAt() == null) {
            return "Token not confirmed";
        }
        userServiceImpl.updateUserPassword(newPassword, resetPasswordToken.getUser().getID());
        return "Your password successfully updated.";
    }

    public String forgotPassword(String email) {
        String token = userServiceImpl.createResetPasswordToken(email);
        String link = "http://localhost:8080/auth/confirm-reset-password?token=" + token;
        emailSender.send(email, buildConfirmationResetPasswordEmail(link));
        return token;
    }

    public String register(RegistrationRequest request) {

        String acceptLink = "http://localhost:8080/auth/accept?request=" + request;
        String declineLink = "http://localhost:8080/auth/decline?email=" + request.getEmail();
        emailSender.send(ADMIN_EMAIL, buildConfirmationUserEmail(request, acceptLink, declineLink));

        return "Account is verifying";
    }

    @Transactional
    public String acceptUser(RegistrationRequest request) {
        String token = userServiceImpl.signUpUser(
                new User(request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        Role.USER));
        String link = "http://localhost:8080/auth/confirm?token=" + token;
        emailSender.send(
                request.getEmail(),
                buildConfirmationEmail(request.getFirstName(), link));
        return token;
    }

    public void declineUser(String email) {
        emailSender.send(email, "User info is incorrect. Repeat registration, pls");
    }

    @Transactional
    public String confirmResetToken(String token) {
        ResetPasswordToken resetPasswordToken = resetPasswordService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("Reset token not found"));

        if (resetPasswordToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Reset token already confirmed");
        }

        LocalDateTime expiredAt = resetPasswordToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Reset token expired");
        }
        resetPasswordService.setConfirmedAt(token);
        return "Reset token confirmed";
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        userServiceImpl.enableUser(
                confirmationToken.getUser().getEmail());
        return "confirmed";
    }

    private String buildConfirmationEmail(String name, String link) {
        return "Confirm your email" +
                "<div>Hi " + name + ". Thank you for registering. Please click on the below link to activate your account:" +
                "<blockquote> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 24 hours. <p>See you soon</p>";
    }

    private String buildConfirmationUserEmail(RegistrationRequest request, String acceptLink, String declineLink) {
        return "<div> New user: " + request + "</div>" +
                "<div>To accept new User click on: " + "<a href=\"" + acceptLink + "\">Accept user</a><br>" +
                "To decline new User click on: " + "<a href=\"" + declineLink + "\"> Decline user</a></div>\"";
    }

    private String buildConfirmationResetPasswordEmail(String link) {
        return "Hi, you need to tap link to get possibility to reset your password: " + "<a href=\"" + link + "\">Activate Now</a>";
    }
}
