package me.colingrimes.midnight.util.bukkit;

import me.colingrimes.midnight.MockSetup;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.inventory.InventoryMock;
import org.mockbukkit.mockbukkit.inventory.ItemStackMock;

import static org.junit.jupiter.api.Assertions.*;

class InventoriesTest extends MockSetup {

	@Test
	void testisFullAndCanFit() {
		Inventory inventory = new InventoryMock(null, InventoryType.PLAYER);
		assertFalse(Inventories.isFull(inventory));
		assertTrue(Inventories.canFit(inventory, new ItemStackMock(Material.DIRT)));
		assertTrue(Inventories.canFit(inventory, new ItemStackMock(Material.STONE)));

		for (int i=0; i<inventory.getSize(); i++) {
			inventory.setItem(i, new ItemStackMock(Material.DIRT));
		}
		assertTrue(Inventories.isFull(inventory));
		assertTrue(Inventories.canFit(inventory, new ItemStackMock(Material.DIRT)));
		assertFalse(Inventories.canFit(inventory, new ItemStackMock(Material.STONE)));

		for (int i=0; i<inventory.getSize(); i++) {
			inventory.setItem(i, new ItemStackMock(Material.DIRT, 64));
		}
		assertTrue(Inventories.isFull(inventory));
		assertFalse(Inventories.canFit(inventory, new ItemStackMock(Material.DIRT)));
		assertFalse(Inventories.canFit(inventory, new ItemStackMock(Material.STONE)));
	}

	@Test
	void testRemove() {
		Inventory inventory = new InventoryMock(null, InventoryType.PLAYER);
		for (int i=0; i<inventory.getSize(); i++) {
			inventory.setItem(i, new ItemStackMock(Material.DIRT));
		}

		// Test removing a single item.
		assertTrue(Inventories.isFull(inventory));
		assertTrue(Inventories.remove(inventory, new ItemStackMock(Material.DIRT)));
		assertFalse(Inventories.isFull(inventory));
		assertFalse(Inventories.remove(inventory, new ItemStackMock(Material.DIAMOND)));
		inventory.setItem(0, new ItemStackMock(Material.DIAMOND));
		assertTrue(inventory.contains(new ItemStackMock(Material.DIAMOND)));
		assertTrue(Inventories.remove(inventory, new ItemStackMock(Material.DIAMOND)));
		assertFalse(inventory.contains(new ItemStackMock(Material.DIAMOND)));

		// Test removing a specific number of items.
//		ItemStack diamond = new ItemStackMock(Material.DIAMOND, 10);
//		inventory.setItem(0, diamond);
//		assertFalse(inventory.contains(diamond, 11));
//		assertTrue(inventory.contains(diamond, 10));
//		assertTrue(Inventories.remove(inventory, diamond, 5));
//		assertEquals(5, inventory.getItem(0).getAmount());
	}
}
