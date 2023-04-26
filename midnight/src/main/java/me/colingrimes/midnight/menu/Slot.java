package me.colingrimes.midnight.menu;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Represents a single slot in a {@link Gui}.
 */
public interface Slot {

	/**
	 * Handles all click events on the slot.
	 * @param event inventory click event
	 */
	void handle(@Nonnull InventoryClickEvent event);

	/**
	 * Gets the {@link Gui} of the slot.
	 * @return the gui of the slot
	 */
	@Nonnull
	Gui getGui();

	/**
	 * Gets the ID number of the slot.
	 *
	 * <p>
	 * This is the slot number that corresponds to its
	 * placement in the {@link Gui} that it is associated with.
	 * </p>
	 *
	 * @return the id of the slot
	 */
	int getId();

	/**
	 * Gets the item of the slot.
	 * @return the item of the slot
	 */
	@Nullable
	ItemStack getItem();

	/**
	 * Sets the item of the slot.
	 * @param item the item of the slot
	 * @return the slot object
	 */
	@Nonnull
	Slot setItem(@Nonnull ItemStack item);

	/**
	 * Binds a click type to a handler that will perform an action on click.
	 * @param type the click type
	 * @param handler the action to perform on the click
	 * @return the slot object
	 */
	@Nonnull
	Slot bind(@Nonnull ClickType type, @Nonnull Consumer<InventoryClickEvent> handler);

	/**
	 * Binds a click type to a handler that will perform an action on click.
	 * @param handler the action to perform on the click
	 * @param types the click types
	 * @return the slot object
	 */
	@Nonnull
	Slot bind(@Nonnull Consumer<InventoryClickEvent> handler, @Nonnull ClickType... types);

	/**
	 * Clears all bindings of all click types.
	 */
	void clearBindings();
}
