package me.colingrimes.plugin.listener;

import me.colingrimes.midnight.display.Display;
import me.colingrimes.midnight.display.type.DisplayType;
import me.colingrimes.midnight.event.DisplayHideEvent;
import me.colingrimes.midnight.event.DisplayShowEvent;
import me.colingrimes.plugin.MidnightPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
import java.util.Optional;

public class DisplayListeners implements Listener {

    private final MidnightPlugin plugin;

    public DisplayListeners(@Nonnull MidnightPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDisplayShow(@Nonnull DisplayShowEvent event) {
        Player player = event.getPlayer();
        Optional<Display> display = plugin.getDisplayManager().get(player, DisplayType.ACTION_BAR);

        // Check if there is already an active display.
        if (display.isPresent() && event.getDisplay().getPriority() < display.get().getPriority()) {
            event.setCancelled(true);
            return;
        }

        display.ifPresent(d -> d.hide(player));
        plugin.getDisplayManager().set(player, event.getDisplay());
    }

    @EventHandler
    public void onDisplayHide(@Nonnull DisplayHideEvent event) {
        plugin.getDisplayManager().remove(event.getPlayer(), event.getType());
    }
}
