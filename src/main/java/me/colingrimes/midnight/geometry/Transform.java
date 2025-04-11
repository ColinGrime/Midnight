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

public class Transform implements Serializable {

	private final Position position;
	private final Rotation rotation;
	private Location location;

	/**
	 * Constructs a new {@link Transform} with the given {@link Location}.
	 *
	 * @param location the location
	 * @return the rotation
	 */
	@Nonnull
	public static Transform of(@Nonnull Location location) {
		Preconditions.checkArgument(location.getWorld() != null, "Location must have a world.");
		return Transform.of(Position.of(location), Rotation.of(location.getYaw(), location.getPitch(), 0));
	}

	/**
	 * Constructs a new {@link Transform} with the given {@link Position} and {@link Rotation}.
	 *
	 * @param position the position
	 * @param rotation the rotation
	 * @return the transform
	 */
	public static Transform of(@Nonnull Position position, @Nonnull Rotation rotation) {
		return new Transform(position, rotation);
	}

	private Transform(@Nonnull Position position, @Nonnull Rotation rotation) {
		this.position = position;
		this.rotation = rotation;
	}

	/**
	 * Gets the position of the transform.
	 *
	 * @return the position of the transform
	 */
	@Nonnull
	public Position getPosition() {
		return position;
	}

	/**
	 * Sets the position of the transform.
	 *
	 * @param position the new position
	 * @return the transform
	 */
	@Nonnull
	public Transform setPosition(@Nonnull Position position) {
		return Transform.of(position, rotation);
	}

	/**
	 * Gets the rotation of the transform.
	 *
	 * @return the rotation of the transform
	 */
	@Nonnull
	public Rotation getRotation() {
		return rotation;
	}

	/**
	 * Sets the rotation of the transform.
	 *
	 * @param rotation the new rotation
	 * @return the transform
	 */
	@Nonnull
	public Transform setRotation(@Nonnull Rotation rotation) {
		return Transform.of(position, rotation);
	}

	/**
	 * Converts this transform into a {@link Location}.
	 *
	 * @return the location that this transform represents
	 */
	@Nonnull
	public Location toLocation() {
		if (location == null) {
			location = position.toLocation();
			location.setYaw((float) rotation.getYaw());
			location.setPitch((float) rotation.getPitch());
		}
		return location;
	}

	/**
	 * Gets the world of the transform.
	 *
	 * @return the world of the transform
	 */
	@Nonnull
	public World getWorld() {
		return position.getWorld();
	}

	/**
	 * Gets the x-coordinate of the transform.
	 *
	 * @return the x-coordinate of the transform
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
	 * Gets the y-coordinate of the transform.
	 *
	 * @return the y-coordinate of the transform
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
	 * Gets the z-coordinate of the transform.
	 *
	 * @return the z-coordinate of the transform
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
	 * Gets the yaw angle of the transform.
	 *
	 * @return the yaw angle of the transform
	 */
	public double getYaw() {
		return rotation.getYaw();
	}

	/**
	 * Gets the pitch angle of the transform.
	 *
	 * @return the pitch angle of the transform
	 */
	public double getPitch() {
		return rotation.getPitch();
	}

	/**
	 * Gets the roll angle of the transform.
	 *
	 * @return the roll angle of the transform
	 */
	public double getRoll() {
		return rotation.getYaw();
	}

	@Override
	public boolean equals(@Nullable Object o) {
		if (!(o instanceof Transform transform)) return false;
		return Objects.equals(getPosition(), transform.getPosition()) && Objects.equals(getRotation(), transform.getRotation());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getPosition(), getRotation(), location);
	}

	@Nonnull
	@Override
	public String toString() {
		return "rotation{" +
				"position=" + position +
				", rotation=" + rotation +
				'}';
	}

	@Nonnull
	@Override
	public JsonElement serialize() {
		return Json.create()
				.add("position", position.serialize())
				.add("rotation", rotation.serialize())
				.build();
	}

	@Nonnull
	public static Transform deserialize(@Nonnull JsonElement element) {
		JsonObject object = Validator.checkJson(element, "position", "rotation");
		return of(Position.deserialize(object.get("position")), Rotation.deserialize(object.get("rotation")));
	}
}