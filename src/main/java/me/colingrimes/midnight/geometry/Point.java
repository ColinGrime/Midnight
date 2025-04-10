package me.colingrimes.midnight.geometry;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.colingrimes.midnight.serialize.Json;
import me.colingrimes.midnight.serialize.Serializable;
import me.colingrimes.midnight.util.misc.Validator;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class Point<T extends Direction> implements Serializable {

	private final Position position;
	private final T direction;

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
		return Point.of(Position.of(location), direction);
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
	 * Converts this point into a {@link Location}.
	 *
	 * @return a new Location object representing this point
	 */
	@Nonnull
	public Location toLocation() {
		Location location = position.toLocation();
		location.setYaw((float) direction.getYaw());
		location.setPitch((float) direction.getPitch());
		return location;
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
	 * Gets the x-coordinate as an int.
	 *
	 * @return the x-coordinate as an int
	 */
	public int getBlockX() {
		return position.getBlockX();
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
	 * Gets the y-coordinate as an int.
	 *
	 * @return the y-coordinate as an int
	 */
	public int getBlockY() {
		return position.getBlockY();
	}

	/**
	 * Gets the z-coordinate of the point.
	 *
	 * @return the z-coordinate of the point
	 */
	public double getZ() {
		return position.getZ();
	}

	/**
	 * Gets the z-coordinate as an int.
	 *
	 * @return the z-coordinate as an int
	 */
	public int getBlockZ() {
		return position.getBlockZ();
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
	public JsonElement serialize() {
		return Json.create()
				.add("position", position.serialize())
				.add("direction", direction.serialize())
				.build();
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public static <T extends Direction> Point<T> deserialize(@Nonnull JsonElement element) {
		JsonObject object = Validator.checkJson(element, "position", "direction");
		Position position = Position.deserialize(object.get("position"));
		JsonObject direction = (JsonObject) object.get("direction");

		if (direction.has("pitch")) {
			return of(position, (T) Rotation.deserialize(direction));
		} else {
			return of(position, (T) Direction.deserialize(direction));
		}
	}
}