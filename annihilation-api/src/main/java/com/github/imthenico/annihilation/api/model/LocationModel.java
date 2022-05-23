package com.github.imthenico.annihilation.api.model;

import com.github.imthenico.gmlib.world.AWorld;
import com.github.imthenico.gmlib.world.WorldContainer;
import com.github.imthenico.json.JsonSerializable;
import com.github.imthenico.json.JsonTreeBuilder;
import com.google.gson.JsonElement;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public class LocationModel implements JsonSerializable {

    public static LocationModel ZERO = new LocationModel(0, 0, 0, 0, 0);

    protected double x, y, z;
    protected float yaw, pitch;
    private String worldName;

    public LocationModel(double x, double y, double z, float yaw, float pitch) {
        this(x, y, z, yaw, pitch, null);
    }

    public LocationModel(double x, double y, double z, float yaw, float pitch, String worldName) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.worldName = worldName;
    }

    public LocationModel(double x, double y, double z, String worldName) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.worldName = worldName;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public LocationModel copy() {
        return new LocationModel(x, y, z, yaw, pitch, worldName);
    }

    public LocationModel add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public LocationModel add(double x, double y, double z, float yaw, float pitch) {
        this.x += x;
        this.y += y;
        this.z += z;
        this.yaw += yaw;
        this.pitch += pitch;
        return this;
    }

    public LocationModel subtract(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public LocationModel subtract(double x, double y, double z, float yaw, float pitch) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        this.yaw -= yaw;
        this.pitch -= pitch;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationModel that = (LocationModel) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0 && Double.compare(that.z, z) == 0 && Float.compare(that.yaw, yaw) == 0 && Float.compare(that.pitch, pitch) == 0 && Objects.equals(worldName, that.worldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, yaw, pitch, worldName);
    }

    @Override
    public @NotNull JsonElement serialize() {
        JsonTreeBuilder jsonTreeBuilder = JsonTreeBuilder.create();

        jsonTreeBuilder.add("x", x);
        jsonTreeBuilder.add("y", y);
        jsonTreeBuilder.add("z", z);
        jsonTreeBuilder.add("yaw", yaw);
        jsonTreeBuilder.add("pitch", pitch);

        if (worldName != null)
            jsonTreeBuilder.add("worldName", worldName);

        return jsonTreeBuilder.build();
    }

    public Location toBukkit(WorldContainer<?> worldContainer, String worldName) {
        Objects.requireNonNull(worldName, "worldName is null");

        AWorld aWorld = worldContainer.getByName(worldName);

        if (aWorld == null)
            throw new IllegalArgumentException("no world found with name '" + worldName + "' in container");

        return toBukkit((World) aWorld.getHandle());
    }

    public Location toBukkit(WorldContainer<?> worldContainer) {
        return toBukkit(worldContainer, worldName);
    }

    public Location toBukkit(World world) {
        return new Location(
                world,
                x,
                y,
                z,
                yaw,
                pitch
        );
    }

    public static LocationModel deserialize(Map<String, Object> objectMap) {
        return new LocationModel(
                (double) objectMap.get("x"),
                (double) objectMap.get("y"),
                (double) objectMap.get("z"),
                (float) objectMap.get("yaw"),
                (float) objectMap.get("pitch"),
                (String) objectMap.get("worldName")
        );
    }

    public static LocationModel fromBukkit(Location location) {
        return new LocationModel(
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch(),
                location.getWorld().getName()
        );
    }
}