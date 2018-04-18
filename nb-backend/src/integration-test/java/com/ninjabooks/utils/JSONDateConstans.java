package com.ninjabooks.utils;

import com.ninjabooks.util.constants.DomainTestConstants;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public enum JSONDateConstans
{
    ORDER_DATE(DomainTestConstants.ORDER_DATE.format(ISO_LOCAL_DATE_TIME)),
    BORROW_DATE(DomainTestConstants.BORROW_DATE.format(ISO_LOCAL_DATE)),
    RETURN_DATE(DomainTestConstants.EXPECTED_RETURN_DATE.format(ISO_LOCAL_DATE)),
    COMMENT_DATE(DomainTestConstants.COMMENT_DATE.format(ISO_LOCAL_DATE_TIME));

    private final String value;

    JSONDateConstans(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
