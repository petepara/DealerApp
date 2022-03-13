package com.ppdev.securityapp.service.impl;

import com.ppdev.securityapp.entity.GameObject;
import com.ppdev.securityapp.entity.User;
import com.ppdev.securityapp.exception.ResourceNotFoundException;
import com.ppdev.securityapp.repository.GameObjectRepository;
import com.ppdev.securityapp.repository.UserRepository;
import com.ppdev.securityapp.service.GameObjectService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class GameObjectServiceImpl implements GameObjectService {

    private final GameObjectRepository gameObjectRepository;

    private final UserRepository userRepository;

    private static final String GAME_OBJECT_NOT_FOUND_MESSAGE= "Game object not found with id : ";
    private static final String USER_NOT_FOUND_MESSAGE= "Game object not found with id : ";
    @Override
    public List<GameObject> getAllGameObjects() {
        return gameObjectRepository.findAll();
    }


    @Override
    public GameObject getGameObject(Long id) throws ResourceNotFoundException {
        return gameObjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(GAME_OBJECT_NOT_FOUND_MESSAGE + id));
    }

    @Override
    public List<GameObject> getCurrentUserGameObjects(Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE + id));
        return gameObjectRepository.findGameObjectsByAuthorId(user.getID());
    }

    @Override
    public void deleteGameObject(Long id) throws ResourceNotFoundException {
        GameObject existingGameObject = gameObjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(GAME_OBJECT_NOT_FOUND_MESSAGE + id));
        gameObjectRepository.delete(existingGameObject);
    }

    @Override
    public GameObject addGameObject(GameObject gameObject) {
        return gameObjectRepository.save(gameObject);
    }


    @Override
    public GameObject updateGameObjectInfo(GameObject gameObject, Long id) throws ResourceNotFoundException {
        GameObject existingGameObject = gameObjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(GAME_OBJECT_NOT_FOUND_MESSAGE + id));
        existingGameObject.setTitle(gameObject.getTitle());
        existingGameObject.setText(gameObject.getText());
        return gameObjectRepository.save(existingGameObject);
    }
}
