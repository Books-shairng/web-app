package com.ninjabooks.json.book;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.ninjabooks.domain.Book;
import com.ninjabooks.dto.BookDto;
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
public class BookInfResponse
{
    private final Book book;

    @JsonUnwrapped
    private BookDto bookDto;
    private boolean isBorrowed;
    private int numberOfPeopleInQueue;
    private String description;

    public BookInfResponse(Book book) {
        this.book = book;
    }

    public String getDescription() {
        return "Amazon API not yet implemented.";
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public BookDto getBookDto() {
        return new ModelMapper().map(book, BookDto.class);
    }

    public void setBookDto(BookDto bookDto) {
        this.bookDto = bookDto;
    }

    public boolean isBorrowed() {
        return !book.getBorrows().isEmpty();
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    public int getNumberOfPeopleInQueue() {
        return book.getQueues().size();
    }

    public void setNumberOfPeopleInQueue(int numberOfPeopleInQueue) {
        this.numberOfPeopleInQueue = numberOfPeopleInQueue;
    }
}
