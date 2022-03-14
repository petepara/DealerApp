package com.ppdev.securityapp.controller;

import com.ppdev.securityapp.dto.CommentDto;
import com.ppdev.securityapp.dto.CommentReadDto;
import com.ppdev.securityapp.entity.Comment;
import com.ppdev.securityapp.entity.User;
import com.ppdev.securityapp.service.CommentService;
import com.ppdev.securityapp.service.UserService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users/{userId}/comments")
@AllArgsConstructor
public class CommentController {

    private CommentService commentService;
    private UserService userService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @SneakyThrows
    public Long addComment(@PathVariable Long userId,
                           @RequestBody CommentDto commentDto) {
        return commentService.addComment(userId, commentDto);
    }

    @GetMapping
    @SneakyThrows
    public List<CommentReadDto> getAllUserComments(@PathVariable Long userId) {
        return commentService.getUserComments(userId);
    }

    @GetMapping("/{commentId}")
    @SneakyThrows
    public CommentReadDto getUserComment(@PathVariable Long userId,
                                         @PathVariable Long commentId) {
        return commentService.findById(commentId);
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("isAuthenticated()")
    @SneakyThrows
    public ResponseEntity<String> deleteComment(@PathVariable Long userId,
                                                @PathVariable Long commentId) {
        User user = userService.getCurrentUser();

        if (user.getID() == userService.findUserById(userId).getID()) {
            commentService.deleteComment(commentId);
            return new ResponseEntity<>("Comment successfully deleted", HttpStatus.OK);

        }
        return new ResponseEntity<>("IT IS NOT YOURS", HttpStatus.FORBIDDEN);

    }

    @PutMapping("{commentId}")
    @PreAuthorize("isAuthenticated()")
    @SneakyThrows
    public ResponseEntity<String> updateComment(@RequestBody CommentDto commentDto,
                                                @PathVariable Long userId,
                                                @PathVariable Long commentId) {
        User user = userService.getCurrentUser();
        if (user.getID() == commentService.findById(commentId).getAuthorId()) {
            commentService.updateComment(commentDto, commentId);
            return new ResponseEntity<>("Comment successfully updated", HttpStatus.OK);
        }
        return new ResponseEntity<>("It is not your comment! How dare you?!?!?!?!", HttpStatus.OK);

    }

}
