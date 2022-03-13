package com.ppdev.securityapp.service.impl;

import com.ppdev.securityapp.entity.Comment;
import com.ppdev.securityapp.entity.User;
import com.ppdev.securityapp.exception.ResourceNotFoundException;
import com.ppdev.securityapp.repository.CommentRepository;
import com.ppdev.securityapp.repository.UserRepository;
import com.ppdev.securityapp.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.Access;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private static final String USER_NOT_FOUND_MESSAGE= "User not found with id : ";
    private static final String COMMENT_NOT_FOUND_MESSAGE= "Comment not found with id : ";


    @Override
    public Comment addComment(Long userId, Comment comment) throws ResourceNotFoundException {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE + userId));
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getUserComments(Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE + id));
        return commentRepository.findCommentsByUserId(user.getID());
    }

    @Override
    public Comment getComment(Long userId, Long commentId) throws ResourceNotFoundException {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE + userId));
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(COMMENT_NOT_FOUND_MESSAGE + commentId));
    }

    @Override
    public void deleteComment(Long userId, Long commentId) throws ResourceNotFoundException {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE + userId));
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(COMMENT_NOT_FOUND_MESSAGE + commentId));
        commentRepository.delete(existingComment);
    }

    @Override
    public Comment updateComment(Comment comment, Long userId, Long id) throws ResourceNotFoundException {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE + userId));
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMMENT_NOT_FOUND_MESSAGE + id));
        existingComment.setMessage(comment.getMessage());

        return commentRepository.save(existingComment);
    }
}
