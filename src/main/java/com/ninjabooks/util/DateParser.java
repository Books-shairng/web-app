package com.ninjabooks.util;

import com.ninjabooks.security.TokenUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public final class DateParser
{
    private DateParser() {
    }

    /**
     * Convert date from map to local date time object.
     * {@link TokenUtils#getCreatedDateFromToken(String)}
     *
     * @param map - this map contains converted local date  object
     * @return desired local date
     */

    public static LocalDateTime parseMapToLocalDateTime(Map map) {
        List<Integer> list = new ArrayList<>(map.values());
        int year = list.get(1);
        int month = list.get(5);
        int day = list.get(2);

        int hour = list.get(3);
        int minute = list.get(4);
        int second = list.get(7);
        int nano = list.get(6);

        return LocalDateTime.of(year, month, day, hour, minute, second, nano);
    }
}
