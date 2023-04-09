package me.colingrimes.midnight.menu.gui.paginated;

import me.colingrimes.midnight.menu.gui.AbstractGui;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class PaginatedGui extends AbstractGui {

	public PaginatedGui(@Nonnull Player player, @Nonnull String title, int rows) {
		super(player, title, rows);
	}

	@Override
	public void draw() {

	}
}
