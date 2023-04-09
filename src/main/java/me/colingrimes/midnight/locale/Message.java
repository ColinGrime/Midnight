package me.colingrimes.midnight.locale;

import me.colingrimes.midnight.plugin.Midnight;
import me.colingrimes.midnight.util.Logger;
import me.colingrimes.midnight.util.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Lists the various messages of the plugin.
 */
public enum Message {

	// messages
	;

	private static File file;
	private static FileConfiguration config;

	private final String path;
	private List<String> msg;

	Message(@Nonnull String path, @Nonnull String...msg) {
		this.path = path;
		this.msg = Arrays.asList(msg);
	}

	/**
	 * Sends the message to the specified sender.
	 * @param sender the sender to send the message to
	 */
	public void sendTo(@Nonnull CommandSender sender) {
		sendTo(sender, null);
	}

	/**
	 * Sends the message, with replaced placeholders, to the specified sender
	 * @param sender the sender to send the message to
	 * @param placeholders the placeholders to search for
	 */
	public void sendTo(@Nonnull CommandSender sender, @Nullable Placeholders placeholders) {
		Objects.requireNonNull(sender, "Sender is null.");
		placeholders = Objects.requireNonNullElse(placeholders, new Placeholders());
		placeholders.replace(msg).forEach(sender::sendMessage);
	}

	public String getPath() {
		return path;
	}

	public List<String> getMsg() {
		return msg;
	}

	@Override
	public String toString() {
		return String.join("\n", msg);
	}

	/**
	 * Initializes the messages' configuration file.
	 * @param plugin the plugin
	 */
	public static void init(@Nonnull Midnight plugin) {
		file = new File(plugin.getDataFolder(), "messages.yml");
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			plugin.saveResource("messages.yml", false);
		}
	}

	/**
	 * Reloads the various messages of the plugin.
	 */
	public static void reload() {
		config = YamlConfiguration.loadConfiguration(file);
		Arrays.stream(Message.values()).forEach(Message::update);
	}

	/**
	 * Updates the message with whatever value is in the {@link FileConfiguration}.
	 * If the message is not found, it will use the default one given.
	 */
	private void update() {
		if (!config.getStringList(path).isEmpty()) {
			msg = Text.color(config.getStringList(path));
		} else if (config.getString(path) != null) {
			msg = Collections.singletonList(Text.color(config.getString(path)));
		} else {
			Logger.warn("Messages path \"" + path + "\" has failed to load (using default value).");
			msg = Text.color(msg);
		}
	}
}