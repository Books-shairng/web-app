package com.ninjabooks.json.book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.ninjabooks.domain.Book;
import com.ninjabooks.dto.BookDto;
import org.modelmapper.ModelMapper;

import java.io.Serializable;

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

    private Book book;

    public BookInfo(Book book) {
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
