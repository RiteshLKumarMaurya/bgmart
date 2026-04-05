package com.biharigraphic.jilamart.utils;

public class DistanceUtil {

    private static final double AVERAGE_SPEED_KMPH = 40.0; // Average speed in km/h

    /**
     * Estimates time to reach destination in total minutes based on distance.
     *
     * @param distanceInKm Distance in kilometers
     * @return Estimated time in minutes as a long, or -1 if invalid
     */
    public static long estimateTimeToReach(long distanceInKm) {
        if (distanceInKm <= 0) {
            return -1; // Invalid distance
        }

        // Calculate total minutes from distance and speed
        double timeInHours = distanceInKm / AVERAGE_SPEED_KMPH;
        long totalMinutes = Math.round(timeInHours * 60);

        return totalMinutes;
    }

    // For testing
    public static void main(String[] args) {
        System.out.println("10 km: " + estimateTimeToReach(10) + " min");
        System.out.println("50 km: " + estimateTimeToReach(50) + " min");
        System.out.println("0 km: " + estimateTimeToReach(0)); // returns -1
    }
}
