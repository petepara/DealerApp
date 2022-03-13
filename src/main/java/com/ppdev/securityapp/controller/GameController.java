package com.ppdev.securityapp.controller;

import com.ppdev.securityapp.entity.Game;
import com.ppdev.securityapp.exception.ResourceNotFoundException;
import com.ppdev.securityapp.service.GameService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("games")
@AllArgsConstructor
public class GameController {

    private GameService gameService;

    @GetMapping
    public List<Game> getAllGames() {
        return gameService.getGames();
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public Game addGame(@RequestBody Game game) {
        return gameService.addGame(game);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Game updateGame(@RequestBody Game game, @PathVariable Long id) throws ResourceNotFoundException {
        return gameService.updateGameName(game, id);
    }
}
