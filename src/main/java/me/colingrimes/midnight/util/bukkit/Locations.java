package me.colingrimes.midnight.util.bukkit;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public final class Locations {

	/**
	 * Retrieves all locations between two locations.
	 *
	 * @param corner1 the first corner
	 * @param corner2 the second corner
	 * @return all blocks between the two locations
	 */
	public static List<Location> between(Location corner1, Location corner2) {
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
	 * Gets the direction from the source location to the target location.
	 *
	 * @param source the source location
	 * @param target the target location
	 * @return the resulting unit vector
	 */
	@Nonnull
	public static Vector direction(@Nonnull Location source, @Nonnull Location target) {
		return target.toVector().subtract(source.toVector()).normalize();
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
	public static String toString(Location location) {
		return "X=" + location.getBlockX() + " Y=" + location.getBlockY() + " Z=" + location.getBlockZ();
	}

	private Locations() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
