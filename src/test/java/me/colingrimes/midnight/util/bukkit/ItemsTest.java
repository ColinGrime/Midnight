package me.colingrimes.midnight.util.bukkit;

import me.colingrimes.midnight.MockSetup;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemsTest extends MockSetup {

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
