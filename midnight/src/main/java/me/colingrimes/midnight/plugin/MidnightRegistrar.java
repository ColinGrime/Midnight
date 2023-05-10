package me.colingrimes.midnight.plugin;

import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.listener.ArmorEquipListeners;
import me.colingrimes.midnight.listener.InventoryListener;
import me.colingrimes.midnight.listener.MenuListeners;
import me.colingrimes.midnight.util.Common;

import javax.annotation.Nonnull;

public final class MidnightRegistrar {

	/**
	 * Registers everything that needs to be registered.
	 *
	 * @param plugin the plugin
	 */
	public static void register(@Nonnull Midnight plugin) {
		Common.register(plugin, new InventoryListener());
		Common.register(plugin, new ArmorEquipListeners());
		Common.register(plugin, new MenuListeners(plugin));
	}

	private MidnightRegistrar() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
