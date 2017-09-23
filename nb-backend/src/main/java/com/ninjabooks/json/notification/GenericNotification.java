package com.ninjabooks.json.notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.ninjabooks.domain.Book;
import com.ninjabooks.dto.BookDto;
import org.modelmapper.ModelMapper;

import java.io.Serializable;

/**
 * Notification by default should return information about book. If notification
 * should return other information like order date, this class must be inherited
 * by more elaborate class.
 *
 * @see BorrowNotification
 * @see QueueNotification
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public abstract class GenericNotification implements Serializable
{
    private static final long serialVersionUID = -2145369478368501705L;

    @JsonUnwrapped
    @JsonIgnoreProperties(value = {"active", "description", "status"})
    private BookDto bookDto;

    public GenericNotification(ModelMapper modelMapper, Book book) {
        bookDto = modelMapper.map(book, BookDto.class);
    }

    public BookDto getBookDto() {
        return bookDto;
    }

    public void setBookDto(BookDto bookDto) {
        this.bookDto = bookDto;
    }
}
