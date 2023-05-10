package me.colingrimes.channels.manager;

import me.colingrimes.channels.channel.Channel;
import me.colingrimes.channels.channel.Participant;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Manages the various communication channels.
 */
public class ChannelManager {

	private final Map<String, Channel> channels = new HashMap<>();
	private final Map<UUID, Participant> participants = new HashMap<>();

	/**
	 * Registers a channel with the manager.
	 *
	 * @param channel the channel to add
	 * @throws IllegalStateException if the channel is already registered
	 */
	public void registerChannel(@Nonnull Channel channel) {
		if (!channels.containsKey(channel.getName())) {
			channels.put(channel.getName(), channel);
		} else {
			throw new IllegalStateException("Channel already registered: " + channel.getName());
		}
	}

	/**
	 * Registers a participant with the manager.
	 *
	 * @param participant the participant to add
	 */
	public void registerParticipant(@Nonnull Participant participant) {
		if (!participants.containsKey(participant.getID())) {
			participants.put(participant.getID(), participant);
		}
	}

	/**
	 * Gets a channel by its name.
	 *
	 * @param channelName the name of the channel
	 * @return an Optional containing the channel if found, otherwise empty
	 */
	@Nonnull
	public Optional<Channel> getChannel(@Nonnull String channelName) {
		return Optional.ofNullable(channels.get(channelName));
	}

	/**
	 * Gets a participant by their player.
	 *
	 * @param player the player of the participant
	 * @return the participant
	 * @throws IllegalStateException if the player is not loaded
	 */
	@Nonnull
	public Participant getParticipant(@Nonnull Player player) {
		return getParticipant(player.getUniqueId()).orElseThrow(() -> new IllegalStateException("Player not loaded: " + player.getUniqueId()));
	}

	/**
	 * Gets a participant by their UUID.
	 *
	 * @param uuid the UUID of the participant
	 * @return an Optional containing the participant if found, otherwise empty
	 */
	@Nonnull
	public Optional<Participant> getParticipant(@Nonnull UUID uuid) {
		return Optional.ofNullable(participants.get(uuid));
	}
}
