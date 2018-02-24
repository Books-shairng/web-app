package com.ninjabooks.json.notification;

import com.ninjabooks.dto.BorrowDto;

import static com.ninjabooks.util.constants.DomainTestConstants.BORROW_DATE;
import static com.ninjabooks.util.constants.DomainTestConstants.BORROW_FULL;
import static com.ninjabooks.util.constants.DomainTestConstants.CAN_EXTEND;
import static com.ninjabooks.util.constants.DomainTestConstants.EXPECTED_RETURN_DATE;

import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BorrowNotificationTest
{
    private BorrowNotification sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new BorrowNotification(BORROW_FULL, new ModelMapper());
    }

    @Test
    public void testNotificationShouldReturnCorrectReturnDate() throws Exception {
        BorrowDto actual = sut.getBorrowDto();

        assertThat(actual).extracting("expectedReturnDate")
            .contains(EXPECTED_RETURN_DATE);
    }

    @Test
    public void testNotificationShouldReturnCorrectBorrowDate() throws Exception {
        BorrowDto actual = sut.getBorrowDto();

        assertThat(actual).extracting("borrowDate").contains(BORROW_DATE);
    }

    @Test
    public void testNotificationShouldReturnExpectedBorrowStatus() throws Exception {
        BorrowDto actual = sut.getBorrowDto();

        assertThat(actual).extracting("canExtendBorrow").contains(CAN_EXTEND);
    }

}
