package me.colingrimes.midnight.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Represents a 3D direction in the form of pitch, yaw, and roll angles.
 */
public class Rotation extends Direction {

    private final double roll;

    /**
     * Constructs a new Rotation with the given pitch, yaw, and roll angles.
     * @param pitch the pitch angle
     * @param yaw the yaw angle
     * @param roll the roll angle
     */
    public static Rotation of(double pitch, double yaw, double roll) {
        return new Rotation(pitch, yaw, roll);
    }

    private Rotation(double pitch, double yaw, double roll) {
        super(pitch, yaw);
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
                "pitch=" + getPitch() +
                ", yaw=" + getYaw() +
                ", roll=" + getRoll() +
                '}';
    }
}
