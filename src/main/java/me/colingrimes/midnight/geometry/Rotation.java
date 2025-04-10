package me.colingrimes.midnight.geometry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.colingrimes.midnight.serialize.Json;
import me.colingrimes.midnight.serialize.Serializable;
import me.colingrimes.midnight.util.misc.Validator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Represents a 3D direction in the form of yaw, pitch, and roll angles.
 */
public class Rotation extends Direction implements Serializable {

    private final double roll;

    /**
     * Constructs a new Rotation with default angles.
     *
     * @return the rotation
     */
    @Nonnull
    public static Rotation create() {
        return new Rotation(0, 0, 0);
    }

    /**
     * Constructs a new Rotation with the given yaw, pitch, and roll angles.
     *
     * @param yaw   the yaw angle
     * @param pitch the pitch angle
     * @param roll  the roll angle
     * @return the rotation
     */
    @Nonnull
    public static Rotation of(double yaw, double pitch, double roll) {
        return new Rotation(yaw, pitch, roll);
    }

    private Rotation(double yaw, double pitch, double roll) {
        super(yaw, pitch);
        this.roll = roll;
    }

    /**
     * Gets the roll angle.
     *
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

    @Nonnull
    @Override
    public JsonElement serialize() {
        return Json.of(super.serialize()).add("roll", roll).build();
    }

    @Nonnull
    public static Rotation deserialize(@Nonnull JsonElement element) {
        JsonObject object = Validator.checkJson(element, "yaw", "pitch", "roll");
        return of(object.get("yaw").getAsDouble(), object.get("pitch").getAsDouble(), object.get("roll").getAsDouble());
    }
}
