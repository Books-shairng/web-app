package com.ninjabooks.controller;

import com.ninjabooks.error.exception.comment.CommentException;
import com.ninjabooks.json.comment.CommentRequest;
import com.ninjabooks.json.comment.CommentResponse;
import com.ninjabooks.json.message.MessageResponse;
import com.ninjabooks.service.rest.comment.CommentRestService;

import javax.validation.Valid;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@RestController
public class CommentController
{
    private static final String URL = "/api/comment/";
    private final CommentRestService service;

    @Autowired
    public CommentController(CommentRestService service) {
        this.service = service;
    }

    @RequestMapping(value = URL, method = GET)
    public ResponseEntity<?> fetchBookComments(@RequestParam(value = "isbn") String isbn) {
        Set<CommentResponse> comments = service.getComments(isbn);
        if (comments.isEmpty()) {
            String message = "Book does not contains any comments";
            return new ResponseEntity<>(new MessageResponse(message), OK);
        }

        return ResponseEntity.ok(comments);
    }

    @RequestMapping(value = URL + "{userID}/add", method = POST)
    public ResponseEntity<MessageResponse> addComment(@Valid @RequestBody CommentRequest commentRequest,
                                                      @PathVariable Long userID,
                                                      @RequestParam Long bookID) throws CommentException {
        service.addComment(commentRequest.getComment(), userID, bookID);
        final String message = format("Successfully add comment for book with id: {0}", bookID);

        return new ResponseEntity<>(new MessageResponse(message), OK);
    }
}
