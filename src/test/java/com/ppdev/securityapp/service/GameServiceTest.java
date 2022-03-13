package com.ppdev.securityapp.service;

import com.ppdev.securityapp.entity.Game;
import com.ppdev.securityapp.exception.ResourceNotFoundException;
import com.ppdev.securityapp.repository.GameRepository;
import com.ppdev.securityapp.service.impl.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

class GameServiceTest {

    @Mock
    GameRepository gameRepository;
    @InjectMocks
    GameService gameService;

    private static final Game game1 = new Game();

    Game game2 = new Game();

    @Test
    @DisplayName("Test find game")
    void findGameById() throws ResourceNotFoundException {

        game1.setName("Dota 2");
        game1.setId(1L);
        doReturn(Optional.of(game1)).when(gameRepository).findById(1L);

        Game returnedGame = gameService.findGame(1L);

        assertAll(() -> assertNotNull(returnedGame, "Game was not found"), () -> assertSame(game1, returnedGame, "The game returned was not the same as the mock"));
    }

    @Test
    @DisplayName("Test findGame Not Found")
    void throwExceptionIfUserIsNullGetByEmail() {
        assertThrows(ResourceNotFoundException.class, () -> gameService.findGame(1L));
    }

    @Test
    @DisplayName("Test find all games")
    void testGetUsers() {

        game1.setName("Dota 2");
        game1.setId(1L);

        game1.setName("CS 1.6");
        game1.setId(2L);
        doReturn(Arrays.asList(game1, game2)).when(gameRepository).findAll();

        List<Game> games = gameService.getGames();

        assertEquals(2, games.size(), "should return 2 games");
    }
}
