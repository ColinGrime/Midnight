package me.colingrimes.midnight.util.bukkit;

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
	private ItemStack itemStack1;
	private ItemStack itemStack2;
	private ItemStack itemStack3;

	@BeforeEach
	void setUp() {
		player = mock(Player.class);
		itemStack1 = mock(ItemStack.class);
		itemStack2 = mock(ItemStack.class);
		itemStack3 = mock(ItemStack.class);

		when(itemStack1.getType()).thenReturn(Material.DIAMOND);
		when(itemStack2.getType()).thenReturn(Material.GOLD_INGOT);
		when(itemStack3.getType()).thenReturn(Material.DIAMOND);

		var inventory = mock(PlayerInventory.class);
		when(inventory.getContents()).thenReturn(new ItemStack[]{ null, itemStack1, null, itemStack2 });
		when(player.getInventory()).thenReturn(inventory);
	}

	@Test
	void testFindSlot() {
		assertEquals(Optional.empty(), Items.findSlot((Player) null, itemStack1));
		assertEquals(Optional.empty(), Items.findSlot((Inventory) null, itemStack1));
		assertEquals(Optional.empty(), Items.findSlot(player, null));
		assertEquals(Optional.empty(), Items.findSlot(player, mock(ItemStack.class)));
		assertEquals(Optional.of(1), Items.findSlot(player, itemStack1));

	}

	@Test
	void testIsSameType() {
		assertFalse(Items.isSameType(null, null));
		assertFalse(Items.isSameType(itemStack1, null));
		assertFalse(Items.isSameType(null, itemStack2));
		assertFalse(Items.isSameType(itemStack1, itemStack2));
		assertTrue(Items.isSameType(itemStack1, itemStack3));
	}
}
