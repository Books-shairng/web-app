package com.ninjabooks.json.notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.ninjabooks.domain.Book;
import com.ninjabooks.dto.BookDto;
import org.modelmapper.ModelMapper;

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
public abstract class GenericNotification
{
    @JsonUnwrapped
    @JsonIgnoreProperties(value = "id")
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
