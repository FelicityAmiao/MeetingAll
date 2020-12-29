package com.group8.meetingall.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.group8.meetingall.utils.Constant.YYYY_MMM_DD_HH_MM_E;
import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

public class DateTimeUtil {

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
        assert startTime != null;
        assert endTime != null;
        return startTime.isBefore(endTime);
    }

    public static String getOffsetTime(Integer offsetMinute) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        long time = offsetMinute*60*1000;
        Date afterDate = new Date(now .getTime() + time);
        return sdf.format(afterDate );
    }
}
