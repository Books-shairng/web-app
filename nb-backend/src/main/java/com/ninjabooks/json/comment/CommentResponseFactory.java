package com.ninjabooks.json.comment;

import com.ninjabooks.domain.Comment;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class CommentResponseFactory
{
    public static CommentResponse makeCommentResponse(Comment comment) {
        return new CommentResponse(
            comment.getUser().getName(),
            comment.getDate(),
            comment.getContent(),
            comment.getBook().getIsbn()
        );
    }
}
