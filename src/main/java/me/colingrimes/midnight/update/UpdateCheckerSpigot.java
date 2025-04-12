package me.colingrimes.midnight.update;

import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.util.io.Logger;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateCheckerSpigot implements UpdateChecker {

	private final Plugin plugin;
	private final String updateUrl;
	private final String resourceUrl;

	public UpdateCheckerSpigot(@Nonnull Plugin plugin, int resourceId) {
		this.plugin = plugin;
		this.updateUrl = "https://api.spigotmc.org/legacy/update.php?resource=" + resourceId;
		this.resourceUrl = "https://www.spigotmc.org/resources/" + resourceId;
		Scheduler.async().run(this::check);
	}

	@Override
	public void check() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(updateUrl).openStream()))) {
			String current = plugin.getDescription().getVersion();
			String latest = reader.readLine();
			if (!current.equals(latest)) {
				Logger.warn(plugin, "A new update is available: v" + latest + " (current: v" + current + ")");
				Logger.warn(plugin, "Download: " + resourceUrl);
			}
		} catch (IOException ignored) {
			// Silent failure is fine; logging isn't critical here.
		}
	}
}
