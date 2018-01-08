package com.ninjabooks.validator.isbn;

import java.util.Arrays;

import org.assertj.core.api.Assertions;
import org.hibernate.validator.internal.util.annotation.ConstraintAnnotationDescriptor;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class ISBNValidatorTest
{
    private static final String[] VALID_ISBN = {
        null,
        "978-03213-566-8-0",
        "978-123-456-789-7",
        "978-91-983989-1-5",
        "978-988-785-411-1",
        "978-1-56619-909-4",
        "978-1-4028-9462-6",
        "978-0-85131-041-1",
        "978-0-684-84328-5",
        "978-1-84356-028-9",
        "978-0-54560-495-6"};

    private static final String[] INVALID_ISBN = {
        "978-123-456-789-6",
        "978-91-983989-1-4",
        "978-988-785-411-2",
        "978-1-56619-909-1",
        "978-1-4028-9462-0",
        "978-0-85131-041-5",
        "978-0-684-84328-1",
        "978-1-84356-028-1",
        "978-0-54560-495-4",
        "",
        "978-0-54560-4",
        "978-0-55555555555555"
    };

    private ISBNValidator sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new ISBNValidator();
        this.sut.initialize(initAnnotation());
    }

    @Test
    public void testValidateWithCorrectISBNShouldReturnTrue() throws Exception {
        assertSoftly(softly -> Arrays.stream(VALID_ISBN)
            .forEach(s -> {
                boolean actual = validate(s);
                Assertions.assertThat(actual).isTrue();
            }));
    }

    @Test
    public void testValidateWithIncorrectISBNShouldReturnFalse() throws Exception {
        assertSoftly(softly -> Arrays.stream(INVALID_ISBN)
            .forEach(s -> {
                boolean actual = validate(s);
                Assertions.assertThat(actual).isFalse();
            }));
    }

    private ISBN initAnnotation() {
        return new ConstraintAnnotationDescriptor.Builder<>(ISBN.class).build().getAnnotation();
    }

    private boolean validate(String isbn) {
        return sut.isValid(isbn, null);
    }
}
