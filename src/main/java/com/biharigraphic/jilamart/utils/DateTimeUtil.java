package com.biharigraphic.jilamart.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public static String getFriendlyTimeAfterMinutes(long minutesToAdd, ZoneId userZone) {
        if (minutesToAdd < 0) {
            return "Invalid time";
        }

        Instant nowUtc = Instant.now();
        ZonedDateTime nowUser = nowUtc.atZone(userZone);
        ZonedDateTime futureUser = nowUser.plusMinutes(minutesToAdd);

        String timePart = futureUser.format(DateTimeFormatter.ofPattern("h:mm a"));

        if (nowUser.toLocalDate().equals(futureUser.toLocalDate())) {
            return "Arriving at " + timePart + " today";
        } else if (nowUser.plusDays(1).toLocalDate().equals(futureUser.toLocalDate())) {
            return "Expected at " + timePart + " tomorrow";
        } else {
            String dayName = capitalize(futureUser.getDayOfWeek().name().toLowerCase());
            return "Reaches on " + dayName + " at " + timePart;
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    // For testing
    public static void main(String[] args) {
        ZoneId india = ZoneId.of("Asia/Kolkata");
        ZoneId newYork = ZoneId.of("America/New_York");

        System.out.println(getFriendlyTimeAfterMinutes(30, india));
        System.out.println(getFriendlyTimeAfterMinutes(1500, india));
        System.out.println(getFriendlyTimeAfterMinutes(4320, newYork));
    }
}
