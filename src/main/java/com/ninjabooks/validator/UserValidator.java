package com.ninjabooks.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Component
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
