package com.ppdev.securityapp.service.impl;

import com.ppdev.securityapp.dto.CommentDto;
import com.ppdev.securityapp.dto.CommentReadDto;
import com.ppdev.securityapp.entity.Comment;
import com.ppdev.securityapp.entity.User;
import com.ppdev.securityapp.exception.ResourceNotFoundException;
import com.ppdev.securityapp.mapper.CommentMapper;
import com.ppdev.securityapp.repository.CommentRepository;
import com.ppdev.securityapp.repository.UserRepository;
import com.ppdev.securityapp.service.CommentService;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private static final String USER_NOT_FOUND_MESSAGE = "User not found with id : ";
    private static final String COMMENT_NOT_FOUND_MESSAGE = "Comment not found with id : ";


    @Override
    public Long addComment(Long userId, CommentDto commentDto) throws ResourceNotFoundException {
        Comment comment = commentMapper.toEntity(commentDto);
        comment.setUserId(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE + userId)));
        return commentRepository.save(comment).getId();
    }

    @Override
    public List<CommentReadDto> getUserComments(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE + userId));
        return commentRepository.findCommentsByUserId(user.getID())
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentReadDto findById(Long commentId) throws ResourceNotFoundException {
        return commentMapper.toDto(commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(COMMENT_NOT_FOUND_MESSAGE + commentId)));
    }

    @Override
    public void deleteComment(Long commentId) {
        Optional<Comment> maybeComment = commentRepository.findById(commentId);
        maybeComment.ifPresent(commentRepository::delete);
    }

    @Override
    public Long updateComment(CommentDto commentDto, Long commentId) throws ResourceNotFoundException {

        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(COMMENT_NOT_FOUND_MESSAGE + commentId));
        existingComment.setMessage(commentDto.getMessage());

        return commentRepository.save(existingComment).getId();
    }
}
