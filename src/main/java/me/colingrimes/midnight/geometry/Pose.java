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

public class Pose implements Serializable {

	private final Position position;
	private final Direction direction;
	private Location location;

	/**
	 * Constructs a new {@link Pose} with the given {@link Location}.
	 *
	 * @param location the location
	 * @return the pose
	 */
	@Nonnull
	public static Pose of(@Nonnull Location location) {
		Preconditions.checkArgument(location.getWorld() != null, "Location must have a world.");
		return Pose.of(Position.of(location), Direction.of(location));
	}

	/**
	 * Constructs a new {@link Pose} with the given {@link Position} and {@link Direction}.
	 *
	 * @param position the position
	 * @param direction the direction
	 * @return the pose
	 */
	public static Pose of(@Nonnull Position position, @Nonnull Direction direction) {
		return new Pose(position, direction);
	}

	private Pose(@Nonnull Position position, @Nonnull Direction direction) {
		this.position = position;
		this.direction = direction;
	}

	/**
	 * Gets the position of the pose.
	 *
	 * @return the position of the pose
	 */
	@Nonnull
	public Position getPosition() {
		return position;
	}

	/**
	 * Sets the position of the pose.
	 *
	 * @param position the new position
	 * @return the pose
	 */
	@Nonnull
	public Pose setPosition(@Nonnull Position position) {
		return Pose.of(position, direction);
	}

	/**
	 * Gets the direction of the pose.
	 *
	 * @return the direction of the pose
	 */
	@Nonnull
	public Direction getDirection() {
		return direction;
	}

	/**
	 * Sets the direction of the pose.
	 *
	 * @param direction the new direction
	 * @return the pose
	 */
	@Nonnull
	public Pose setDirection(@Nonnull Direction direction) {
		return Pose.of(position, direction);
	}

	/**
	 * Converts this pose into a {@link Location}.
	 *
	 * @return the location that this pose represents
	 */
	@Nonnull
	public Location toLocation() {
		if (location == null) {
			location = position.toLocation();
			location.setYaw((float) direction.getYaw());
			location.setPitch((float) direction.getPitch());
		}
		return location;
	}

	/**
	 * Gets the world of the pose.
	 *
	 * @return the world of the pose
	 */
	@Nonnull
	public World getWorld() {
		return position.getWorld();
	}

	/**
	 * Gets the x-coordinate of the pose.
	 *
	 * @return the x-coordinate of the pose
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
	 * Gets the y-coordinate of the pose.
	 *
	 * @return the y-coordinate of the pose
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
	 * Gets the z-coordinate of the pose.
	 *
	 * @return the z-coordinate of the pose
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

	/**
	 * Gets the yaw angle of the pose.
	 *
	 * @return the yaw angle of the pose
	 */
	public double getYaw() {
		return direction.getYaw();
	}

	/**
	 * Gets the pitch angle of the pose.
	 *
	 * @return the pitch angle of the pose
	 */
	public double getPitch() {
		return direction.getPitch();
	}

	@Override
	public boolean equals(@Nullable Object o) {
		if (!(o instanceof Pose pose)) return false;
		return Objects.equals(getPosition(), pose.getPosition()) && Objects.equals(getDirection(), pose.getDirection());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getPosition(), getDirection(), location);
	}

	@Nonnull
	@Override
	public String toString() {
		return "Pose{" +
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

	@Nonnull
	public static Pose deserialize(@Nonnull JsonElement element) {
		JsonObject object = Validator.checkJson(element, "position", "direction");
		return of(Position.deserialize(object.get("position")), Direction.deserialize(object.get("direction")));
	}
}