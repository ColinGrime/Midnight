package me.colingrimes.midnight.listener;

import me.colingrimes.midnight.plugin.Midnight;
import me.colingrimes.midnight.util.Common;
import org.bukkit.event.Listener;

class AbstractListener implements Listener {

	final Midnight plugin;

	AbstractListener(Midnight plugin) {
		this.plugin = plugin;
		Common.registerEvents(this);
	}
}