package com.group8.meetingall.utils;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

public class DateTimeUtil {
    public static String YYYY_MMM_DD_HH_MM_E = "yyyy-MM-dd HH:mm:ss";

    public static String getCurrentDateTime() {
        OffsetDateTime utc = OffsetDateTime.now();
        return utc.format(DateTimeFormatter.ofPattern(YYYY_MMM_DD_HH_MM_E));
    }

    public static LocalDateTime toLocalDateTimeWithDefaultFormat(String timeStr) {
        if (isNull(timeStr) || isBlank(timeStr)) {
            return null;
        }
        try {
            return LocalDateTime.parse(timeStr, DateTimeFormatter.ofPattern(YYYY_MMM_DD_HH_MM_E));
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isBefore(String firstStr, String secondStr) {
        LocalDateTime startTime = toLocalDateTimeWithDefaultFormat(firstStr);
        LocalDateTime endTime = toLocalDateTimeWithDefaultFormat(secondStr);
        return startTime.isBefore(endTime);
    }
}