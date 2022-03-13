package com.ppdev.securityapp.service.impl;

import com.ppdev.securityapp.entity.Game;
import com.ppdev.securityapp.exception.ResourceNotFoundException;
import com.ppdev.securityapp.repository.GameRepository;
import com.ppdev.securityapp.service.GameService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GameServiceImpl implements GameService {

    private static final String GAME_NOT_FOUND_MESSAGE = "Game not found with id: ";

    private final GameRepository gameRepository;

    @Override
    public List<Game> getGames() {
        return gameRepository.findAll();
    }

    @Override
    public Game addGame(Game game) {
        return gameRepository.save(game);
    }

    @Override
    public Game updateGameName(Game game, Long id) throws ResourceNotFoundException {
        Game existingGame = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(GAME_NOT_FOUND_MESSAGE + id));
        existingGame.setName(game.getName());
        return gameRepository.save(existingGame);
    }

    @Override
    public Game findGame(Long id) throws ResourceNotFoundException {
        return gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(GAME_NOT_FOUND_MESSAGE + id));
    }
}
