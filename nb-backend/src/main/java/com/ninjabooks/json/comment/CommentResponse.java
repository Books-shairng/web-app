package com.ninjabooks.json.comment;

import com.ninjabooks.util.converter.json.LocalDateTimeDeserializer;
import com.ninjabooks.util.converter.json.LocalDateTimeSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class CommentResponse implements Serializable
{
    private static final long serialVersionUID = 4719413999741691927L;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private final LocalDateTime date;
    private final String author;
    private final String content;
    private final String isbn;

    @JsonCreator
    public CommentResponse(@JsonProperty(value = "author") String author,
                           @JsonProperty(value = "date") LocalDateTime date,
                           @JsonProperty(value = "content") String content,
                           @JsonProperty(value = "isbn") String isbn) {
        this.author = author;
        this.date = date;
        this.content = content;
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getIsbn() {
        return isbn;
    }

}
