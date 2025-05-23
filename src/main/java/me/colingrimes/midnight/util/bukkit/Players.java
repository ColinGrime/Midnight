package me.colingrimes.midnight.util.bukkit;

import me.colingrimes.midnight.util.Common;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
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
		return Optional.ofNullable(Common.server().getPlayerExact(name));
	}

	/**
	 * Returns the offline player with the given UUID.
	 *
	 * @param uuid the UUID of the player
	 * @return the offline player
	 */
	@Nonnull
	public static OfflinePlayer getOffline(@Nonnull UUID uuid) {
		return Common.server().getOfflinePlayer(uuid);
	}

	/**
	 * Returns the player's name with the given UUID.
	 * <p>
	 * If no name is found, it will return "Unknown".
	 *
	 * @param uuid the UUID of the player
	 * @return the player name or "Unknown" if not found
	 */
	@Nonnull
	public static String getName(@Nonnull UUID uuid) {
		OfflinePlayer player = getOffline(uuid);
		return player.getName() != null ? player.getName() : "Unknown";
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
		command = command.contains("/") ? command.substring(command.indexOf("/") + 1) : command;
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

	private Players() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
