package me.colingrimes.midnight.util;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;

public final class Locations {

	/**
	 * Converts a location to a very readable string.
	 * @param location the location
	 * @return the string
	 */
	public static String toString(Location location) {
		return "X=" + location.getBlockX() + " Y=" + location.getBlockY() + " Z=" + location.getBlockZ();
	}

	/**
	 * Finds the closest entity of the specified type to the given location within the given number of blocks.
	 * @param entityType the class of the desired entity type
	 * @param location the location
	 * @param blocks the number of blocks
	 * @param <T> the type of the entity
	 * @return the closest entity of the specified type
	 */
	@Nonnull
	public static <T extends Entity> Optional<T> findClosest(@Nonnull Class<T> entityType, @Nullable Location location, int blocks) {
		if (location == null || location.getWorld() == null) {
			return Optional.empty();
		}

		Collection<T> entities;
		T closestEntity = null;
		double closestDistance = Double.MAX_VALUE;

		// Only check nearby entities if there's less than 100 blocks to check.
		if (blocks <= 100) {
			entities = location.getWorld().getNearbyEntities(location, blocks, blocks, blocks)
					.stream()
					.filter(entityType::isInstance)
					.map(entityType::cast)
					.toList();
		} else {
			entities = location.getWorld().getEntitiesByClass(entityType);
		}

		// If the list exceeds the threshold, return an empty optional.
		if (entities.size() > 1000) {
			return Optional.empty();
		}

		// Find the closest entity.
		for (T entity : entities) {
			double distance = entity.getLocation().distanceSquared(location);
			if (distance < closestDistance) {
				closestEntity = entity;
				closestDistance = distance;
			}
		}

		return Optional.ofNullable(closestEntity);
	}

	private Locations() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
