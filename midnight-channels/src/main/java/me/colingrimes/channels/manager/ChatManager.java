package me.colingrimes.channels.manager;

import me.colingrimes.channels.channel.chatter.Chatter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;

public class ChatManager {

	private final Map<UUID, Chatter> chatters = new HashMap<>();

	/**
	 * Registers a chatter with the manager.
	 *
	 * @param chatter the chatter to add
	 */
	public void registerChatter(@Nonnull Chatter chatter) {
		if (!chatters.containsKey(chatter.getID())) {
			chatters.put(chatter.getID(), chatter);
		}
	}

	/**
	 * Gets an immutable set of all chatters.
	 *
	 * @return a set of all chatters
	 */
	@Nonnull
	public Set<Chatter> getChatters() {
		return Set.copyOf(chatters.values());
	}

	/**
	 * Gets a chatter by their UUID.
	 *
	 * @param uuid the UUID of the chatter
	 * @return an Optional containing the chatter if found, otherwise empty
	 */
	@Nonnull
	public Optional<Chatter> getChatter(@Nonnull UUID uuid) {
		return Optional.ofNullable(chatters.get(uuid));
	}

	/**
	 * Gets a chatter by their player.
	 *
	 * @param player the player of the chatter
	 * @return the chatter
	 */
	@Nonnull
	public Optional<Chatter> getChatter(@Nonnull Player player) {
		return getChatter(player.getUniqueId());
	}
}
