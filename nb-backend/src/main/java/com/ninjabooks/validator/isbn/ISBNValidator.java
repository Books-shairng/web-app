package com.ninjabooks.validator.isbn;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * Default ISBN-13 validator
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class ISBNValidator implements ConstraintValidator<ISBN, String>
{
    private static final Pattern NON_DIGIT_PATTERN = Pattern.compile("[^\\d]");
    private static final int DEFAULT_ISBN_LENGTH = 13;

    @Override
    public void initialize(ISBN constraintAnnotation) {
    }

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext context) {
        if (isbn == null) {
            return true;
        }

        String digits = NON_DIGIT_PATTERN.matcher(isbn).replaceAll("");

        return digits.length() == DEFAULT_ISBN_LENGTH && checkChecksumISBN(digits);
    }

    private boolean checkChecksumISBN(String isbn) {
        int sum = 0;
        for (int i = 0; i < isbn.length() - 1; i++) {
            sum += (isbn.charAt(i) - '0') * (i % 2 == 0 ? 1 : 3);
        }
        char checkSum = isbn.charAt(12);
        return 10 - sum % 10 == (checkSum - '0' == 0 ? 10 : checkSum - '0');
    }
}
