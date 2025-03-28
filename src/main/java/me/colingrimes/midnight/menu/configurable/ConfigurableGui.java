package me.colingrimes.midnight.menu.configurable;

import me.colingrimes.midnight.config.util.ConfigurableInventory;
import me.colingrimes.midnight.menu.Gui;
import me.colingrimes.midnight.util.bukkit.Players;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import javax.annotation.Nonnull;

public class ConfigurableGui extends Gui {

	private final ConfigurableInventory config;

	public ConfigurableGui(@Nonnull Player player, @Nonnull ConfigurableInventory config) {
		super(player, config.getTitle(), config.getRows());
		this.config = config;
	}

	@Override
	public void draw() {
		config.getItems().forEach((i, item) -> {
			getSlot(i).setItem(item);

			// Set the command of the slot if applicable.
			String command = config.getCommand(i);
			if (command != null && !command.isEmpty()) {
				getSlot(i).bind(ClickType.LEFT, e -> Players.command(getPlayer(), command));
			}
		});
	}
}
