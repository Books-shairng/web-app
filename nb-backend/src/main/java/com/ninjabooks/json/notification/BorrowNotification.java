package com.ninjabooks.json.notification;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.dto.BorrowDto;
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
    private BorrowDto borrowDto;

    public BorrowNotification(Borrow borrow, ModelMapper modelMapper) {
        super(modelMapper, borrow.getBook());
        borrowDto = modelMapper.map(borrow, BorrowDto.class);
    }

    public BorrowDto getBorrowDto() {
        return borrowDto;
    }

    public void setBorrowDto(BorrowDto borrowDto) {
        this.borrowDto = borrowDto;
    }
}
