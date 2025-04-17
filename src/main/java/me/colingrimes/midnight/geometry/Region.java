package me.colingrimes.midnight.geometry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.colingrimes.midnight.functional.TriConsumer;
import me.colingrimes.midnight.functional.TriPredicate;
import me.colingrimes.midnight.serialize.Json;
import me.colingrimes.midnight.serialize.Serializable;
import me.colingrimes.midnight.util.misc.Validator;
import org.bukkit.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Represents a region with a minimum and maximum {@link Position}.
 * You can also retrieve the region's {@link Size}.
 */
public class Region implements Serializable {

	protected final Position min;
	protected final Position max;
	protected final Size size;

	/**
	 * Constructs a new {@link Region} with the given {@link Position} objects.
	 *
	 * @param pos1 the first position
	 * @param pos2 the second position
	 * @return the region
	 */
	@Nonnull
	public static Region of(@Nonnull Position pos1, @Nonnull Position pos2) {
		return new Region(pos1, pos2);
	}

	protected Region(@Nonnull Position pos1, @Nonnull Position pos2) {
		int x1 = pos1.getBlockX(), y1 = pos1.getBlockY(), z1 = pos1.getBlockZ();
		int x2 = pos2.getBlockX(), y2 = pos2.getBlockY(), z2 = pos2.getBlockZ();
		this.min = Position.of(pos1.getWorld(), Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2));
		this.max = Position.of(pos1.getWorld(), Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2));
		this.size = Size.of(max.getBlockX() - min.getBlockX() + 1, max.getBlockY() - min.getBlockY() + 1, max.getBlockZ() - min.getBlockZ() + 1);
	}

	/**
	 * Gets the minimum position of the region.
	 *
	 * @return the minimum position
	 */
	@Nonnull
	public Position getMin() {
		return min;
	}

	/**
	 * Gets the maximum position of the region.
	 *
	 * @return the maximum position
	 */
	@Nonnull
	public Position getMax() {
		return max;
	}

	/**
	 * Gets the size of the region.
	 *
	 * @return the region size
	 */
	@Nonnull
	public Size getSize() {
		return size;
	}

	/**
	 * Gets the world of the region.
	 *
	 * @return the region world
	 */
	@Nonnull
	public World getWorld() {
		return min.getWorld();
	}

	/**
	 * Checks if the region contains the specified position.
	 *
	 * @param pos the position
	 * @return true if the position is within the region
	 */
	public boolean contains(@Nonnull Position pos) {
		return containsWithin(pos, 0);
	}

	/**
	 * Checks if the region is within the specified distance from the position.
	 *
	 * @param pos the position
	 * @param distance the distance from the position to check
	 * @return true if the position is within {@code distance} blocks
	 */
	public boolean containsWithin(@Nonnull Position pos, double distance) {
		double x = pos.getX();
		double y = pos.getY();
		double z = pos.getZ();
		return x >= min.getX() - distance && x <= max.getX() + distance
				&& y >= min.getY() - distance && y <= max.getY() + distance
				&& z >= min.getZ() - distance && z <= max.getZ() + distance;
	}

	/**
	 * Performs mass actions on all block positions in the region.
	 *
	 * @param action the action to perform on all positions
	 */
	public void handler(@Nonnull TriConsumer<Integer, Integer, Integer> action) {
		for (int y=max.getBlockY(); y>=min.getBlockY(); y--) {
			for (int x=min.getBlockX(); x<=max.getBlockX(); x++) {
				for (int z=min.getBlockZ(); z<=max.getBlockZ(); z++) {
					action.accept(x, y, z);
				}
			}
		}
	}

	/**
	 * Performs mass actions on all block positions in the region.
	 * Stops early if one of the predicates returns false.
	 *
	 * @param action the action to perform on all positions
	 * @return true if the action was successful
	 */
	public boolean handler(@Nonnull TriPredicate<Integer, Integer, Integer> action) {
		for (int y=max.getBlockY(); y>=min.getBlockY(); y--) {
			for (int x=min.getBlockX(); x<=max.getBlockX(); x++) {
				for (int z=min.getBlockZ(); z<=max.getBlockZ(); z++) {
					if (!action.test(x, y, z)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean equals(@Nullable Object o) {
		if (!(o instanceof Region region)) return false;
		return Objects.equals(min, region.min) && Objects.equals(max, region.max);
	}

	@Override
	public int hashCode() {
		return Objects.hash(min, max);
	}

	@Nonnull
	@Override
	public String toString() {
		return "Region{" +
				"min=" + min +
				", max=" + max +
				", size=" + size +
				'}';
	}

	@Nonnull
	@Override
	public JsonElement serialize() {
		return Json.create()
				.add("min", min.serialize())
				.add("max", max.serialize())
				.build();
	}

	@Nonnull
	public static Region deserialize(@Nonnull JsonElement element) {
		JsonObject object = Validator.checkJson(element, "min", "max");
		return of(Position.deserialize(object.get("min")), Position.deserialize(object.get("max")));
	}
}
