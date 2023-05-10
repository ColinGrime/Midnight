package me.colingrimes.midnight.command.handler.util;

import me.colingrimes.midnight.util.text.Parser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

/**
 * Represents a list of command arguments that extends {@link ArrayList} and provides
 * utility methods for retrieving and converting the arguments into various data types,
 * such as integers, doubles, booleans, and players.
 */
public class ArgumentList extends ArrayList<String> {

	/**
	 * Constructs a new {@link ArgumentList} with the given array of arguments.
	 *
	 * @param args the command arguments
	 */
	public ArgumentList(@Nonnull String[] args) {
		Collections.addAll(this, args);
	}

	/**
	 * Gets the argument at the specified index as an {@link Optional} of {@link String}.
	 *
	 * @param index the index of the argument to retrieve
	 * @return an {@link Optional} containing the argument,
	 * 		   or an empty {@link Optional} if the index is out of bounds
	 */
	@Nonnull
	public Optional<String> getOptional(int index) {
		if (isIndexOutOfBounds(index)) {
			return Optional.empty();
		}

		return Optional.of(this.get(index));
	}

	/**
	 * Gets the argument at the specified index in lowercase.
	 *
	 * @param index the index of the argument to retrieve
	 * @return the argument in lowercase
	 */
	@Nonnull
	public String getLowercase(int index) {
		return get(index).toLowerCase();
	}

	/**
	 * Gets the argument at the specified index as an {@link Optional} of {@link Integer}.
	 *
	 * @param index the index of the argument to retrieve
	 * @return an {@link Optional} containing the parsed integer,
	 * 		   or an empty {@link Optional} if the index is out of bounds
	 * 		   or the argument cannot be parsed as an integer
	 */
	@Nonnull
	public Optional<Integer> getInt(int index) {
		if (isIndexOutOfBounds(index)) {
			return Optional.empty();
		}

		try {
			return Optional.of(Integer.parseInt(this.get(index)));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}

	/**
	 * Gets the argument at the specified index as an integer,
	 * or returns the default value if the argument cannot be parsed as an integer.
	 *
	 * @param index        the index of the argument to retrieve
	 * @param defaultValue the default value to return if the argument cannot be parsed as an integer
	 * @return the parsed integer, or the default value
	 */
	public int getIntOrDefault(int index, int defaultValue) {
		return getInt(index).orElse(defaultValue);
	}

	/**
	 * Gets the argument at the specified index as an {@link Optional} of {@link Double}.
	 *
	 * @param index the index of the argument to retrieve
	 * @return an {@link Optional} containing the parsed double,
	 * 		   or an empty {@link Optional} if the index is out of bounds
	 * 		   or the argument cannot be parsed as a double
	 */
	@Nonnull
	public Optional<Double> getDouble(int index) {
		if (isIndexOutOfBounds(index)) {
			return Optional.empty();
		}

		try {
			return Optional.of(Double.parseDouble(this.get(index)));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}

	/**
	 * Gets the argument at the specified index as a double,
	 * or returns the default value if the argument cannot be parsed as a double.
	 *
	 * @param index        the index of the argument to retrieve
	 * @param defaultValue the default value to return if the argument cannot be parsed as a double
	 * @return the parsed double, or the default value
	 */
	public double getDoubleOrDefault(int index, double defaultValue) {
		return getDouble(index).orElse(defaultValue);
	}

	/**
	 * Gets the argument at the specified index as an {@link Optional} of {@link Boolean}.
	 *
	 * @param index the index of the argument to retrieve
	 * @return an {@link Optional} containing the parsed boolean,
	 * 		   or an empty {@link Optional} if the index is out of bounds
	 * 		   or the argument cannot be parsed as a boolean
	 */
	@Nonnull
	public Optional<Boolean> getBoolean(int index) {
		if (isIndexOutOfBounds(index)) {
			return Optional.empty();
		}

		String value = this.get(index).toLowerCase();
		if ("true".equals(value) || "false".equals(value)) {
			return Optional.of(Boolean.parseBoolean(value));
		}

		return Optional.empty();
	}

	/**
	 * Gets the argument at the specified index as a boolean,
	 * or returns the default value if the argument cannot be parsed as a boolean.
	 *
	 * @param index        the index of the argument to retrieve
	 * @param defaultValue the default value to return if the argument cannot be parsed as a boolean
	 * @return the parsed boolean, or the default value
	 */
	public boolean getBooleanOrDefault(int index, boolean defaultValue) {
		return getBoolean(index).orElse(defaultValue);
	}

	/**
	 * Gets the argument at the specified index as an {@link Optional} of {@link Player}.
	 *
	 * @param index the index of the argument to retrieve
	 * @return an {@link Optional} containing the player,
	 * 		   or an empty {@link Optional} if the index is out of bounds
	 * 		   or the player cannot be found
	 */
	@Nonnull
	public Optional<Player> getPlayer(int index) {
		if (isIndexOutOfBounds(index)) {
			return Optional.empty();
		}

		return Optional.ofNullable(Bukkit.getPlayer(this.get(index)));
	}

	/**
	 * Gets the argument at the specified index as an {@link Optional} of {@link Duration}.
	 *
	 * @param index the index of the argument to retrieve
	 * @return an {@link Optional} containing the parsed duration,
	 * 		   or an empty {@link Optional} if the index is out of bounds
	 * 		   or the argument cannot be parsed as a duration
	 */
	@Nonnull
	public Optional<Duration> getDuration(int index) {
		if (isIndexOutOfBounds(index)) {
			return Optional.empty();
		}

		return Optional.ofNullable(Parser.parseDuration(this.get(index)));
	}

	/**
	 * Checks if the index is out of bounds.
	 *
	 * @param index the index to check
	 * @return true if the index is out of bounds, false otherwise
	 */
	private boolean isIndexOutOfBounds(int index) {
		return index < 0 || index >= this.size();
	}
}