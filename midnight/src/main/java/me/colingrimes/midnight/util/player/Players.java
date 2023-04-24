package me.colingrimes.midnight.util.player;

import me.colingrimes.midnight.util.Common;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public final class Players {

	/**
	 * Returns the player with the given UUID.
	 * @param uuid the UUID of the player
	 * @return the player
	 */
	@Nullable
	public static Player getNullable(@Nonnull UUID uuid) {
		return Common.server().getPlayer(uuid);
	}

	/**
	 * Returns the player with the given UUID.
	 * @param uuid the UUID of the player
	 * @return the player
	 */
	@Nonnull
	public static Optional<Player> get(@Nonnull UUID uuid) {
		return Optional.ofNullable(getNullable(uuid));
	}

	/**
	 * Returns the player with the given name.
	 * @param name the name of the player
	 * @return the player
	 */
	@Nullable
	public static Player getNullable(@Nonnull String name) {
		return Common.server().getPlayer(name);
	}

	/**
	 * Returns the player with the given name.
	 * @param name the name of the player
	 * @return the player
	 */
	@Nonnull
	public static Optional<Player> get(@Nonnull String name) {
		return Optional.ofNullable(Common.server().getPlayer(name));
	}

	/**
	 * Returns a collection of all online players.
	 * @return all online players
	 */
	@Nonnull
	public static Collection<? extends Player> all() {
		return Common.server().getOnlinePlayers();
	}

	/**
	 * Performs the given action for each online player.
	 * @param action the action to perform
	 */
	public static void forEach(@Nonnull Consumer<? super Player> action) {
		all().forEach(action);
	}

	private Players() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
