package me.colingrimes.midnight.util.bukkit;

import me.colingrimes.midnight.util.misc.Random;
import org.bukkit.Location;
import org.bukkit.block.Block;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Locations {

	/**
	 * Gets a random location in a radius around the specified location.
	 * This location will be at the highest Y at the random location.
	 *
	 * @param location the base location
	 * @param radius the radius around the given location
	 * @return a random location
	 */
	@Nonnull
	public static Optional<Location> random(@Nonnull Location location, int radius) {
		return random(location, radius, false);
	}

	/**
	 * Gets a random solid location in a radius around the specified location.
	 * This location will be at the highest Y at the random location.
	 *
	 * @param location the base location
	 * @param radius the radius around the given location
	 * @param solid whether the location has to be on a solid block
	 * @return a random solid location
	 */
	@Nonnull
	public static Optional<Location> random(@Nonnull Location location, int radius, boolean solid) {
		if (location.getWorld() == null) {
			return Optional.empty();
		}

		int attempts = 100;
		while (attempts-- > 0) {
			int x = Random.number(location.getBlockX() - radius, location.getBlockX() + radius);
			int z = Random.number(location.getBlockZ() - radius, location.getBlockZ() + radius);
			Block block = location.getWorld().getHighestBlockAt(x, z);
			if (block.getType().isSolid()) {
				return Optional.of(block.getLocation().add(0.5, 1, 0.5).setDirection(location.getDirection()));
			}
		}
		return Optional.empty();
	}

	/**
	 * Retrieves all locations between two locations.
	 *
	 * @param corner1 the first corner
	 * @param corner2 the second corner
	 * @return all blocks between the two locations
	 */
	@Nonnull
	public static List<Location> between(@Nonnull Location corner1, @Nonnull Location corner2) {
		double lowX = Math.min(corner1.getX(), corner2.getX());
		double lowY = Math.min(corner1.getY(), corner2.getY());
		double lowZ = Math.min(corner1.getZ(), corner2.getZ());

		List<Location> locations = new ArrayList<>();
		for (int blockY = Math.abs(corner1.getBlockY() - corner2.getBlockY()); blockY >= 0; blockY--) {
			for (int blockX = 0; blockX < Math.abs(corner1.getBlockX() - corner2.getBlockX()); blockX++) {
				for (int blockZ = 0; blockZ < Math.abs(corner1.getBlockZ() - corner2.getBlockZ()); blockZ++) {
					locations.add(new Location(corner1.getWorld(), lowX + blockX, lowY + blockY, lowZ + blockZ));
				}
			}
		}
		return locations;
	}

	/**
	 * Checks if the two locations have equal X, Y, Z block coordinates.
	 *
	 * @param location1 the first location
	 * @param location2 the second location
	 * @return true if the locations are equal
	 */
	public static boolean equal(@Nonnull Location location1, @Nonnull Location location2) {
		return location1.getBlock().equals(location2.getBlock());
	}

	/**
	 * Converts a location to a readable string.
	 *
	 * @param location the location
	 * @return the string
	 */
	@Nonnull
	public static String toString(@Nonnull Location location) {
		return "X=" + location.getBlockX() + " Y=" + location.getBlockY() + " Z=" + location.getBlockZ();
	}

	private Locations() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
