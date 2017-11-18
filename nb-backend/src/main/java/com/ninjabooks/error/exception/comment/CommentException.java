package com.ninjabooks.error.exception.comment;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 * */
public class CommentException extends Exception
{
    private static final long serialVersionUID = -3194428163775776445L;

    public CommentException(String message) {
        super(message);
    }

    public CommentException(String message, Throwable cause) {
        super(message, cause);
    }
}
