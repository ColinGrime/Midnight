package me.colingrimes.midnight.util.bukkit;

import me.colingrimes.midnight.MockSetup;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.inventory.InventoryMock;
import org.mockbukkit.mockbukkit.inventory.ItemStackMock;

import java.util.Arrays;
import java.util.Objects;

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
		assertEquals(1, Inventories.removeSingle(inventory, new ItemStackMock(Material.DIRT)));
		assertFalse(Inventories.isFull(inventory));
		assertEquals(0, Inventories.removeSingle(inventory, new ItemStackMock(Material.DIAMOND)));
		inventory.setItem(0, new ItemStackMock(Material.DIAMOND));
		assertTrue(inventory.contains(new ItemStackMock(Material.DIAMOND)));
		assertEquals(1, Inventories.removeSingle(inventory, new ItemStackMock(Material.DIAMOND)));
		assertFalse(inventory.contains(new ItemStackMock(Material.DIAMOND)));

		// Test removing the full stack of items.
		ItemStack emerald = new ItemStackMock(Material.EMERALD, 15);
		inventory.setItem(1, emerald);
		inventory.setItem(2, emerald);
		assertTrue(inventory.contains(emerald, 30));
		assertEquals(15, Inventories.remove(inventory, emerald));
		assertNull(inventory.getItem(1));
		assertTrue(inventory.contains(new ItemStackMock(Material.EMERALD, 15)));

		// Test removing a specific number of items.
		ItemStack diamond = new ItemStackMock(Material.DIAMOND, 10);
		ItemStack coal = new ItemStackMock(Material.COAL, 15);
		inventory.setItem(0, diamond);
		inventory.setItem(1, coal);
		assertFalse(inventory.contains(diamond, 11));
		assertTrue(inventory.contains(diamond, 10));
		assertTrue(inventory.contains(coal, 15));
		assertEquals(5, Inventories.remove(inventory, diamond, 5));
		assertEquals(5, Objects.requireNonNull(inventory.getItem(0)).getAmount());
		assertEquals(10, Inventories.remove(inventory, coal, 10));
		assertEquals(5, inventory.getContents()[1].getAmount());
		assertEquals(5, Inventories.remove(inventory, diamond, 5));
		assertNull(inventory.getItem(0));

		// Test removing all items.
		inventory.setItem(3, null);
		inventory.setItem(4, null);
		inventory.setItem(5, null);
		inventory.setItem(6, null);
		ItemStack[] prev = inventory.getContents();
		inventory.setItem(3, new ItemStackMock(Material.REDSTONE, 10));
		inventory.setItem(4, new ItemStackMock(Material.REDSTONE, 12));
		inventory.setItem(5, new ItemStackMock(Material.REDSTONE, 14));
		inventory.setItem(6, new ItemStackMock(Material.REDSTONE, 65));
		assertNotEquals(Arrays.asList(prev), Arrays.asList(inventory.getContents()));
		assertEquals(101, Inventories.removeAll(inventory, new ItemStackMock(Material.REDSTONE)));
		assertNull(inventory.getItem(3));
		assertNull(inventory.getItem(4));
		assertNull(inventory.getItem(5));
		assertNull(inventory.getItem(6));
		assertEquals(Arrays.asList(prev), Arrays.asList(inventory.getContents()));
	}
}
