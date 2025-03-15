package me.colingrimes.midnight.util.bukkit;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

public class Vectors {

	/**
	 * Gets the direction from the source vector to the target vector.
	 *
	 * @param source the source vector
	 * @param target the target vector
	 * @return the resulting unit vector
	 */
	@Nonnull
	public static Vector direction(@Nonnull Vector source, @Nonnull Vector target) {
		return target.subtract(source).normalize();
	}

	/**
	 * Gets the direction from the source location to the target location.
	 *
	 * @param source the source location
	 * @param target the target location
	 * @return the resulting unit vector
	 */
	@Nonnull
	public static Vector direction(@Nonnull Location source, @Nonnull Location target) {
		return direction(source.toVector(), target.toVector());
	}

	/**
	 * Gets the direction from the source entity to the target entity.
	 *
	 * @param source the source entity
	 * @param target the target entity
	 * @return the resulting unit vector
	 */
	@Nonnull
	public static Vector direction(@Nonnull Entity source, @Nonnull Entity target) {
		return direction(source.getLocation(), target.getLocation());
	}

	private Vectors() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
