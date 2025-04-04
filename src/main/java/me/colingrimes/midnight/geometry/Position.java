package me.colingrimes.midnight.geometry;

import com.google.common.base.Preconditions;
import me.colingrimes.midnight.serialize.Serializable;
import me.colingrimes.midnight.util.bukkit.Worlds;
import me.colingrimes.midnight.util.misc.Validator;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Represents a 3D position in the form of x, y, and z coordinates.
 */
public class Position implements Serializable {

    private final World world;
    private final double x;
    private final double y;
    private final double z;

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
     * @return a new Position with the updated coordinates
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
     * @return a new Position with the updated coordinates
     */
    @Nonnull
    public Position subtract(double x, double y, double z) {
        return new Position(this.world, this.x - x, this.y - y, this.z - z);
    }

    /**
     * Rotates the position using the specified rotation's pitch, yaw, and roll angles.
     *
     * @param rotation the rotation to use
     * @return a new position rotated by the pitch, yaw, and roll angles
     */
    @Nonnull
    public Position rotate(@Nonnull Rotation rotation) {
        // Convert angles to radians.
        double yawRadians = Math.toRadians(rotation.getYaw());
        double pitchRadians = Math.toRadians(rotation.getPitch());
        double rollRadians = Math.toRadians(rotation.getRoll());

        // Calculate sin and cos values for each angle
        double sinYaw = Math.sin(yawRadians);
        double cosYaw = Math.cos(yawRadians);
        double sinPitch = Math.sin(pitchRadians);
        double cosPitch = Math.cos(pitchRadians);
        double sinRoll = Math.sin(rollRadians);
        double cosRoll = Math.cos(rollRadians);

        // Calculate the rotation matrices for yaw, pitch, and roll.
        double[][] yawMatrix = {
                {cosYaw, 0, -sinYaw},
                {0, 1, 0},
                {sinYaw, 0, cosYaw}
        };

        double[][] pitchMatrix = {
                {1, 0, 0},
                {0, cosPitch, sinPitch},
                {0, -sinPitch, cosPitch}
        };

        double[][] rollMatrix = {
                {cosRoll, sinRoll, 0},
                {-sinRoll, cosRoll, 0},
                {0, 0, 1}
        };

        // Multiply pitch, yaw, and roll matrices to get the final rotation matrix.
        double[][] combinedMatrix = multiplyMatrices(multiplyMatrices(yawMatrix, pitchMatrix), rollMatrix);

        // Apply the rotation matrix to the input position vector.
        double newX = combinedMatrix[0][0] * x + combinedMatrix[0][1] * y + combinedMatrix[0][2] * z;
        double newY = combinedMatrix[1][0] * x + combinedMatrix[1][1] * y + combinedMatrix[1][2] * z;
        double newZ = combinedMatrix[2][0] * x + combinedMatrix[2][1] * y + combinedMatrix[2][2] * z;

        return Position.of(world, newX, newY, newZ);
    }

    /**
     * Multiplies two 3x3 matrices.
     *
     * @param matrix1 the first matrix
     * @param matrix2 the second matrix
     * @return the product of the two matrices
     */
    private double[][] multiplyMatrices(@Nonnull double[][] matrix1, @Nonnull double[][] matrix2) {
        double[][] result = new double[3][3];
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                for (int k=0; k<3; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return result;
    }

    /**
     * Converts this position into a {@link Location}.
     *
     * @return a new Location object representing this position
     */
    @Nonnull
    public Location toLocation() {
        return new Location(world, x, y, z);
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
     * Gets the x coordinate.
     *
     * @return the x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y coordinate.
     *
     * @return the y coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Gets the z coordinate.
     *
     * @return the z coordinate
     */
    public double getZ() {
        return z;
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
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("world", world.getName());
        map.put("x", x);
        map.put("y", y);
        map.put("z", z);
        return map;
    }

    @Nonnull
    public static Position deserialize(@Nonnull Map<String, Object> map) {
        Validator.checkMap(map, "world", "x", "y", "z");

        Optional<World> world = Worlds.get((String) map.get("world"));
        Preconditions.checkArgument(world.isPresent());

        double x = (double) map.get("x");
        double y = (double) map.get("y");
        double z = (double) map.get("z");
        return of(world.get(), x, y, z);
    }
}
