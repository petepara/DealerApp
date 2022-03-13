package com.ppdev.securityapp.controller;

import com.ppdev.securityapp.entity.User;
import com.ppdev.securityapp.exception.ResourceNotFoundException;
import com.ppdev.securityapp.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {

    private UserServiceImpl userService;

    @GetMapping("/users")
    public List<User> findAll() {
        return userService.getUsers();
    }

    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable Long id) throws ResourceNotFoundException {
        return userService.findUserById(id);
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("#user.email == authentication.principal.username")
    public User updateUser(@RequestBody User user,
                           @PathVariable Long id) throws ResourceNotFoundException {
        return userService.updateUser(user, id);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteUser(@PathVariable Long id,
                                             HttpServletRequest request) throws ResourceNotFoundException {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserById(id);
        if (user.getEmail().equals(currentUserEmail)) {
            request.getSession().invalidate();
            userService.deleteUser(id);
            return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
        }
    }
}