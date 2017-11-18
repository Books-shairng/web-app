package com.ninjabooks.controller;

import com.ninjabooks.json.comment.CommentResponse;
import com.ninjabooks.json.message.MessageResponse;
import com.ninjabooks.service.rest.comment.CommentRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@RestController
public class CommentController
{
    private final CommentRestService commentRestService;

    @Autowired
    public CommentController(CommentRestService commentRestService) {
        this.commentRestService = commentRestService;
    }

    @RequestMapping(value = "/api/comment/", method = RequestMethod.GET)
    public ResponseEntity<?> fetchBookComments(@RequestParam(value = "isbn") String isbn) {
        Set<CommentResponse> comments = commentRestService.getComments(isbn);
        if (comments.isEmpty()) {
            String message = "Book does not contains any comments";
            return new ResponseEntity<>(new MessageResponse(message), HttpStatus.OK);
        }

        return ResponseEntity.ok(comments);
    }
}
