package com.ppdev.securityapp.service;

import com.ppdev.securityapp.entity.Role;
import com.ppdev.securityapp.entity.User;
import com.ppdev.securityapp.exception.ResourceNotFoundException;
import com.ppdev.securityapp.repository.UserRepository;
import com.ppdev.securityapp.service.impl.ConfirmationTokenService;
import com.ppdev.securityapp.service.impl.ResetPasswordServiceImpl;
import com.ppdev.securityapp.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private  UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private  ConfirmationTokenService confirmationTokenService;
    @Mock
    private  ResetPasswordServiceImpl resetPasswordService;
    @InjectMocks
    UserService userService;


    private static final User user1 = new User(
            "Kira",
            "Arik",
            "zachem@gmail.com",
            "discodisco",
            Role.USER);
    private static final User user2 = new User(
            "Badum",
            "Babadum",
            "zatem@gmail.com",
            "discodiscodisco",
            Role.USER);


    @Test
    @DisplayName("Test getByEmail Success")
    void userReturnedGetByEmail() throws ResourceNotFoundException {

        doReturn(user1).when(userRepository).getByEmail("yakolya@mail.ru");

        User returnedUser = userService.getByEmail("yakolya@mail.ru");

        assertAll(
                () -> assertNotNull(returnedUser, "User was not found"),
                () -> assertSame(user1, returnedUser, "The returned user was not the same as the mock")
        );
    }

    @Test
    @DisplayName("Test getByEmail Not Found")
    void throwExceptionIfUserIsNullGetByEmail(){

        doReturn(null).when(userRepository).getByEmail("emailanet@mail.ru");

        assertThrows(ResourceNotFoundException.class, () -> userService.getByEmail("emailanet@mail.ru"));
    }

    @Test
    @DisplayName("Test findAll")
    void usersSizeIfUsersAdded() {

        doReturn(Arrays.asList(user1, user2)).when(userRepository).findAll();

        List<User> users = userService.getUsers();

        assertEquals(2, users.size(), "findAll should return 2 users");
    }

    @Test
    @DisplayName("Test find user by id")
    void findUserById() throws ResourceNotFoundException {

        user1.setID(1L);
        doReturn(Optional.of(user1)).when(userRepository).findById(1L);

        User returnedUser = userService.findUserById(1L);

        assertAll(
                () -> assertNotNull(returnedUser, "User was not found"),
                () -> assertSame(user1, returnedUser, "The user returned was not the same as the mock")
        );
    }

}
