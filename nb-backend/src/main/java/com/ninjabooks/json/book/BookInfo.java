package com.ninjabooks.json.book;

import com.ninjabooks.domain.Book;
import com.ninjabooks.dto.BookDto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.modelmapper.ModelMapper;

/**
 * This class responsible for return all information about book
 * - id
 * - title
 * - author
 * - is borrowed
 * - is in any queue
 * - descprition
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BookInfo implements Serializable
{
    private static final long serialVersionUID = -8519537591630420779L;

    private final Book book;

    @JsonCreator
    public BookInfo(@JsonProperty(value = "book") Book book) {
        this.book = book;
    }

    @JsonUnwrapped
    @JsonIgnoreProperties(value = {"active"})
    public BookDto getBookDto() {
        return new ModelMapper().map(book, BookDto.class);
    }

    public int getQueueSize() {
        return book.getQueues().size();
    }

}
