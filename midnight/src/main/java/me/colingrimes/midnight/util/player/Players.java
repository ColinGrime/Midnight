package me.colingrimes.midnight.util.player;

import me.colingrimes.midnight.util.Common;
import me.colingrimes.midnight.util.Locations;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Sound;
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

	/**
	 * Plays the given sound to the given player.
	 * @param player the player
	 * @param sound the sound
	 */
	public static void playSound(@Nonnull Player player, @Nonnull Sound sound) {
		player.playSound(player.getLocation(), sound, 1F, 1F);
	}

	/**
	 * Sends the given message as an action bar to the given player.
	 * @param player the player
	 * @param message the message
	 */
	public static void sendActionBar(@Nonnull Player player, @Nonnull String message) {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
	}

	/**
	 * Finds the closest player to the given location within 100 blocks.
	 * @param location the location
	 * @return the closest player
	 */
	@Nonnull
	public static Optional<Player> findClosest(@Nullable Location location) {
		return findClosest(location, 100);
	}

	/**
	 * Finds the closest player to the given location within the given number of blocks.
	 * @param location the location
	 * @param blocks the number of blocks
	 * @return the closest player
	 */
	@Nonnull
	public static Optional<Player> findClosest(@Nullable Location location, int blocks) {
		return Locations.findClosest(Player.class, location, blocks);
	}

	private Players() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
