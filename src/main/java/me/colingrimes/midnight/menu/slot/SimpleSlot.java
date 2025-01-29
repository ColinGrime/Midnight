package me.colingrimes.midnight.menu.slot;

import me.colingrimes.midnight.menu.Gui;
import me.colingrimes.midnight.menu.Slot;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class SimpleSlot implements Slot {

	private final Gui gui;
	private final int id;
	private final Map<ClickType, Set<Consumer<InventoryClickEvent>>> handlers;

	public SimpleSlot(@Nonnull Gui gui, int id) {
		this.gui = gui;
		this.id = id;
		this.handlers = new EnumMap<>(ClickType.class);
	}

	@Override
	public void handle(@Nonnull InventoryClickEvent event) {
		Objects.requireNonNull(event, "InventoryClickEvent is null.");

		// Get all the handlers of the click type.
		Set<Consumer<InventoryClickEvent>> handlers = this.handlers.get(event.getClick());
		if (handlers == null) {
			return;
		}

		// Performs all the handlers of the click type.
		handlers.forEach(h -> h.accept(event));
	}

	@Nonnull
	@Override
	public Gui getGui() {
		return gui;
	}

	@Override
	public int getId() {
		return id;
	}

	@Nullable
	@Override
	public ItemStack getItem() {
		return gui.getHandle().getItem(id);
	}

	@Nonnull
	@Override
	public Slot setItem(@Nonnull ItemStack item) {
		this.gui.getHandle().setItem(id, Objects.requireNonNull(item, "Item is null."));
		return this;
	}

	@Nonnull
	@Override
	public Slot bind(@Nonnull ClickType type, @Nonnull Consumer<InventoryClickEvent> handler) {
		Objects.requireNonNull(type, "Type is null.");
		Objects.requireNonNull(handler, "Handler is null.");

		// Bind the handler to the click type.
		handlers.computeIfAbsent(type, c -> new HashSet<>()).add(handler);
		return this;
	}

	@Override
	public void clearBindings() {
		handlers.clear();
	}
}
