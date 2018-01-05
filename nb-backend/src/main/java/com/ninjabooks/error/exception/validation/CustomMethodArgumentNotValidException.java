package com.ninjabooks.error.exception.validation;

import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

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
            .map(objectError -> {
                final FieldError fieldError = (FieldError) objectError;
                return fieldError.getField() + " " + fieldError.getDefaultMessage();
            })
            .collect(Collectors.joining("; "));
    }
}
