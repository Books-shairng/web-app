package com.ninjabooks.json.comment;

import java.io.Serializable;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class CommentRequest implements Serializable
{
    private static final long serialVersionUID = 85904392214674941L;

    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
