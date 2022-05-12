package com.github.imthenico.annihilation.api.world;

import com.github.imthenico.annihilation.api.util.Formatting;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.function.Supplier;

public interface LocationReference extends Supplier<Location> {

    Location original();

    @Override
    default Location get() {
        Location location = original();
        World world = Bukkit.getWorld(location.getWorld().getName());

        return new Location(
            world,
            location.getX(),
            location.getY(),
            location.getZ(),
            location.getYaw(),
            location.getPitch()
        );
    }

    default String toString(String format) {
        return Formatting.formatLocation(get(), 3, format);
    }
}