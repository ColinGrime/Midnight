package me.colingrimes.midnight.menu;

import com.google.common.base.Preconditions;
import me.colingrimes.midnight.scheduler.Scheduler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a {@link Gui} menu in the game.
 */
public interface Gui {

	/**
	 * Players currently viewing a {@link Gui} instance.
	 */
	Map<Player, Gui> players = new HashMap<>();

	/**
	 * Draws the {@link Gui} by placing all the items in the slots.
	 * This method is also called when {@link Gui#open()} is run.
	 */
	void draw();

	/**
	 * Gets the player viewing the {@link Gui}.
	 * @return the player
	 */
	@Nonnull
	Player getPlayer();

	/**
	 * Gets the inventory that the {@link Gui} is representing.
	 * @return the inventory
	 */
	@Nonnull
	Inventory getHandle();

	/**
	 * Gets the {@link Slot} that corresponds to the slot number.
	 * @param slot the slot number
	 * @return the slot object
	 */
	@Nonnull
	Slot getSlot(int slot);

	/**
	 * Gets whether the {@link Gui} is valid (opened).
	 * @return true if valid
	 */
	boolean isValid();

	/**
	 * Invalidates a {@link Gui}.
	 * This will prevent the menu from being interacted with further.
	 */
	void invalidate();

	/**
	 * Opens the {@link Gui}.
	 * To ensure it is properly loaded, there is a 1 tick delay in the opening.
	 */
	default void open() {
		Preconditions.checkArgument(isValid(), "Gui has already been opened.");
		draw();

		// Close any other menus the player has open.
		if (Gui.players.containsKey(getPlayer())) {
			Gui.players.get(getPlayer()).close();
		}

		// Delay the opening by 1 tick to ensure inventory is ready.
		Scheduler.SYNC.run(() -> {
			players.put(getPlayer(), this);
			getPlayer().openInventory(getHandle());
		});
	}

	/**
	 * Closes the {@link Gui}.
	 */
	default void close() {
		getPlayer().closeInventory();
	}
}
