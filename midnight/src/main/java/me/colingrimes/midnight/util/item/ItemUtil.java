package me.colingrimes.midnight.util.item;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Provides various utilities for {@link ItemStack} objects.
 */
public final class ItemUtil {

	/**
	 * Finds the slot number of the first occurrence of the specified item,
	 * @param player the player to check
	 * @param item the item to search for
	 * @return the first slot that contains the item
	 */
	@Nonnull
	public static Optional<Integer> findSlot(@Nullable Player player, @Nullable ItemStack item) {
		return player == null ? Optional.empty() : findSlot(player.getInventory(), item);
	}

	/**
	 * Finds the slot number of the first occurrence of the specified item,
	 * @param inv the inventory to check
	 * @param item the item to search for
	 * @return the first slot that contains the item
	 */
	@Nonnull
	public static Optional<Integer> findSlot(@Nullable Inventory inv, @Nullable ItemStack item) {
		return inv == null ? Optional.empty() : findSlot(inv.getContents(), item);
	}

	/**
	 * Finds the slot number of the first occurrence of the specified item,
	 * @param items the items to check
	 * @param item the item to search for
	 * @return the first slot that contains the item
	 */
	@Nonnull
	public static Optional<Integer> findSlot(@Nullable ItemStack[] items, @Nullable ItemStack item) {
		if (items == null || item == null) {
			return Optional.empty();
		}

		for (int i=0; i<items.length; i++) {
			if (item.equals(items[i])) {
				return Optional.of(i);
			}
		}

		return Optional.empty();
	}

	/**
	 * Checks if two items are of the same {@link org.bukkit.Material}.
	 * @param item1 any item
	 * @param item2 any item
	 * @return true if the two items are of the same type
	 */
	public static boolean isSameType(@Nullable ItemStack item1, @Nullable ItemStack item2) {
		if (item1 == null || item2 == null) {
			return false;
		}

		return item1.getType().equals(item2.getType());
	}

	/**
	 * Parses a {@link ConfigurationSection} and checks for the following:
	 * - A "type" or "material" key for materials.
	 * - A "name" key for the name of the item.
	 * - A "lore" key for the lore of the item.
	 * - A "glowing" key for whether the item should be glowing or not.
	 *
	 * @param sec the section of the configuration file
	 * @return the itembuilder object
	 */
	@Nonnull
	public static ItemStack config(@Nullable ConfigurationSection sec) {
		return new ItemBuilder().config(sec).build();
	}

	private ItemUtil() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
