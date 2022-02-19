package com.github.imthenico.annihilation.api.util;

import com.github.imthenico.simplecommons.minecraft.LocationModel;
import org.bukkit.Location;

import java.text.NumberFormat;

public class Formatting {

    private final static NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();

    public synchronized static String formatAxis(
            double x,
            double y,
            double z,
            float yaw,
            float pitch,
            int maximumDecimals,
            String format
    ) {
        int oldMaximumDecimals = NUMBER_FORMAT.getMaximumFractionDigits();
        NUMBER_FORMAT.setMaximumFractionDigits(maximumDecimals);

        String formattedX = NUMBER_FORMAT.format(x);
        String formattedY = NUMBER_FORMAT.format(y);
        String formattedZ = NUMBER_FORMAT.format(z);
        String formattedYaw = NUMBER_FORMAT.format(yaw);
        String formattedPitch = NUMBER_FORMAT.format(pitch);

        NUMBER_FORMAT.setMaximumFractionDigits(oldMaximumDecimals);

        return String.format(format, formattedX, formattedY, formattedZ, formattedYaw, formattedPitch);
    }

    public static String formatAxis(
            double x,
            double y,
            double z,
            int maximumDecimals,
            String format
    ) {
        return formatAxis(x, y, z, 0F, 0F, maximumDecimals, format);
    }

    public static String formatLocation(Location location, int maximumDecimals, String format) {
        return formatAxis(location.getX(), location.getY(), location.getZ(),
                location.getYaw(), location.getPitch(), maximumDecimals, format);
    }

    public static String formatLocation(LocationModel location, int maximumDecimals, String format) {
        return formatAxis(location.getX(), location.getY(), location.getZ(),
                location.getYaw(), location.getPitch(), maximumDecimals, format);
    }
}