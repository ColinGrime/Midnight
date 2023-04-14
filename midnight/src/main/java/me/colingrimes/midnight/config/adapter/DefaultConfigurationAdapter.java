package me.colingrimes.midnight.config.adapter;

import me.colingrimes.midnight.plugin.MidnightPlugin;

import javax.annotation.Nonnull;

/**
 * Configuration adapter for default config.yml files.
 */
public class DefaultConfigurationAdapter extends BaseConfigurationAdapter {

	private final MidnightPlugin plugin;

	public DefaultConfigurationAdapter(@Nonnull MidnightPlugin plugin) {
		this.plugin = plugin;
		this.plugin.saveDefaultConfig();
	}

	@Override
	public void reload() {
		plugin.reloadConfig();
		config = plugin.getConfig();
	}
}
