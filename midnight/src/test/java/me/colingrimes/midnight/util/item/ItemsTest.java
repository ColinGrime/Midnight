package me.colingrimes.midnight.util.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemsTest {

	private Player player;
	private PlayerInventory inventory;
	private ItemStack itemStack1;
	private ItemStack itemStack2;
	private ItemStack[] items;

	@BeforeEach
	void setUp() {
		player = mock(Player.class);
		inventory = mock(PlayerInventory.class);
		itemStack1 = mock(ItemStack.class);
		itemStack2 = mock(ItemStack.class);

		when(itemStack1.getType()).thenReturn(Material.DIAMOND);
		when(itemStack2.getType()).thenReturn(Material.GOLD_INGOT);

		items = new ItemStack[]{null, itemStack1, null, itemStack2};
		when(inventory.getContents()).thenReturn(items);
		when(player.getInventory()).thenReturn(inventory);
	}

	@Test
	void findSlot_nullPlayer() {
		assertEquals(Optional.empty(), Items.findSlot((Player) null, itemStack1), "Result should be empty Optional when Player is null");
	}

	@Test
	void findSlot_nullInventory() {
		assertEquals(Optional.empty(), Items.findSlot((Inventory) null, itemStack1), "Result should be empty Optional when Inventory is null");
	}

	@Test
	void findSlot_nullItemStack() {
		assertEquals(Optional.empty(), Items.findSlot(player, null), "Result should be empty Optional when ItemStack is null");
	}

	@Test
	void findSlot_itemExists() {
		assertEquals(Optional.of(1), Items.findSlot(player, itemStack1), "Result should be the correct slot when ItemStack exists");
	}

	@Test
	void findSlot_itemDoesNotExist() {
		ItemStack itemStack3 = mock(ItemStack.class);
		when(itemStack3.getType()).thenReturn(Material.IRON_INGOT);
		assertEquals(Optional.empty(), Items.findSlot(player, itemStack3), "Result should be empty Optional when ItemStack does not exist");
	}

	@Test
	void isSameType_bothNull() {
		assertFalse(Items.isSameType(null, null), "Result should be false when both ItemStacks are null");
	}

	@Test
	void isSameType_oneNull() {
		assertFalse(Items.isSameType(itemStack1, null), "Result should be false when one ItemStack is null");
		assertFalse(Items.isSameType(null, itemStack2), "Result should be false when one ItemStack is null");
	}

	@Test
	void isSameType_sameType() {
		ItemStack anotherItemStack1 = mock(ItemStack.class);
		when(anotherItemStack1.getType()).thenReturn(Material.DIAMOND);
		assertTrue(Items.isSameType(itemStack1, anotherItemStack1), "Result should be true when both ItemStacks are of the same type");
	}

	@Test
	void isSameType_differentType() {
		assertFalse(Items.isSameType(itemStack1, itemStack2), "Result should be false when both ItemStacks are of different types");
	}
}
