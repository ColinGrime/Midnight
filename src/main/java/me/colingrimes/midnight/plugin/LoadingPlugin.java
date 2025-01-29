package me.colingrimes.midnight.plugin;

import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.listener.ArmorEquipListeners;
import me.colingrimes.midnight.listener.InventoryListener;
import me.colingrimes.midnight.listener.MenuListeners;
import me.colingrimes.midnight.util.Common;

/**
 * Represents the loading plugin that provides the Midnight library to other plugins.
 */
public class LoadingPlugin extends Midnight {

    @Override
    public void onEnable() {
        Common.register(this, new InventoryListener());
        Common.register(this, new ArmorEquipListeners());
        Common.register(this, new MenuListeners(this));
    }
}
