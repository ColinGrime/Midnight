package me.colingrimes.midnight.util.bukkit;

import me.colingrimes.midnight.util.Common;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Players {

	/**
	 * Returns the player with the given UUID.
	 *
	 * @param uuid the UUID of the player
	 * @return the player
	 */
	@Nonnull
	public static Optional<Player> get(@Nonnull UUID uuid) {
		return Optional.ofNullable(Common.server().getPlayer(uuid));
	}

	/**
	 * Returns the player with the given name.
	 *
	 * @param name the name of the player
	 * @return the player
	 */
	@Nonnull
	public static Optional<Player> get(@Nonnull String name) {
		return Optional.ofNullable(Common.server().getPlayer(name));
	}

	/**
	 * Returns a collection of all online players.
	 *
	 * @return all online players
	 */
	@Nonnull
	public static Collection<? extends Player> all() {
		return Common.server().getOnlinePlayers();
	}

	/**
	 * Performs the given action for each online player.
	 *
	 * @param action the action to perform
	 */
	public static void forEach(@Nonnull Consumer<? super Player> action) {
		all().forEach(action);
	}

	/**
	 * Returns a stream of all online players, filtered by the given predicate.
	 *
	 * @param predicate the predicate
	 * @return the stream of players
	 */
	@Nonnull
	public static Stream<? extends Player> filter(@Nonnull Predicate<? super Player> predicate) {
		return all().stream().filter(predicate);
	}

	/**
	 * Returns a stream of results obtained by applying the given function to each online player.
	 *
	 * @param function the function to apply to each player
	 * @return the stream of results
	 */
	@Nonnull
	public static <R> Stream<R> map(@Nonnull Function<? super Player, ? extends R> function) {
		return all().stream().map(function);
	}

	/**
	 * Applies the given function to each online player and collects the results into a list.
	 *
	 * @param function the function to apply to each player
	 * @return the list of results
	 */
	@Nonnull
	public static <R> List<R> mapList(@Nonnull Function<? super Player, ? extends R> function) {
		return map(function).collect(Collectors.toList());
	}

	/**
	 * Performs the command as the given player.
	 *
	 * @param player  the player
	 * @param command the command
	 */
	public static void command(@Nonnull Player player, @Nonnull String command) {
		command = command.startsWith("/") ? command.substring(1) : command;
		player.performCommand(command);
	}

	/**
	 * Plays the given sound to the given player.
	 *
	 * @param player the player
	 * @param sound  the sound
	 */
	public static void sound(@Nonnull Player player, @Nonnull Sound sound) {
		player.playSound(player.getLocation(), sound, 1F, 1F);
	}

	/**
	 * Finds the closest player to the given location within 100 blocks.
	 *
	 * @param location the location
	 * @return the closest player
	 */
	@Nonnull
	public static Optional<Player> find(@Nullable Location location) {
		return find(location, 100);
	}

	/**
	 * Finds the closest player to the given location within the given number of blocks.
	 *
	 * @param location the location
	 * @param blocks   the number of blocks
	 * @return the closest player
	 */
	@Nonnull
	public static Optional<Player> find(@Nullable Location location, int blocks) {
		return Locations.findClosest(Player.class, location, blocks);
	}

	private Players() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
