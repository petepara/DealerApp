package com.ppdev.securityapp.mapper;

import com.ppdev.securityapp.dto.CommentDto;
import com.ppdev.securityapp.dto.CommentReadDto;
import com.ppdev.securityapp.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    public CommentReadDto toDto(Comment comment){
        return CommentReadDto.builder()
                .message(comment.getMessage())
                .userId(comment.getUserId().getID())
                .authorId(comment.getAuthorId())
                .build();
    }
    public Comment toEntity(CommentDto commentDto){
        return Comment.builder()
                .message(commentDto.getMessage())
                .build();
    }

}
