package me.colingrimes.example.menu;

import me.colingrimes.midnight.menu.Gui;
import me.colingrimes.midnight.menu.pattern.MultiPattern;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class LolMenu extends Gui {

	public LolMenu(@Nonnull Player player) {
		super(player, "Example Menu", 3);
	}

	@Override
	public void draw() {
		// LOL (GOLD, IRON, COPPER)
		MultiPattern.create()
				.mask("G00IIIC00")
				.mask("G00I0IC00")
				.mask("GGGIIICCC")
				.item('G', Material.GOLD_INGOT)
				.item('I', Material.IRON_INGOT)
				.item('C', Material.COPPER_INGOT)
				.fill(this);
	}
}
