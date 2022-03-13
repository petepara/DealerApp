package com.ppdev.securityapp.mapper;

import com.ppdev.securityapp.dto.CommentDto;
import com.ppdev.securityapp.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDto toDto(Comment comment);
    Comment toEntity(CommentDto commentDto);

}
