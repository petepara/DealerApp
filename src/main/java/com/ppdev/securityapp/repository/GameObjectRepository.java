package com.ppdev.securityapp.repository;

import com.ppdev.securityapp.entity.GameObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameObjectRepository extends JpaRepository<GameObject, Long> {
    List<GameObject> findGameObjectsByAuthorId(Long userId);
}
