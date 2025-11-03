package com.glamora_store.util;

import com.glamora_store.enums.ErrorMessage;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility class for calculating distance between two coordinates using
 * Haversine formula
 */
public class DistanceCalculator {

    // Store location coordinates: SPKT (Glamora Store)
    public static final BigDecimal STORE_LATITUDE = new BigDecimal("10.850769");
    public static final BigDecimal STORE_LONGITUDE = new BigDecimal("106.771848");

    private static final double EARTH_RADIUS_KM = 6371.0; // Earth radius in kilometers

    /**
     * Calculate distance between two coordinates using Haversine formula
     *
     * @param lat1 Latitude of first point
     * @param lng1 Longitude of first point
     * @param lat2 Latitude of second point
     * @param lng2 Longitude of second point
     * @return Distance in kilometers (rounded to 2 decimal places)
     */
    public static BigDecimal calculateDistance(
            BigDecimal lat1,
            BigDecimal lng1,
            BigDecimal lat2,
            BigDecimal lng2) {

        if (lat1 == null || lng1 == null || lat2 == null || lng2 == null) {
            return BigDecimal.ZERO;
        }

        double lat1Rad = Math.toRadians(lat1.doubleValue());
        double lng1Rad = Math.toRadians(lng1.doubleValue());
        double lat2Rad = Math.toRadians(lat2.doubleValue());
        double lng2Rad = Math.toRadians(lng2.doubleValue());

        double dLat = lat2Rad - lat1Rad;
        double dLng = lng2Rad - lng1Rad;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS_KM * c;

        // Round to 2 decimal places
        return BigDecimal.valueOf(distance).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculate shipping fee based on distance (2000 VND per km)
     *
     * @param distance Distance in kilometers
     * @return Shipping fee in VND
     * @throws IllegalArgumentException if distance is null or negative
     */
    public static BigDecimal calculateShippingFee(BigDecimal distance) {
        if (distance == null) {
            throw new IllegalArgumentException(ErrorMessage.DISTANCE_NULL.getMessage());
        }
        if (distance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(ErrorMessage.DISTANCE_NEGATIVE.getMessage());
        }
        if (distance.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal ratePerKm = new BigDecimal("2000"); // 2000 VND per km
        return distance.multiply(ratePerKm).setScale(0, RoundingMode.HALF_UP);
    }
}
