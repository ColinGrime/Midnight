package me.colingrimes.midnight.util.bukkit;

import me.colingrimes.midnight.MockSetup;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemsTest extends MockSetup {

	@Test
	void testFindSlot() {
		assertEquals(Optional.empty(), Items.findSlot((Player) null, bukkit.helmet));
		assertEquals(Optional.empty(), Items.findSlot((Inventory) null, bukkit.helmet));
		assertEquals(Optional.empty(), Items.findSlot(bukkit.player, null));
		assertEquals(Optional.empty(), Items.findSlot(bukkit.player, mock(ItemStack.class)));
		assertEquals(Optional.of(1), Items.findSlot(bukkit.player, bukkit.item2));
	}

	@Test
	void testIsSameType() {
		ItemStack anotherStick = mock(ItemStack.class);
		when(anotherStick.getType()).thenReturn(Material.STICK);

		assertFalse(Items.isSameType(null, null));
		assertFalse(Items.isSameType(bukkit.item, null));
		assertFalse(Items.isSameType(null, anotherStick));
		assertFalse(Items.isSameType(bukkit.item, bukkit.item2));
		assertTrue(Items.isSameType(bukkit.item, anotherStick));
	}
}
