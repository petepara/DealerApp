package com.ppdev.securityapp.service;

import com.ppdev.securityapp.dto.CommentDto;
import com.ppdev.securityapp.dto.CommentReadDto;
import com.ppdev.securityapp.entity.Comment;
import com.ppdev.securityapp.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Long addComment(Long userId, CommentDto commentDto) throws ResourceNotFoundException;

    List<CommentReadDto> getUserComments(Long id) throws ResourceNotFoundException;

    CommentReadDto findById(Long commentId) throws ResourceNotFoundException;

    void deleteComment(Long commentId) throws ResourceNotFoundException;

    Long updateComment(CommentDto commentDto,Long commentId) throws ResourceNotFoundException;

}
