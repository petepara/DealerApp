package com.ppdev.securityapp.service.impl;

import com.ppdev.securityapp.entity.ResetPasswordToken;
import com.ppdev.securityapp.entity.User;
import com.ppdev.securityapp.exception.ResourceNotFoundException;
import com.ppdev.securityapp.repository.UserRepository;
import com.ppdev.securityapp.entity.ConfirmationToken;
import com.ppdev.securityapp.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String USER_WITH_EMAIL_IS_NOT_FOUND =
            "user with email %s not found";
    private static final String USER_WITH_ID_IS_NOT_FOUND = "User not found with id : ";

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final ConfirmationTokenService confirmationTokenService;

    private final ResetPasswordServiceImpl resetPasswordService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format(USER_WITH_EMAIL_IS_NOT_FOUND, email));

        }
        return user;
    }

    public String createResetPasswordToken(String email) {

        User user = userRepository.getByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format(USER_WITH_EMAIL_IS_NOT_FOUND, email));
        }
        String token = UUID.randomUUID().toString();

        ResetPasswordToken resetPasswordToken = new ResetPasswordToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1440),
                user
        );
        resetPasswordService.saveResetPasswordToken(resetPasswordToken);
        return token;
    }

    @Override
    public String signUpUser(User user) {
        boolean userExists = userRepository.getByEmail(user.getEmail())!=null;

        if (userExists) {
            throw new IllegalStateException("email already taken");
        }
        String encodedPassword = bCryptPasswordEncoder
                .encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(24*60),
                user
        );
        confirmationTokenService.saveConfirmationToken(
                confirmationToken);
        return token;
    }

    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getByEmail(String email) throws ResourceNotFoundException {
        User user = userRepository.getByEmail(email);
        if(user==null){
            throw new ResourceNotFoundException(String.format(USER_WITH_EMAIL_IS_NOT_FOUND, email));
        }
        return user;
    }

    @Override
    public User findUserById(Long id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_WITH_ID_IS_NOT_FOUND + id));
    }

    @Override
    public void deleteUser(Long id) throws ResourceNotFoundException {
        User existingUser = this.userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_WITH_ID_IS_NOT_FOUND + id));
        userRepository.delete(existingUser);
    }

    @Override
    public User updateUser(User user, Long id) throws ResourceNotFoundException {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_WITH_ID_IS_NOT_FOUND + id));
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        return userRepository.save(existingUser);
    }
    @Override
    public User updateUserPassword(String password, Long id) throws ResourceNotFoundException {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_WITH_ID_IS_NOT_FOUND + id));
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        existingUser.setPassword(encodedPassword);
        return userRepository.save(existingUser);
    }

    public User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
