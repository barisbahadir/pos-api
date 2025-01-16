package com.bahadir.pos.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtils {

    // Sabit tarih formatları
    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm";
    public static final String DEFAULT_DATE_TIME_FORMAT = DEFAULT_DATE_FORMAT + " " + DEFAULT_TIME_FORMAT;

    // DateTimeFormatter nesneleri
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT);

    // LocalDateTime to String
    public static String formatLocalDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATE_TIME_FORMATTER) : null;
    }

    // Date to String (Eski java.util.Date kullanıyorsanız)
    public static String formatDate(Date date) {
        return date != null ? DATE_TIME_FORMATTER.format(date.toInstant()) : null;
    }

    // String to LocalDateTime
    public static LocalDateTime parseToLocalDateTime(String dateTimeString) {
        return dateTimeString != null ? LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER) : null;
    }
}
