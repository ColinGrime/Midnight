package me.colingrimes.midnight.geometry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Represents a 3D direction in the form of yaw and pitch angles.
 */
public class Direction {

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
}



