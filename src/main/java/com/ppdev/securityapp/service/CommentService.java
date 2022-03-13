package com.ppdev.securityapp.service;

import com.ppdev.securityapp.entity.Comment;
import com.ppdev.securityapp.exception.ResourceNotFoundException;

import java.util.List;

public interface CommentService {
    Comment addComment(Long userId,Comment comment) throws ResourceNotFoundException;

    List<Comment> getUserComments(Long id) throws ResourceNotFoundException;

    Comment getComment(Long userId, Long commentId) throws ResourceNotFoundException;

    void deleteComment(Long userId, Long commentId) throws ResourceNotFoundException;

    Comment updateComment(Comment comment, Long userId, Long id) throws ResourceNotFoundException;

}
