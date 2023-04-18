package me.colingrimes.midnight.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Represents a 3D direction in the form of pitch and yaw angles.
 */
public class Direction {

    private final double pitch;
    private final double yaw;

    /**
     * Constructs a new Direction with the given pitch and yaw angles.
     * @param pitch The pitch angle.
     * @param yaw   The yaw angle.
     */
    public static Direction of(double pitch, double yaw) {
        return new Direction(pitch, yaw);
    }

    Direction(double pitch, double yaw) {
        this.pitch = pitch;
        this.yaw = yaw;
    }

    /**
     * Gets the pitch angle.
     * @return the pitch angle
     */
    public double getPitch() {
        return pitch;
    }

    /**
     * Gets the yaw angle.
     * @return the yaw angle
     */
    public double getYaw() {
        return yaw;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Direction direction = (Direction) o;
        return Double.compare(direction.getPitch(), getPitch()) == 0 && Double.compare(direction.getYaw(), getYaw()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPitch(), getYaw());
    }

    @Nonnull
    @Override
    public String toString() {
        return "Direction{" +
                "pitch=" + pitch +
                ", yaw=" + yaw +
                '}';
    }
}



