package me.colingrimes.midnight.util.bukkit;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;

public final class Entities {

	/**
	 * Spawns the entity type at the given location.
	 *
	 * @param location the location to spawn the entity
	 * @param entityType the entity type to spawn
	 */
	public static void spawn(@Nonnull Location location, @Nonnull EntityType entityType) {
		Preconditions.checkNotNull(location.getWorld(), "World is null.");
		location.getWorld().spawnEntity(location, entityType);
	}

	/**
	 * Gets all nearby entities.
	 *
	 * @param location the location
	 * @param distance the distance
	 * @return all nearby entities
	 */
	@Nonnull
	public static Collection<Entity> nearby(@Nonnull Location location, double distance) {
		return nearby(location, distance, distance, distance);
	}

	/**
	 * Gets all nearby entities.
	 *
	 * @param location the location
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return all nearby entities
	 */
	@Nonnull
	public static Collection<Entity> nearby(@Nonnull Location location, double x, double y, double z) {
		Preconditions.checkNotNull(location.getWorld(), "World is null.");
		return location.getWorld().getNearbyEntities(location, x, y, z);
	}

	/**
	 * Finds the closest entity of the specified type to the given location within the given number of blocks.
	 *
	 * @param entityType the class of the desired entity type
	 * @param location   the location
	 * @param blocks     the number of blocks
	 * @param <T>        the type of the entity
	 * @return the closest entity of the specified type
	 */
	@Nonnull
	public static <T extends Entity> Optional<T> find(@Nonnull Class<T> entityType, @Nullable Location location, int blocks) {
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

	/**
	 * Forces the {@link Entity} to look at the given {@link Location}.
	 *
	 * @param entity the entity
	 * @param target the target location to look at
	 */
	public static void lookAt(@Nonnull Entity entity, @Nonnull Location target) {
		Location location = entity.getLocation();

		// Calculate direction to target.
		double dx = target.getX() - location.getX();
		double dy = target.getY() - (location.getY() + 1.62);
		double dz = target.getZ() - location.getZ();

		// Calculate yaw (horizontal rotation).
		double yaw = Math.toDegrees(Math.atan2(-dx, dz));

		// Calculate pitch (vertical rotation).
		double distanceXZ = Math.sqrt(dx * dx + dz * dz);
		double pitch = Math.toDegrees(Math.atan2(-dy, distanceXZ));

		// Set player's rotation.
		location.setYaw((float) yaw);
		location.setPitch((float) pitch);
		entity.teleport(location);
	}

	private Entities() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
