package com.ninjabooks.json.history;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ninjabooks.dto.BookDto;
import com.ninjabooks.dto.HistoryDto;

/**
 * This response model return user history which contains
 * - date
 * - book
 * @see GenericHistoryResponse
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class UserHistoryResponse extends GenericHistoryResponse
{
    private static final long serialVersionUID = 8744468996309968293L;

    @JsonProperty(value = "book")
    @JsonIgnoreProperties(value = "isActive")
    private BookDto bookDto;

    public UserHistoryResponse(HistoryDto historyDto, BookDto bookDto) {
        super(historyDto);
        this.bookDto = bookDto;
    }

    public BookDto getBookDto() {
        return bookDto;
    }

    public void setBookDto(BookDto bookDto) {
        this.bookDto = bookDto;
    }
}
