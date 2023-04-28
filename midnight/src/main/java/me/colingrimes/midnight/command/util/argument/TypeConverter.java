package me.colingrimes.midnight.command.util.argument;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Optional;

/**
 * Represents an interface for converting command arguments to various data types.
 * Provides methods to convert arguments to integers, doubles, booleans, and players,
 * as well as returning default values when the conversion is not possible.
 */
public interface TypeConverter {

	/**
	 * Gets the optional version of the argument at the specified index.
	 * @param index index of the argument
	 * @return the argument in an optional
	 */
	@Nonnull
	Optional<String> getOptional(int index);

	/**
	 * Gets the lowercase version of the argument at the specified index.
	 * @param index index of the argument
	 * @return the argument in lowercase
	 */
	@Nonnull
	String getLowercase(int index);

	/**
	 * Gets the integer argument at the specified index.
	 * @param index index of the argument
	 * @return the argument as an integer
	 */
	@Nonnull
	Optional<Integer> getInt(int index);

	/**
	 * Gets the integer argument at the specified index.
	 * @param index index of the argument
	 * @param defaultValue default value to return if the argument is not an integer
	 * @return the argument as an integer
	 */
	int getIntOrDefault(int index, int defaultValue);

	/**
	 * Gets the double argument at the specified index.
	 * @param index index of the argument
	 * @return the argument as a double
	 */
	@Nonnull
	Optional<Double> getDouble(int index);

	/**
	 * Gets the double argument at the specified index.
	 * @param index index of the argument
	 * @param defaultValue default value to return if the argument is not a double
	 * @return the argument as a double
	 */
	double getDoubleOrDefault(int index, double defaultValue);

	/**
	 * Gets the boolean argument at the specified index.
	 * @param index index of the argument
	 * @return the argument as a boolean
	 */
	@Nonnull
	Optional<Boolean> getBoolean(int index);

	/**
	 * Gets the boolean argument at the specified index.
	 * @param index index of the argument
	 * @param defaultValue default value to return if the argument is not a boolean
	 * @return the argument as a boolean
	 */
	boolean getBooleanOrDefault(int index, boolean defaultValue);

	/**
	 * Gets the player argument at the specified index.
	 * @param index index of the argument
	 * @return the argument as a player
	 */
	@Nonnull
	Optional<Player> getPlayer(int index);

	/**
	 * Gets the duration argument at the specified index.
	 * @param index index of the argument
	 * @return the argument as a duration
	 */
	@Nonnull
	Optional<Duration> getDuration(int index);
}
