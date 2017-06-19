package com.ninjabooks.json.book;

import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.domain.Queue;
import com.ninjabooks.dto.BookDto;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BookInfResponseTest
{
    private static final String AUTHOR ="J. Bloch";
    private static final String TITLE = "Effective Java";
    private static final String ISBN = "978-0321356680";

    private final Book book = new Book(TITLE, AUTHOR, ISBN);

    private BookInfResponse bookInfResponse;

    @Before
    public void setUp() throws Exception {
        this.bookInfResponse = new BookInfResponse(book);
    }

    @Test
    public void tesIsBorrowedShouldReturnTrue() throws Exception {
        book.setBorrows(Collections.singletonList(new Borrow()));

        boolean actual = bookInfResponse.isBorrowed();

        assertThat(actual).isTrue();
    }

    @Test
    public void testIsBorrowedShouldReturnFalse() throws Exception {
        boolean actual = bookInfResponse.isBorrowed();

        assertThat(actual).isFalse();
    }

    @Test
    public void testNumberOfPeopleShouldBeZero() throws Exception {
        int actual = bookInfResponse.getNumberOfPeopleInQueue();

        assertThat(actual).isZero();
    }

    @Test
    public void testNumberOfPeopleInQueueShouldBePositive() throws Exception {
        book.setQueues(Collections.singletonList(new Queue()));

        int actual = bookInfResponse.getNumberOfPeopleInQueue();

        assertThat(actual).isNotZero();
    }

    @Test
    public void testTransferBookToDtoShouldHaveSameFields() throws Exception {
        BookDto actual = bookInfResponse.getBookDto();

        assertSoftly(softly -> {
            assertThat(actual.getAuthor()).isEqualTo(AUTHOR);
            assertThat(actual.getTitle()).isEqualTo(TITLE);
            assertThat(actual.getIsbn()).isEqualTo(ISBN);
        });
    }
}
