package com.ppdev.securityapp.service;

import com.ppdev.securityapp.entity.Game;
import com.ppdev.securityapp.exception.ResourceNotFoundException;

import java.util.List;

public interface GameService {
    List<Game> getGames();

    Game addGame(Game game);

    Game updateGameName(Game game, Long id) throws ResourceNotFoundException;

    Game findGame(Long id) throws ResourceNotFoundException;
}
