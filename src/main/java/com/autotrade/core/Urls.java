package com.autotrade.core;

import java.util.Base64;
import java.util.UUID;

public final class Urls {
    private static final String DEFAULT_VEHICLE_IMAGE = "/images/default-vehicle-logo.png";

    public static String vehicle(UUID id) {
        return "/vehicle/" + id;
    }

    public static String vehicleImage(UUID vehicleId, byte[] data) {
        if (data != null && data.length > 0) {
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(data);
        }

        return DEFAULT_VEHICLE_IMAGE;
    }

    private Urls() {
    }
}
