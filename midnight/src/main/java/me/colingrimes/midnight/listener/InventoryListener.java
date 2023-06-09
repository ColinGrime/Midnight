package me.colingrimes.midnight.listener;

import me.colingrimes.midnight.event.SimpleClickEvent;
import me.colingrimes.midnight.util.Common;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import javax.annotation.Nonnull;

public class InventoryListener implements Listener {

	@EventHandler
	public void onInventoryClick(@Nonnull InventoryClickEvent event) {
		Common.call(new SimpleClickEvent(event));
	}
}
