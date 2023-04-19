package me.colingrimes.midnight.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Represents a 3D direction in the form of yaw, pitch, and roll angles.
 */
public class Rotation extends Direction {

    private final double roll;

    /**
     * Constructs a new Rotation with the given yaw, pitch, and roll angles.
     * @param yaw the yaw angle
     * @param pitch the pitch angle
     * @param roll the roll angle
     */
    public static Rotation of(double yaw, double pitch, double roll) {
        return new Rotation(yaw, pitch, roll);
    }

    private Rotation(double yaw, double pitch, double roll) {
        super(yaw, pitch);
        this.roll = roll;
    }

    /**
     * Gets the roll angle.
     * @return the roll angle
     */
    public double getRoll() {
        return roll;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof Rotation rotation)) return false;
        if (!super.equals(o)) return false;
        return Double.compare(rotation.getRoll(), getRoll()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getRoll());
    }

    @Nonnull
    @Override
    public String toString() {
        return "Direction{" +
                "yaw=" + getYaw() +
                ", pitch=" + getPitch() +
                ", roll=" + getRoll() +
                '}';
    }
}
