package com.biharigraphic.jilamart.utils;

public class DistanceTimeEstimatorUtil {

    // Default average speed in km/h (can be adjusted for walking, driving, etc.)
    private static final double AVERAGE_SPEED_KMPH = 40.0;

    /**
     * Calculates estimated time to reach the destination.
     *
     * @param distanceInKm Distance in kilometers
     * @return Time to reach as a formatted string "X hr Y min"
     */
    public static String estimateTimeToReach(long distanceInKm) {
        if (distanceInKm <= 0) {
            return "Invalid distance";
        }

        // Time in hours
        double timeInHours = distanceInKm / AVERAGE_SPEED_KMPH;

        int hours = (int) timeInHours;
        int minutes = (int) Math.round((timeInHours - hours) * 60);

        if (hours == 0) {
            return minutes + " min";
        } else {
            return hours + " hr " + minutes + " min";
        }
    }


}
