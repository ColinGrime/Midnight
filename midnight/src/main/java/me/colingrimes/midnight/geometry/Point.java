package me.colingrimes.midnight.geometry;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class Point<T extends Direction> {

	private final Position position;
	private final T direction;

	/**
	 * Constructs a new Point with the given player.
	 * @param player the player
	 * @return the point
	 */
	@Nonnull
	public static Point<Rotation> of(@Nonnull Player player) {
		return Point.of(player.getLocation());
	}

	/**
	 * Constructs a new Point with the given location.
	 * @param location the location
	 * @return the point
	 */
	@Nonnull
	public static Point<Rotation> of(@Nonnull Location location) {
		return Point.of(location, 0, 0 ,0);
	}

	/**
	 * Constructs a new Point with the given player and direction.
	 * @param player the player
	 * @param yaw the yaw
	 * @param pitch the pitch
	 * @param roll the roll
	 * @return the point
	 */
	@Nonnull
	public static Point<Rotation> of(@Nonnull Player player, double yaw, double pitch, double roll) {
		return Point.of(player.getLocation(), yaw, pitch, roll);
	}

	/**
	 * Constructs a new Point with the given location and direction.
	 * @param location location
	 * @param yaw the yaw
	 * @param pitch the pitch
	 * @param roll the roll
	 * @return the point
	 */
	@Nonnull
	public static Point<Rotation> of(@Nonnull Location location, double yaw, double pitch, double roll) {
		Objects.requireNonNull(location.getWorld(), "World is null.");
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		return Point.of(location.getWorld(), x, y, z, Rotation.of(yaw, pitch, roll));
	}

	/**
	 * Constructs a new Point with the given coordinates and direction.
	 * @param world the world
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 * @param z the z-coordinate
	 * @param direction the direction
	 * @return the point
	 * @param <T> the type of direction
	 */
	@Nonnull
	public static <T extends Direction> Point<T> of(@Nonnull World world, double x, double y, double z, @Nonnull T direction) {
		return Point.of(Position.of(world, x, y, z), direction);
	}

	/**
	 * Constructs a new Point with the given position and direction.
	 * @param position the position
	 * @param direction the direction
	 * @return the point
	 * @param <T> the type of direction
	 */
	public static <T extends Direction> Point<T> of(@Nonnull Position position, @Nonnull T direction) {
		return new Point<>(position, direction);
	}

	private Point(@Nonnull Position position, @Nonnull T direction) {
		this.position = position;
		this.direction = direction;
	}

	/**
	 * Gets the position of the point.
	 * @return the position of the point
	 */
	@Nonnull
	public Position getPosition() {
		return position;
	}

	/**
	 * Sets the position of the point.
	 * @param position the new position
	 * @return the point
	 */
	@Nonnull
	public Point<T> setPosition(@Nonnull Position position) {
		return Point.of(position, direction);
	}

	/**
	 * Gets the direction of the point.
	 * @return the direction of the point
	 */
	@Nonnull
	public T getDirection() {
		return direction;
	}

	/**
	 * Sets the direction of the point.
	 * @param direction the new direction
	 * @return the point
	 */
	@Nonnull
	public Point<T> setDirection(@Nonnull T direction) {
		return Point.of(position, direction);
	}

	/**
	 * Gets the world of the point.
	 * @return the world of the point
	 */
	@Nonnull
	public World getWorld() {
		return position.getWorld();
	}

	/**
	 * Gets the x-coordinate of the point.
	 * @return the x-coordinate of the point
	 */
	public double getX() {
		return position.getX();
	}

	/**
	 * Gets the y-coordinate of the point.
	 * @return the y-coordinate of the point
	 */
	public double getY() {
		return position.getY();
	}

	/**
	 * Gets the z-coordinate of the point.
	 * @return the z-coordinate of the point
	 */
	public double getZ() {
		return position.getZ();
	}

	@Override
	public boolean equals(@Nullable Object o) {
		if (this == o) return true;
		if (!(o instanceof Point<?> point)) return false;
		return Objects.equals(getPosition(), point.getPosition()) && Objects.equals(getDirection(), point.getDirection());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getPosition(), getDirection());
	}

	@Nonnull
	@Override
	public String toString() {
		return "Point{" +
				"position=" + position +
				", direction=" + direction +
				'}';
	}
}