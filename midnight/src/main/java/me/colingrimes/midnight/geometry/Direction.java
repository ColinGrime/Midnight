package me.colingrimes.midnight.geometry;

import com.google.common.base.Preconditions;
import me.colingrimes.midnight.serialize.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a 3D direction in the form of yaw and pitch angles.
 */
public class Direction implements Serializable {

    private final double yaw;
    private final double pitch;

    /**
     * Constructs a new Direction with the given yaw and pitch angles.
     * @param yaw the yaw angle
     * @param pitch the pitch angle
     */
    public static Direction of(double yaw, double pitch) {
        return new Direction(yaw, pitch);
    }

    Direction(double yaw, double pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * Gets the yaw angle.
     * @return the yaw angle
     */
    public double getYaw() {
        return yaw;
    }

    /**
     * Gets the pitch angle.
     * @return the pitch angle
     */
    public double getPitch() {
        return pitch;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Direction direction = (Direction) o;
        return Double.compare(direction.getYaw(), getYaw()) == 0 && Double.compare(direction.getPitch(), getPitch()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getYaw(), getPitch());
    }

    @Nonnull
    @Override
    public String toString() {
        return "Direction{" +
                "yaw=" + yaw +
                ", pitch=" + pitch +
                '}';
    }

    @Nonnull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("yaw", yaw);
        map.put("pitch", pitch);
        return map;
    }

    /**
     * Deserializes a direction from a map.
     * @param map the map to deserialize from
     * @return the deserialized direction
     */
    @Nonnull
    public static Direction deserialize(@Nonnull Map<String, Object> map) {
        Preconditions.checkArgument(map.containsKey("yaw"));
        Preconditions.checkArgument(map.containsKey("pitch"));

        return of(
                (double) map.get("yaw"),
                (double) map.get("pitch")
        );
    }
}



