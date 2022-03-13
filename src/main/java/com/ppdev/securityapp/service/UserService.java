package com.ppdev.securityapp.service;

import com.ppdev.securityapp.entity.User;
import com.ppdev.securityapp.exception.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getUsers();

    User getByEmail(String email) throws ResourceNotFoundException;

    User findUserById(Long id) throws ResourceNotFoundException;

    void deleteUser(Long id) throws ResourceNotFoundException;

    User updateUser(User user, Long id) throws ResourceNotFoundException;

    String signUpUser(User user);

    User updateUserPassword(String password, Long id) throws ResourceNotFoundException;

    User getCurrentUser();
}