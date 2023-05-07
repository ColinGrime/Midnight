package me.colingrimes.plugin.listener.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public enum ArmorType {

	HELMET(3),
	CHESTPLATE(2),
	LEGGINGS(1),
	BOOTS(0);

	private final int index;

	ArmorType(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	/**
	 * Gets the armor type item from the player's inventory if it exists.
	 * @param player the player
	 * @return armor from inventory
	 */
	@Nonnull
	public Optional<ItemStack> getFrom(@Nonnull Player player) {
		return Optional.ofNullable(player.getInventory().getArmorContents()[index]);
	}

	/**
	 * Gets the armor type from the item stack.
	 * @param item the item stack
	 * @return armor type
	 */
	@Nonnull
	public static Optional<ArmorType> fromItem(@Nullable ItemStack item) {
		if (item == null) {
			return Optional.empty();
		}

		for (ArmorType armor : ArmorType.values()) {
			if (item.getType().name().endsWith(armor.name())) {
				return Optional.of(armor);
			}
		}

		return Optional.empty();
	}
}