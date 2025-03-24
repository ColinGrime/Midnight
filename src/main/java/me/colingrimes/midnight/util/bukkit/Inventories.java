package me.colingrimes.midnight.util.bukkit;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class Inventories {

	/**
	 * Checks if the inventory does not have an empty slot.
	 *
	 * @param inventory the inventory to check
	 * @return true if the inventory is full (no empty slot)
	 */
	public static boolean isFull(@Nonnull Inventory inventory) {
		return inventory.firstEmpty() == -1;
	}

	/**
	 * Checks if the specified item can fit in the inventory.
	 * If the inventory is full, it will check to see if it can stack with a similar item.
	 *
	 * @param inventory the inventory to check
	 * @param item the item to check
	 * @return true if the item can fit in the inventory
	 */
	public static boolean canFit(@Nonnull Inventory inventory, @Nonnull ItemStack item) {
		if (!isFull(inventory)) {
			return true;
		}
		return Arrays.stream(inventory.getContents()).anyMatch(i -> i != null && i.getAmount() < i.getMaxStackSize() && i.isSimilar(item));
	}

	/**
	 * Removes a single instance of the specified item from the inventory.
	 *
	 * @param item the item to remove
	 * @return true if the item was removed
	 */
	public static boolean remove(@Nonnull Inventory inventory, @Nonnull ItemStack item) {
		return remove(inventory, item, 1);
	}

	/**
	 * Removes the specified item from the inventory.
	 * If there is not enough to remove, they will all be removed.
	 *
	 * @param item the item to remove
	 * @param amount the amount to remove
	 * @return true if at least 1 item was removed
	 */
	public static boolean remove(@Nonnull Inventory inventory, @Nonnull ItemStack item, int amount) {
		if (amount <= 0) {
			return false;
		}

		boolean removed = false;
		for (int i=0; i<inventory.getSize(); i++) {
			ItemStack invItem = inventory.getItem(i);
			if (invItem == null || !item.isSimilar(invItem)) {
				continue;
			}

			int invAmount = invItem.getAmount();
			if (invAmount <= amount) {
				inventory.setItem(i, null);
				amount -= invAmount;
				removed = true;
			} else {
				invItem.setAmount(invAmount - amount);
				inventory.setItem(i, invItem);
				return true;
			}
		}
		return removed;
	}

	/**
	 * Removes all the specified item from the inventory.
	 *
	 * @param item the item to remove
	 * @return the amount of items removed
	 */
	public static int removeAll(@Nonnull Inventory inventory, @Nonnull ItemStack item) {
		int removed = 0;
		for (int i=0; i<inventory.getSize(); i++) {
			ItemStack invItem = inventory.getItem(i);
			if (invItem != null && item.isSimilar(invItem)) {
				inventory.setItem(i, null);
				removed += invItem.getAmount();
			}
		}
		return removed;
	}

	private Inventories() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
