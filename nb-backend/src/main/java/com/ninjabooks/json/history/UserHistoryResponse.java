package com.ninjabooks.json.history;

import com.ninjabooks.domain.History;
import com.ninjabooks.dto.BookDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.modelmapper.ModelMapper;

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
    @JsonIgnoreProperties(value = {"active", "description", "status"})
    private BookDto bookDto;

    public UserHistoryResponse(History history, ModelMapper modelMapper) {
        super(history, modelMapper);
        this.bookDto = modelMapper.map(history.getBook(), BookDto.class);
    }

    public BookDto getBookDto() {
        return bookDto;
    }

    public void setBookDto(BookDto bookDto) {
        this.bookDto = bookDto;
    }
}
