package com.ninjabooks.utils;

import com.ninjabooks.util.constants.DomainTestConstants;

import java.time.format.DateTimeFormatter;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class JSONDateConstans
{
    public static final String ORDER_DATE =
        DomainTestConstants.ORDER_DATE.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    public static final String BORROW_DATE = DomainTestConstants.BORROW_DATE.format(DateTimeFormatter.ISO_LOCAL_DATE);
    public static final String RETURN_DATE =
        DomainTestConstants.EXPECTED_RETURN_DATE.format(DateTimeFormatter.ISO_LOCAL_DATE);
    public static final String COMMENT_DATE =
        DomainTestConstants.COMMENT_DATE.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
}
