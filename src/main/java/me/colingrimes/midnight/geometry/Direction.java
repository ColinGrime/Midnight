package me.colingrimes.midnight.geometry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.colingrimes.midnight.serialize.Json;
import me.colingrimes.midnight.serialize.Serializable;
import me.colingrimes.midnight.util.misc.Validator;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Represents a direction in the form of yaw and pitch angles.
 */
public class Direction implements Serializable {

    private final double yaw;
    private final double pitch;

    /**
     * Constructs a new {@link Direction} with default angles.
     *
     * @return the direction
     */
    @Nonnull
    public static Direction create() {
        return of(0, 0);
    }

    /**
     * Constructs a new {@link Direction} from the location's yaw and pitch values.
     *
     * @param location the location
     * @return the direction
     */
    @Nonnull
    public static Direction of(@Nonnull Location location) {
        return of(location.getYaw(), location.getPitch());
    }

    /**
     * Constructs a new {@link Direction} with the given yaw and pitch angles.
     *
     * @param yaw the yaw angle
     * @param pitch the pitch angle
     * @return the direction
     */
    @Nonnull
    public static Direction of(double yaw, double pitch) {
        return new Direction(yaw, pitch);
    }

    Direction(double yaw, double pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * Gets the yaw angle.
     *
     * @return the yaw angle
     */
    public double getYaw() {
        return yaw;
    }

    /**
     * Gets the pitch angle.
     *
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
    public JsonElement serialize() {
        return Json.create()
                .add("yaw", yaw)
                .add("pitch", pitch)
                .build();
    }

    @Nonnull
    public static Direction deserialize(@Nonnull JsonElement element) {
        JsonObject object = Validator.checkJson(element, "yaw", "pitch");
        return of(object.get("yaw").getAsDouble(), object.get("pitch").getAsDouble());
    }
}
