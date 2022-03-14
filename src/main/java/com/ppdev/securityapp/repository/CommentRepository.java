package com.ppdev.securityapp.repository;

import com.ppdev.securityapp.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.userId.ID = :userId")
    @Transactional
    List<Comment> findCommentsByUserId(@Param("userId") Long id);
}
