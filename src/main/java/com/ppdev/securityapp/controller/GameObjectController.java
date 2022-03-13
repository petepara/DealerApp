package com.ppdev.securityapp.controller;

import com.ppdev.securityapp.entity.GameObject;
import com.ppdev.securityapp.entity.User;
import com.ppdev.securityapp.service.GameObjectService;
import com.ppdev.securityapp.service.UserService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class GameObjectController {

    private GameObjectService gameObjectService;
    private UserService userService;

    @PostMapping("/objects")
    @PreAuthorize("isAuthenticated()")
    @SneakyThrows
    public ResponseEntity<String> addGameObject(@RequestBody GameObject gameObject) {
        gameObjectService.addGameObject(gameObject);
        return new ResponseEntity<>("Game object successfully added ", HttpStatus.OK);
    }

    @GetMapping("/objects")
    public List<GameObject> getAllGameObjects() {
        return gameObjectService.getAllGameObjects();
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    @SneakyThrows
    public List<GameObject> getAllUserGameObjects() {
        User user = userService.getCurrentUser();
        return gameObjectService.getCurrentUserGameObjects(user.getID());
    }

    @PutMapping("/objects/{id}")
    @PreAuthorize("isAuthenticated()")
    @SneakyThrows
    public ResponseEntity<String> updateGameObject(@RequestBody GameObject gameObject,
                                                   @PathVariable Long id) {
        User user = userService.getCurrentUser();
        if (user.getID() == gameObject.getAuthorId()) {
            gameObjectService.updateGameObjectInfo(gameObject, id);
            return new ResponseEntity<>("Game object updated", HttpStatus.OK);
        }
        return new ResponseEntity<>("You can not modify this game object", HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/objects/{id}")
    @PreAuthorize("isAuthenticated()")
    @SneakyThrows
    public ResponseEntity<String> deleteGameObject(@PathVariable Long id) {
        User user = userService.getCurrentUser();
        if (user.getID() == gameObjectService.getGameObject(id).getAuthorId()) {
            gameObjectService.deleteGameObject(id);
            return new ResponseEntity<>("Game object was deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("You can not delete this game object", HttpStatus.FORBIDDEN);
    }

    @GetMapping("/objects/{id}")
    @SneakyThrows
    public GameObject findObjectById(@PathVariable Long id) {
        return gameObjectService.getGameObject(id);
    }
}
