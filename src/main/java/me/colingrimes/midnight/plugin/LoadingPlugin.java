package me.colingrimes.midnight.plugin;

import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.listener.ArmorEquipListeners;
import me.colingrimes.midnight.listener.InventoryListener;
import me.colingrimes.midnight.listener.MenuListeners;
import me.colingrimes.midnight.util.Common;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.AdvancedPie;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the loading plugin that provides the Midnight library to other plugins.
 */
public class LoadingPlugin extends Midnight {

    @Override
    public void onEnable() {
        Common.register(this, new InventoryListener());
        Common.register(this, new ArmorEquipListeners());
        Common.register(this, new MenuListeners(this));
        setupMetrics();
    }

    /**
     * Sets up the Metrics for the library.
     */
    private void setupMetrics() {
        Metrics metrics = new Metrics(this, 25246);
        metrics.addCustomChart(new AdvancedPie("midnight_plugins", () -> {
            Map<String, Integer> map = new HashMap<>();
            for (Midnight plugin : MidnightPlugin.getAll()) {
                if (!plugin.equals(this)) {
                    map.put(plugin.getName(), 1);
                }
            }
            return map;
        }));
    }
}
