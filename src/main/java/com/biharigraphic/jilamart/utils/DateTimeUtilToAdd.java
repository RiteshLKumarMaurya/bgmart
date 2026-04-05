package com.biharigraphic.jilamart.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtilToAdd {

    public static ZonedDateTime getDateTimeAfterMinutes(long minutesToAdd, ZoneId userZone) {
        Instant nowUtc = Instant.now();
        return nowUtc.atZone(userZone).plusMinutes(minutesToAdd);
    }

    public static String getFriendlyTimeAfterMinutes(long minutesToAdd, ZoneId userZone) {
        ZonedDateTime nowUser = Instant.now().atZone(userZone);
        ZonedDateTime futureTime = nowUser.plusMinutes(minutesToAdd);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

        if (futureTime.toLocalDate().isEqual(nowUser.toLocalDate())) {
            return "Today " + futureTime.format(timeFormatter);
        } else {
            return futureTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy, h:mm a"));
        }
    }
}
