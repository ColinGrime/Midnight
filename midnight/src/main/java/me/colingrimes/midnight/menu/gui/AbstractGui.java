package me.colingrimes.midnight.menu.gui;

import com.google.common.base.Preconditions;
import me.colingrimes.midnight.menu.Gui;
import me.colingrimes.midnight.menu.Slot;
import me.colingrimes.midnight.menu.slot.SimpleSlot;
import me.colingrimes.midnight.util.text.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
}
