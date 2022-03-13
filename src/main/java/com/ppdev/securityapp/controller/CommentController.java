package com.ppdev.securityapp.controller;

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

@RestController
@RequestMapping("/users/{userId}/comments")
@AllArgsConstructor
public class CommentController {

    private CommentService commentService;
    private UserService userService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @SneakyThrows
    public Comment addComment(@PathVariable Long userId,
                              @RequestBody Comment comment) {
        return commentService.addComment(userId, comment);
    }

    @GetMapping
    @SneakyThrows
    public List<Comment> getAllUserComments(@PathVariable Long userId) {
        return commentService.getUserComments(userId);
    }

    @GetMapping("/{commentId}")
    @SneakyThrows
    public Comment getUserComment(@PathVariable Long userId,
                                  @PathVariable Long commentId) {
        return commentService.getComment(userId, commentId);
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("isAuthenticated()")
    @SneakyThrows
    public ResponseEntity<String> deleteComment(@PathVariable Long userId,
                                                @PathVariable Long commentId) {
        User user = userService.getCurrentUser();
        if (user.getID() == commentService.getComment(userId, commentId).getAuthorId()) {
            commentService.deleteComment(userId, commentId);
            return new ResponseEntity<>("Comment successfully deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("IT IS NOT YOURS", HttpStatus.FORBIDDEN);

    }

    @PutMapping("{commentId}")
    @PreAuthorize("isAuthenticated()")
    @SneakyThrows
    public ResponseEntity<String> updateComment(@RequestBody Comment comment,
                                                @PathVariable Long userId,
                                                @PathVariable Long commentId) {
        User user = userService.getCurrentUser();
        if (user.getID() == commentService.getComment(userId, commentId).getAuthorId()) {
            commentService.updateComment(comment, userId, commentId);
            return new ResponseEntity<>("Comment successfully updated", HttpStatus.OK);
        }
        return new ResponseEntity<>("It is not your comment! How dare you?!?!?!?!", HttpStatus.OK);

    }

}
