package me.colingrimes.plugin.listener;

import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.menu.Gui;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.PluginDisableEvent;

import javax.annotation.Nonnull;

public class MenuListeners implements Listener {

    private final Midnight plugin;

    public MenuListeners(@Nonnull Midnight plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(@Nonnull InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Gui gui = Gui.players.get(player);

        if (gui != null) {
            event.setCancelled(true);

            if (!gui.isValid()) {
                gui.close();
                return;
            }

            gui.getSlot(event.getRawSlot()).handle(event);
        }
    }

    @EventHandler
    public void onInventoryDrag(@Nonnull InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        Gui gui = Gui.players.get(player);

        if (gui != null) {
            event.setCancelled(true);

            if (!gui.isValid()) {
                gui.close();
            }
        }
    }

    @EventHandler
    public void onInventoryClose(@Nonnull InventoryCloseEvent event) {
        invalidate(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(@Nonnull PlayerDeathEvent event) {
        invalidate(event.getEntity());
    }

    @EventHandler
    public void onPlayerQuit(@Nonnull PlayerQuitEvent event) {
        invalidate(event.getPlayer());
    }

    @EventHandler
    public void onPlayerChangedWorld(@Nonnull PlayerChangedWorldEvent event) {
        invalidate(event.getPlayer());
    }

    @EventHandler
    public void onPlayerTeleport(@Nonnull PlayerTeleportEvent event) {
        invalidate(event.getPlayer());
    }

    @EventHandler
    public void onPluginDisable(@Nonnull PluginDisableEvent event) {
        if (event.getPlugin().equals(plugin)) {
            Gui.players.values().forEach(Gui::invalidate);
        }
    }

    /**
     * Invalidates a valid {@link Gui}.
     * @param humanEntity the entity
     */
    private void invalidate(@Nonnull HumanEntity humanEntity) {
        if (!(humanEntity instanceof Player player)) {
            return;
        }

        Gui gui = Gui.players.get(player);
        if (gui != null) {
            gui.invalidate();
        }
    }
}
