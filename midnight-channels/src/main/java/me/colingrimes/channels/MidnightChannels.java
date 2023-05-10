package me.colingrimes.channels;

import me.colingrimes.channels.listener.ChatListeners;
import me.colingrimes.channels.manager.ChannelManager;
import me.colingrimes.channels.storage.ChatStorage;
import me.colingrimes.channels.storage.ParticipantStorage;
import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.storage.sql.DatabaseCredentials;
import me.colingrimes.midnight.storage.sql.connection.ConnectionFactory;
import me.colingrimes.midnight.storage.sql.connection.ConnectionProvider;
import me.colingrimes.midnight.util.Common;
import me.colingrimes.midnight.util.bukkit.Players;
import me.colingrimes.midnight.util.io.Logger;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
import java.util.List;

public class MidnightChannels extends Midnight {

	private static MidnightChannels instance;
	private ChannelManager channelManager;
	private ParticipantStorage participantStorage;
	private ChatStorage chatStorage;

	@Override
	public void load() {
		instance = this;
		channelManager = new ChannelManager();
	}

	@Override
	protected void enable() {
		saveDefaultConfig();
		DatabaseCredentials credentials = DatabaseCredentials.fromConfig(getConfig().getConfigurationSection("database"));

		// Credentials must be set.
		if (credentials == null) {
			Logger.severe(this, "No database credentials found in config.yml. Disabling plugin.");
			Common.disable(this);
			return;
		}

		ConnectionFactory connectionFactory = new ConnectionFactory(this);
		ConnectionProvider connectionProvider = connectionFactory.createConnection(credentials);

		// Database connection must be successful.
		try {
			participantStorage = new ParticipantStorage(this, connectionProvider);
			chatStorage = new ChatStorage(this, connectionProvider);
			chatStorage.init();
		} catch (Exception e) {
			Logger.severe(this, "Failed to connect to database. Disabling plugin.");
			Common.disable(this);
			return;
		}

		Players.forEach(p -> {
			try {
				participantStorage.load(p.getUniqueId());
			} catch (Exception e) {
				Logger.severe("Failed to load participant: " + p.getUniqueId());
				e.printStackTrace();
			}
		});
	}

	@Override
	public void onDisable() {
		Players.forEach(p -> {
			try {
				participantStorage.save(channelManager.getParticipant(p));
			} catch (Exception e) {
				Logger.severe("Failed to save participant: " + p.getUniqueId());
				e.printStackTrace();
			}
		});

		chatStorage.shutdown();
		instance = null;
	}

	@Override
	protected void registerListeners(@Nonnull List<Listener> listeners) {
		listeners.add(new ChatListeners(this));
	}

	/**
	 * Gets the instance of the plugin.
	 *
	 * @return the instance
	 */
	@Nonnull
	public static MidnightChannels getInstance() {
		return instance;
	}

	/**
	 * Gets the channel manager.
	 *
	 * @return channel manager
	 */
	@Nonnull
	public ChannelManager getChannelManager() {
		return channelManager;
	}

	/**
	 * Gets the participant storage.
	 *
	 * @return the participant storage
	 */
	@Nonnull
	public ParticipantStorage getParticipantStorage() {
		return participantStorage;
	}

	/**
	 * Gets the chat storage.
	 *
	 * @return the chat storage
	 */
	@Nonnull
	public ChatStorage getChatStorage() {
		return chatStorage;
	}
}
