package me.colingrimes.midnight.menu.gui;

import com.google.common.base.Preconditions;
import me.colingrimes.midnight.menu.Gui;
import me.colingrimes.midnight.menu.Slot;
import me.colingrimes.midnight.menu.slot.SimpleSlot;
import me.colingrimes.midnight.util.text.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * The basic {@link Gui} abstraction class.
 * Extend this class if you want basic menu functionalities.
 */
public abstract class AbstractGui implements Gui {

	private final Player player;
	private final Inventory inventory;
	private final Map<Integer, Slot> slots = new HashMap<>();
	private boolean valid = false;

	public AbstractGui(@Nonnull Player player, @Nonnull String title, int rows) {
		this.player = player;
		this.inventory = Bukkit.createInventory(player, rows * 9, Text.color(title));
	}

	@Nonnull
	@Override
	public Player getPlayer() {
		return player;
	}

	@Nonnull
	@Override
	public Inventory getHandle() {
		return inventory;
	}

	@Nonnull
	@Override
	public Slot getSlot(int slot) {
		Preconditions.checkArgument(slot < 0 || slot >= inventory.getSize(), "Invalid slot: " + slot);
		return slots.computeIfAbsent(slot, i -> new SimpleSlot(this, i));
	}

	@Override
	public boolean isValid() {
		return valid;
	}

	@Override
	public void invalidate() {
		valid = false;
		Gui.players.remove(player);

		// Wipe out the inventory to ensure it cannot be interacted with further.
		inventory.clear();
		slots.values().forEach(Slot::clearBindings);
	}

	/**
	 * Invalidates a valid {@link Gui} if the owner is the player.
	 * @param player the player
	 */
	private void invalidate(@Nonnull Player player) {
		if (player.equals(this.player) && valid) {
			invalidate();
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked().equals(player)) {
			event.setCancelled(true);

			// Close if invalid.
			if (!valid) {
				close();
				return;
			}

			// Handle the click if an action exists.
			Slot slot = slots.get(event.getRawSlot());
			if (slot != null) {
				slot.handle(event);
			}
		}
	}

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent event) {
		// Ignore drag event for custom inventories.
		if (event.getWhoClicked().equals(player)) {
			event.setCancelled(true);

			// Close if invalid.
			if (!valid) {
				close();
			}
		}
	}

	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event) {
		// Invalidate if a new inventory is opened.
		if (!event.getInventory().equals(inventory)) {
			invalidate((Player) event.getPlayer());
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		invalidate((Player) event.getPlayer());
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		invalidate(event.getEntity());
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		invalidate(event.getPlayer());
	}

	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		invalidate(event.getPlayer());
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		invalidate(event.getPlayer());
	}
}
