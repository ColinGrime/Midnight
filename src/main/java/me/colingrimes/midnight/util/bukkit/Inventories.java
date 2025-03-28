package me.colingrimes.midnight.util.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;

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
	 * Attempts to give the player the item.
	 * If the player's inventory has no room, no item will be given and this method will return false.
	 *
	 * @param player the player
	 * @param item the item to give to the player
	 * @return true if there was room in the player's inventory, false if there was no room
	 */
	public static boolean give(@Nonnull Player player, @Nonnull ItemStack item) {
		return give(player, item, false);
	}

	/**
	 * Attempts to give the player the item.
	 * If the player's inventory has no room, and {@code dropOnGround} is true, the remaining items will drop on the ground.
	 * If the player's inventory has no room, and {@code dropOnGround} is false, no items will be given.
	 *
	 * @param player the player
	 * @param item the item to give to the player
	 * @return true if there was room in the player's inventory, false if there was no room
	 */
	public static boolean give(@Nonnull Player player, @Nonnull ItemStack item, boolean dropOnGround) {
		if (!canFit(player.getInventory(), item) && !dropOnGround) {
			return false;
		}

		Collection<ItemStack> remaining = player.getInventory().addItem(item).values();
		for (ItemStack itemDrop : remaining) {
			player.getWorld().dropItemNaturally(player.getLocation(), itemDrop);
		}
		return !remaining.isEmpty();
	}

	/**
	 * Removes a single instance of the specified item from the inventory.
	 *
	 * @param item the item to remove
	 * @return the amount of items removed
	 */
	public static int removeSingle(@Nonnull Inventory inventory, @Nonnull ItemStack item) {
		return remove(inventory, item, 1);
	}

	/**
	 * Removes the full stack of the specified item from the inventory.
	 *
	 * @param item the item to remove
	 * @return the amount of items removed
	 */
	public static int remove(@Nonnull Inventory inventory, @Nonnull ItemStack item) {
		return remove(inventory, item, item.getAmount());
	}

	/**
	 * Removes the specified item from the inventory.
	 * If there is not enough to remove, they will all be removed.
	 *
	 * @param item the item to remove
	 * @param amount the amount to remove
	 * @return the amount of items removed
	 */
	public static int remove(@Nonnull Inventory inventory, @Nonnull ItemStack item, int amount) {
		if (amount <= 0) {
			return 0;
		}

		int removed = 0;

		// Priority: checks if the item is in the player's hand & removes that first.
		if (inventory instanceof PlayerInventory playerInventory && item.isSimilar(playerInventory.getItemInMainHand())) {
			int count = removeItemFromIndex(inventory, playerInventory.getHeldItemSlot(), amount);
			amount -= count;
			removed += count;
		}

		for (int i=0; i<inventory.getSize(); i++) {
			if (item.isSimilar(inventory.getItem(i))) {
				int count = removeItemFromIndex(inventory, i, amount);
				amount -= count;
				removed += count;
				if (amount <= 0) {
					return removed;
				}
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

	/**
	 * Removes the item based on index.
	 *
	 * @param inventory the inventory
	 * @param index the index of the item to remove
	 * @param amount the amount of items to remove
	 * @return the amount of items removed
	 */
	private static int removeItemFromIndex(@Nonnull Inventory inventory, int index, int amount) {
		ItemStack item = inventory.getItem(index);
		if (item == null || amount <= 0) {
			return 0;
		}

		int itemAmount = item.getAmount();
		if (itemAmount > amount) {
			item.setAmount(itemAmount - amount);
		} else {
			inventory.setItem(index, null);
		}
		return Math.min(itemAmount, amount);
	}

	private Inventories() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
