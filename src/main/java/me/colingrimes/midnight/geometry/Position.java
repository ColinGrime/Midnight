package me.colingrimes.midnight.geometry;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.colingrimes.midnight.serialize.Json;
import me.colingrimes.midnight.serialize.Serializable;
import me.colingrimes.midnight.util.bukkit.Worlds;
import me.colingrimes.midnight.util.misc.Validator;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a 3D position in the form of x, y, and z coordinates.
 */
public class Position implements Serializable {

    private final World world;
    private final double x;
    private final double y;
    private final double z;
    private Location location;

    /**
     * Constructs a new Position with the given location.
     *
     * @param location the location
     */
    public static Position of(@Nonnull Location location) {
        World world = Objects.requireNonNull(location.getWorld());
        return new Position(world, location.getX(), location.getY(), location.getZ());
    }

    /**
     * Constructs a new Position with the given x, y, and z coordinates.
     *
     * @param world the world
     * @param x     the x coordinate
     * @param y     the y coordinate
     * @param z     the z coordinate
     */
    public static Position of(@Nonnull World world, double x, double y, double z) {
        return new Position(world, x, y, z);
    }

    private Position(@Nonnull World world, double x, double y, double z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Adds the given x, y, and z values to this position.
     *
     * @param x the x value to add
     * @param y the y value to add
     * @param z the z value to add
     * @return a new position with the updated coordinates
     */
    @Nonnull
    public Position add(double x, double y, double z) {
        return new Position(this.world, this.x + x, this.y + y, this.z + z);
    }

    /**
     * Subtracts the given x, y, and z values from this position.
     *
     * @param x the x value to subtract
     * @param y the y value to subtract
     * @param z the z value to subtract
     * @return a new position with the updated coordinates
     */
    @Nonnull
    public Position subtract(double x, double y, double z) {
        return new Position(this.world, this.x - x, this.y - y, this.z - z);
    }

    /**
     * Converts this position into a {@link Location}.
     *
     * @return the location that this position represents
     */
    @Nonnull
    public Location toLocation() {
        if (location == null) {
            location = new Location(world, x, y, z);
        }
        return location;
    }

    /**
     * Converts this position into a {@link Block}.
     *
     * @return the block that this position represents
     */
    @Nonnull
    public Block toBlock() {
        return toLocation().getBlock();
    }

    /**
     * Gets the world of this position.
     *
     * @return the world
     */
    @Nonnull
    public World getWorld() {
        return world;
    }

    /**
     * Gets the x-coordinate.
     *
     * @return the x-coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the x-coordinate as an int.
     *
     * @return the x-coordinate as an int
     */
    public int getBlockX() {
        return (int) Math.floor(x);
    }

    /**
     * Gets the y-coordinate.
     *
     * @return the y-coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Gets the y-coordinate as an int.
     *
     * @return the y-coordinate as an int
     */
    public int getBlockY() {
        return (int) Math.floor(y);
    }

    /**
     * Gets the z-coordinate.
     *
     * @return the z-coordinate
     */
    public double getZ() {
        return z;
    }

    /**
     * Gets the z-coordinate as an int.
     *
     * @return the z-coordinate as an int
     */
    public int getBlockZ() {
        return (int) Math.floor(z);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Double.compare(position.getX(), getX()) == 0 && Double.compare(position.getY(), getY()) == 0 && Double.compare(position.getZ(), getZ()) == 0 && Objects.equals(world, position.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, y, z);
    }

    @Nonnull
    @Override
    public String toString() {
        return "Position{" +
                "world=" + world +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Nonnull
    @Override
    public JsonElement serialize() {
        return Json.create()
                .add("world", world.getName())
                .add("x", x)
                .add("y", y)
                .add("z", z)
                .build();
    }

    @Nonnull
    public static Position deserialize(@Nonnull JsonElement element) {
        JsonObject object = Validator.checkJson(element, "world", "x", "y", "z");
        Optional<World> world = Worlds.get(object.get("world").getAsString());
        Preconditions.checkArgument(world.isPresent());

        double x = object.get("x").getAsDouble();
        double y = object.get("y").getAsDouble();
        double z = object.get("z").getAsDouble();
        return of(world.get(), x, y, z);
    }
}
