package me.colingrimes.midnight.geometry;

import com.google.common.base.Preconditions;
import me.colingrimes.midnight.serialize.Serializable;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Point<T extends Direction> implements Serializable {

	private final Position position;
	private final T direction;

	/**
	 * Constructs a new {@link Point} with the given {@link Player} and {@link Direction}.
	 *
	 * @param player    the player
	 * @param direction the direction
	 * @return the point
	 * @param <T> the type of direction
	 */
	@Nonnull
	public static <T extends Direction> Point<T> of(@Nonnull Player player, @Nonnull T direction) {
		return Point.of(player.getLocation(), direction);
	}

	/**
	 * Constructs a new {@link Point} with the given {@link Location} and {@link Direction}.
	 *
	 * @param location  the location
	 * @param direction the direction
	 * @return the point
	 * @param <T> the type of direction
	 */
	@Nonnull
	public static <T extends Direction> Point<T> of(@Nonnull Location location, @Nonnull T direction) {
		Preconditions.checkArgument(location.getWorld() != null, "Location must have a world.");
		return Point.of(Position.of(location.getWorld(), location.getX(), location.getY(), location.getZ()), direction);
	}

	/**
	 * Constructs a new {@link Point} with the given coordinates and {@link Direction}.
	 *
	 * @param world     the world
	 * @param x         the x-coordinate
	 * @param y         the y-coordinate
	 * @param z         the z-coordinate
	 * @param direction the direction
	 * @return the point
	 * @param <T> the type of direction
	 */
	@Nonnull
	public static <T extends Direction> Point<T> of(@Nonnull World world, double x, double y, double z, @Nonnull T direction) {
		return Point.of(Position.of(world, x, y, z), direction);
	}

	/**
	 * Constructs a new {@link Point} with the given {@link Position} and {@link Direction}.
	 *
	 * @param position  the position
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
	 *
	 * @return the position of the point
	 */
	@Nonnull
	public Position getPosition() {
		return position;
	}

	/**
	 * Sets the position of the point.
	 *
	 * @param position the new position
	 * @return the point
	 */
	@Nonnull
	public Point<T> setPosition(@Nonnull Position position) {
		return Point.of(position, direction);
	}

	/**
	 * Gets the direction of the point.
	 *
	 * @return the direction of the point
	 */
	@Nonnull
	public T getDirection() {
		return direction;
	}

	/**
	 * Sets the direction of the point.
	 *
	 * @param direction the new direction
	 * @return the point
	 */
	@Nonnull
	public Point<T> setDirection(@Nonnull T direction) {
		return Point.of(position, direction);
	}

	/**
	 * Gets the world of the point.
	 *
	 * @return the world of the point
	 */
	@Nonnull
	public World getWorld() {
		return position.getWorld();
	}

	/**
	 * Gets the x-coordinate of the point.
	 *
	 * @return the x-coordinate of the point
	 */
	public double getX() {
		return position.getX();
	}

	/**
	 * Gets the y-coordinate of the point.
	 *
	 * @return the y-coordinate of the point
	 */
	public double getY() {
		return position.getY();
	}

	/**
	 * Gets the z-coordinate of the point.
	 *
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

	@Nonnull
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.put("position", position.serialize());
		map.put("direction", direction.serialize());
		return map;
	}

	/**
	 * Deserializes a point from a map.
	 *
	 * @param map the map to deserialize from
	 * @return the deserialized point
	 */
	@SuppressWarnings("unchecked")
	@Nonnull
	public static <T extends Direction> Point<T> deserialize(@Nonnull Map<String, Object> map) {
		Preconditions.checkArgument(map.containsKey("position"));
		Preconditions.checkArgument(map.containsKey("direction"));
		Preconditions.checkArgument(map.get("position") instanceof Map);
		Preconditions.checkArgument(map.get("direction") instanceof Map);

		Position position = Position.deserialize((Map<String, Object>) map.get("position"));
		Map<String, Object> direction = (Map<String, Object>) map.get("direction");

		if (direction.containsKey("roll")) {
			return of(position, (T) Rotation.deserialize(direction));
		} else {
			return of(position, (T) Direction.deserialize(direction));
		}
	}
}