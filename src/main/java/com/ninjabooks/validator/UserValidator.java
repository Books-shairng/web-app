package com.ninjabooks.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class UserValidator implements Validator
{
    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
