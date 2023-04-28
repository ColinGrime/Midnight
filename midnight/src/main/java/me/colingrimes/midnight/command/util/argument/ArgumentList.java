package me.colingrimes.midnight.command.util.argument;

import me.colingrimes.midnight.util.text.Parser;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

/**
 * Represents a list of command arguments with functionality to convert the arguments
 * to various data types, such as integers, doubles, booleans, and players.
 * Implements the {@link TypeConverter} interface for type conversion and extends {@link ArrayList}.
 */
public class ArgumentList extends ArrayList<String> implements TypeConverter {

	public ArgumentList(@Nonnull String[] args) {
		Collections.addAll(this, args);
	}

	@Nonnull
	@Override
	public Optional<String> getOptional(int index) {
		if (isIndexOutOfBounds(index)) {
			return Optional.empty();
		}

		return Optional.of(this.get(index));
	}

	@Nonnull
	@Override
	public String getLowercase(int index) {
		return get(index).toLowerCase();
	}

	@Nonnull
	@Override
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

	@Override
	public int getIntOrDefault(int index, int defaultValue) {
		return getInt(index).orElse(defaultValue);
	}

	@Nonnull
	@Override
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

	@Override
	public double getDoubleOrDefault(int index, double defaultValue) {
		return getDouble(index).orElse(defaultValue);
	}

	@Nonnull
	@Override
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

	@Override
	public boolean getBooleanOrDefault(int index, boolean defaultValue) {
		return getBoolean(index).orElse(defaultValue);
	}

	@Nonnull
	@Override
	public Optional<Player> getPlayer(int index) {
		if (isIndexOutOfBounds(index)) {
			return Optional.empty();
		}

		return Optional.ofNullable(Bukkit.getPlayer(this.get(index)));
	}

	@Nonnull
	@Override
	public Optional<Duration> getDuration(int index) {
		if (isIndexOutOfBounds(index)) {
			return Optional.empty();
		}

		return Optional.ofNullable(Parser.parseDuration(this.get(index)));
	}

	/**
	 * Checks if the index is out of bounds.
	 * @param index index to check
	 * @return true if the index is out of bounds
	 */
	private boolean isIndexOutOfBounds(int index) {
		return index < 0 || index >= this.size();
	}
}
