package com.ppdev.securityapp.service;

import com.ppdev.securityapp.entity.GameObject;
import com.ppdev.securityapp.exception.ResourceNotFoundException;

import java.util.List;

public interface GameObjectService {
    List<GameObject> getAllGameObjects();

    GameObject getGameObject(Long id) throws ResourceNotFoundException;

    List<GameObject> getCurrentUserGameObjects(Long id) throws ResourceNotFoundException;

    void deleteGameObject(Long id) throws ResourceNotFoundException;

    GameObject addGameObject(GameObject gameObject);

    GameObject updateGameObjectInfo(GameObject gameObject, Long id) throws ResourceNotFoundException;
}
