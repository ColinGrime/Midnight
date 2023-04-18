package me.colingrimes.midnight.model;

import org.bukkit.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Represents a 3D position in the form of x, y, and z coordinates.
 */
public class Position {

    private final World world;
    private final double x;
    private final double y;
    private final double z;

    /**
     * Constructs a new Position with the given x, y, and z coordinates.
     * @param world the world
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
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
     * Gets the x coordinate.
     * @return the x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y coordinate.
     * @return the y coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Gets the z coordinate.
     * @return the z coordinate
     */
    public double getZ() {
        return z;
    }

    /**
     * Adds the given x, y, and z values to this position.
     * @param x the x value to add
     * @param y the y value to add
     * @param z the z value to add
     * @return a new Position with the updated coordinates
     */
    @Nonnull
    public Position add(double x, double y, double z) {
        return new Position(this.world, this.x + x, this.y + y, this.z + z);
    }

    /**
     * Subtracts the given x, y, and z values from this position.
     * @param x the x value to subtract
     * @param y the y value to subtract
     * @param z the z value to subtract
     * @return a new Position with the updated coordinates
     */
    @Nonnull
    public Position subtract(double x, double y, double z) {
        return new Position(this.world, this.x - x, this.y - y, this.z - z);
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
}
