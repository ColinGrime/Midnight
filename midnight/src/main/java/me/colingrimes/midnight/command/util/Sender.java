package me.colingrimes.midnight.command.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * A convenience wrapper around {@link CommandSender} that provides utility methods
 * for common operations related to command senders in a more streamlined manner.
 */
public class Sender {

	private final CommandSender sender;

	public Sender(@Nonnull CommandSender sender) {
		this.sender = sender;
	}

	/**
	 * Sends a message to the sender.
	 * @param message the message to send to the sender
	 */
	public void message(@Nonnull String message) {
		sender.sendMessage(message);
	}

	/**
	 * Checks if the sender is a player.
	 * @return true if the sender is a player
	 */
	public boolean isPlayer() {
		return sender instanceof Player;
	}

	/**
	 * Gets the player instance of the sender.
	 * @return the player instance of the sender
	 */
	@Nonnull
	public Player player() {
		if (!(sender instanceof Player player)) {
			throw new IllegalStateException("Command sender must be a player.");
		}
		return player;
	}

	/**
	 * Gets the location of the sender.
	 * @return the location of the sender
	 */
	@Nonnull
	public Location location() {
		return player().getLocation();
	}

	/**
	 * Gets the world of the sender.
	 * @return the world of the sender
	 */
	@Nonnull
	public World world() {
		return Objects.requireNonNull(location().getWorld(), "World is null.");
	}

	/**
	 * Gets the x coordinate of the sender.
	 * @return the x coordinate of the sender
	 */
	public double x() {
		return location().getX();
	}

	/**
	 * Gets the y coordinate of the sender.
	 * @return the y coordinate of the sender
	 */
	public double y() {
		return location().getY();
	}

	/**
	 * Gets the z coordinate of the sender.
	 * @return the z coordinate of the sender
	 */
	public double z() {
		return location().getZ();
	}
}
