package me.colingrimes.channels;

import me.colingrimes.channels.listener.ChatListeners;
import me.colingrimes.channels.manager.ChatManager;
import me.colingrimes.channels.storage.ChatLogStorage;
import me.colingrimes.channels.storage.ChatterStorage;
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
	private ChatManager channelManager;
	private ChatterStorage chatterStorage;
	private ChatLogStorage chatLogStorage;

	@Override
	public void load() {
		instance = this;
		channelManager = new ChatManager();
	}

	@Override
	protected void enable() {
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
			chatterStorage = new ChatterStorage(this, connectionProvider);
			chatLogStorage = new ChatLogStorage(this, connectionProvider);
			chatLogStorage.init();
		} catch (Exception e) {
			Logger.severe(this, "Failed to connect to database. Disabling plugin.");
			Common.disable(this);
		}

		Players.forEach(p -> {
			try {
				chatterStorage.load(p.getUniqueId());
			} catch (Exception e) {
				Logger.severe("Failed to load chatter: " + p.getUniqueId());
				e.printStackTrace();
			}
		});
	}

	@Override
	public void onDisable() {
		Players.forEach(p -> {
			try {
				chatterStorage.save(channelManager.getChatter(p));
			} catch (Exception e) {
				Logger.severe("Failed to save chatter: " + p.getUniqueId());
				e.printStackTrace();
			}
		});

		chatLogStorage.shutdown();
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
	public ChatManager getChannelManager() {
		return channelManager;
	}

	/**
	 * Gets the chatter storage.
	 *
	 * @return the chatter storage
	 */
	@Nonnull
	public ChatterStorage getChatterStorage() {
		return chatterStorage;
	}

	/**
	 * Gets the chat log storage.
	 *
	 * @return the chat storage
	 */
	@Nonnull
	public ChatLogStorage getChatLogStorage() {
		return chatLogStorage;
	}
}
