package com.ninjabooks.json.comment;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class CommentRequest implements Serializable
{
    private static final long serialVersionUID = 85904392214674941L;

    @NotEmpty(message = "{default.NotEmpty.message}")
    @Length(min = 1, max = 250)
    private final String comment;

    @JsonCreator
    public CommentRequest(@JsonProperty(value = "comment") String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

}
