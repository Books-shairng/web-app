package com.ninjabooks.json.notification;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.dto.BorrowDto;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class BorrowNotificationIT
{
    @Autowired
    private ModelMapper modelMapper;

    private BorrowNotification sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new BorrowNotification(DomainTestConstants.BORROW_FULL, modelMapper);
    }

    @Test
    public void testNotificationShouldReturnCorrectDateReturnDate() throws Exception {
        BorrowDto actual = sut.getBorrowDto();

        assertThat(actual).extracting("expectedReturnDate")
            .contains(DomainTestConstants.EXPECTED_RETURN_DATE);
    }

    @Test
    public void testNotificationShouldReturnCorrectBorrowDate() throws Exception {
        BorrowDto actual = sut.getBorrowDto();

        assertThat(actual).extracting("borrowDate").contains(DomainTestConstants.BORROW_DATE);
    }

    @Test
    public void testNotificationShouldReturnExpectedBorrowStatus() throws Exception {
        BorrowDto actual = sut.getBorrowDto();

        assertThat(actual).extracting("canExtendBorrow").contains(DomainTestConstants.CAN_EXTEND);
    }
}
