package com.ppdev.securityapp.service;

import com.ppdev.securityapp.entity.GameObject;
import com.ppdev.securityapp.exception.ResourceNotFoundException;
import com.ppdev.securityapp.repository.GameObjectRepository;
import com.ppdev.securityapp.repository.UserRepository;
import com.ppdev.securityapp.service.impl.GameObjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

class GameObjectServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    GameObjectRepository gameObjectRepository;

    @InjectMocks
    GameObjectService gameObjectService;

    private static final GameObject gameObject1 = new GameObject();

    private static final GameObject gameObject2 = new GameObject();

    @Test
    @DisplayName("Test find game object")
    void findGameById() throws ResourceNotFoundException {

        gameObject1.setTitle("Axe of madness");
        gameObject1.setText("The best weapon for a warrior");
        gameObject1.setID(1L);
        gameObject1.setCreatedAt(LocalDate.now());

        doReturn(Optional.of(gameObject1)).when(gameObjectRepository).findById(1L);

        GameObject returnedGameObject = gameObjectService.getGameObject(1L);

        assertAll(
                () -> assertNotNull(returnedGameObject, "Game object was not found"),
                () -> assertSame(gameObject1, returnedGameObject, "The returned game object was not the same as the mock")
        );
    }

    @Test
    @DisplayName("Test Game object Not Found")
    void throwExceptionIfUserIsNullGetByEmail() {
        assertThrows(ResourceNotFoundException.class, () -> gameObjectService.getGameObject(1L));
    }

    @Test
    @DisplayName("Test find all games")
    void testGetUsers() {


        gameObject1.setTitle("Axe of madness");
        gameObject1.setText("The best weapon for a warrior");
        gameObject1.setID(1L);
        gameObject1.setCreatedAt(LocalDate.now());


        gameObject2.setTitle("Bow of death");
        gameObject2.setText("The best weapon for a archer");
        gameObject2.setID(2L);
        gameObject2.setCreatedAt(LocalDate.now());
        doReturn(Arrays.asList(gameObject1, gameObject2)).when(gameObjectRepository).findAll();

        List<GameObject> allGameObjects = gameObjectService.getAllGameObjects();

        assertEquals(2, allGameObjects.size(), "should return 2 game objects");
    }
}
