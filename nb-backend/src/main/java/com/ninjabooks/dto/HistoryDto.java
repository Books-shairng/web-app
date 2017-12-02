package com.ninjabooks.dto;

import com.ninjabooks.util.converter.json.LocalDateDeserializer;
import com.ninjabooks.util.converter.json.LocalDateSerializer;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class HistoryDto extends BaseEntityDto
{
    private static final long serialVersionUID = -8941441076513470324L;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate returnDate;

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
