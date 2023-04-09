package me.colingrimes.midnight.util.item;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.inventory.meta.ItemMetaMock;
import me.colingrimes.midnight.plugin.Midnight;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NBTTest {

	@BeforeAll
	public static void load() {
		MockBukkit.mock();
		MockBukkit.load(Midnight.class);
	}

	@AfterAll
	public static void unload() {
		MockBukkit.unmock();
	}

	@Test
	@DisplayName("Verify that NBT tags are being set correctly.")
	void testNBT() {
		// Mock item.
		ItemStack item = mock(ItemStack.class);
		when(item.getItemMeta()).thenReturn(new ItemMetaMock());

		// Test valid input.
		NBT.setTag(item, "key", "value");
		assertTrue(NBT.getTag(item, "key").isPresent());
		assertEquals("value", NBT.getTag(item, "key").get());

		// Test invalid input.
		assertTrue(NBT.getTag(null, null).isEmpty());
		assertTrue(NBT.getTag(null, "test").isEmpty());
		assertTrue(NBT.getTag(null, "").isEmpty());
		assertTrue(NBT.getTag(item, null).isEmpty());
		assertTrue(NBT.getTag(item, "test").isEmpty());
		assertTrue(NBT.getTag(item, "").isEmpty());
	}
}