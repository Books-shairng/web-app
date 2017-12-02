package com.ninjabooks.util;

import com.ninjabooks.security.utils.TokenUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public final class DateUtils
{
    private static final Logger logger = LogManager.getLogger(DateUtils.class);

    private DateUtils() {
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

        logger.info("Map was converted successfull to date");
        return LocalDateTime.of(year, month, day, hour, minute, second, nano);
    }

    /**
     * Convert string which contains date in the following format yyyy-MM-DD.
     * Accepted date separator: [-;,;.;], otherwise throws exception.
     * Month or day less than 10 can be entered as 01 or 1.
     *
     * @param str - string which contains date to convert
     * @return converted string to local date object
     */

    public static LocalDate parseStringToLocalDate(String str) {
        if (!str.matches("[\\d,.-]+") || str.length() > 10) {
            logger.error("String contains illegal arguments");
            throw new IllegalArgumentException("String contains illegal arguments");
        }

        String[] date = str.split("[,.-]");
        int year = Integer.parseInt(date[0]);
        int month = convertNumberLessThanTen(date[1]);
        int day = convertNumberLessThanTen(date[2]);

        return LocalDate.of(year, month, day);
    }

    /**
     * Convert string which contains date in the following format yyyy-MM-ddTHH:mm:ss,
     * where T is time seperator.
     * Accepted date separator: [-;,;.;], otherwise throws exception.
     * Accepted time separaotr [:], otherwise throws exception.
     * Month or day less than 10 can be entered as 01 or 1.
     * Time also can be entered in format 01 or 1 if less than 10.
     *
     * @param str - string which contains date and time to convert
     * @return converted string to local date object
     */

    public static LocalDateTime parseStringToLocalDateTime(String str) {
        if (!str.contains("T") || str.length() > 19) {
            throw new IllegalArgumentException("String does not contains time separator");
        }

        String[] dateWithTime = str.split("[T]");
        LocalDate date = parseStringToLocalDate(dateWithTime[0]);

        String[] time = dateWithTime[1].split("[:]");
        int hour = convertNumberLessThanTen(time[0]);
        int minute = convertNumberLessThanTen(time[1]);
        int second = convertNumberLessThanTen(time[2]);
        LocalTime times = LocalTime.of(hour, minute, second);

        return LocalDateTime.of(date, times);
    }

    private static int convertNumberLessThanTen(String str) {
        int number = 0;
        try {
            number = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            logger.error(e);
            String tmp = str.substring(0, 1);
            number = Integer.parseInt(tmp);
        }
        finally {
            return number;
        }
    }
}
