package me.colingrimes.midnight.hologram;

import me.colingrimes.midnight.geometry.Position;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a hologram that can be spawned in the world.
 */
public interface Hologram {

	Set<Hologram> SPAWNED_HOLOGRAMS = new HashSet<>();

	/**
	 * Constructs a new {@link BasicHologram} at the given position.
	 *
	 * @param position the position
	 * @return the hologram
	 */
	@Nonnull
	static Hologram of(@Nonnull Position position) {
		return new BasicHologram(position);
	}

	/**
	 * Spawns the hologram in at its set {@link Position}.
	 */
	void spawn();

	/**
	 * Removes the hologram from the world.
	 */
	void remove();

	/**
	 * Checks if the hologram is currently active.
	 *
	 * @return true if the hologram is active
	 */
	boolean active();

	/**
	 * Gets the {@link Position} of the hologram.
	 *
	 * @return the hologram's position
	 */
	@Nonnull
	Position getPosition();

	/**
	 * Gets the lines of the hologram.
	 *
	 * @return the hologram lines
	 */
	@Nonnull
	List<String> getLines();

	/**
	 * Sets the lines of the hologram.
	 *
	 * @param newLines the hologram lines
	 */
	void setLines(@Nonnull List<String> newLines);

	/**
	 * Sets a line of text at the specified index.
	 *
	 * @param index the index to set the line
	 * @param text the text of the line
	 */
	void setLine(int index, @Nonnull String text);

	/**
	 * Adds a line of text to the hologram.
	 *
	 * @param text the text of the line
	 */
	void addLine(@Nonnull String text);

	/**
	 * Removes the line at the specified index.
	 *
	 * @param index the index to remove
	 */
	void removeLine(int index);
}
