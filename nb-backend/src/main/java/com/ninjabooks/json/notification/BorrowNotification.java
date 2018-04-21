package com.ninjabooks.json.notification;

import com.ninjabooks.domain.Borrow;
import com.ninjabooks.dto.BorrowDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.modelmapper.ModelMapper;

/**
 * This class extend {@link GenericNotification} and add information about:
 * - borrow date
 * - return date
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BorrowNotification extends GenericNotification
{
    private static final long serialVersionUID = -7981917194070092757L;

    @JsonUnwrapped
    @JsonIgnoreProperties(value = {"active", "id"})
    private final BorrowDto borrowDto;

    public BorrowNotification(Borrow borrow, ModelMapper modelMapper) {
        super(modelMapper, borrow.getBook());
        borrowDto = modelMapper.map(borrow, BorrowDto.class);
    }

    public BorrowDto getBorrowDto() {
        return borrowDto;
    }
}
