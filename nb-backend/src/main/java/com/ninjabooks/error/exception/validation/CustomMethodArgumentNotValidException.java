package com.ninjabooks.error.exception.validation;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class CustomMethodArgumentNotValidException extends Exception
{
    private static final long serialVersionUID = -3908554887268272153L;

    private final BindingResult bindingResult;

    public CustomMethodArgumentNotValidException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    @Override
    public String getMessage() {
        return bindingResult.getAllErrors().stream()
            .map(this::formatMessage)
            .collect(Collectors.joining("; "));
    }

    private String formatMessage(ObjectError objectError) {
        final FieldError fieldError = (FieldError) objectError;
        final String defaultMessage = fieldError.getDefaultMessage();
        return containsSpecialValue(defaultMessage) ?
            defaultMessage :
            fieldError.getField() + " " + defaultMessage;
    }

    private boolean containsSpecialValue(String value) {
        return Arrays.stream(SpecialType.values())
            .map(SpecialType::toString)
            .anyMatch(specialType -> specialType.equalsIgnoreCase(value));
    }

    private enum SpecialType {
        ISBN;

        @Override
        public String toString() {
            switch (this) {
                case ISBN:
                    return "invalid ISBN";
                default:
                    return null;
            }
        }
    }
}
