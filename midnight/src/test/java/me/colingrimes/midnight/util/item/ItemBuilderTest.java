package me.colingrimes.midnight.util.item;

import be.seeseemelk.mockbukkit.MockBukkit;
import me.colingrimes.midnight.MidnightPlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemBuilderTest {

	private static MidnightPlugin plugin;

	@BeforeAll
	public static void load() {
		MockBukkit.mock();
		plugin = MockBukkit.load(MidnightPlugin.class);
	}

	@AfterAll
	public static void unload() {
		MockBukkit.unmock();
	}

	@Test
	@DisplayName("Verify that the item builder works.")
	void testBuilder() {
		// Test Justingo input.
		ItemStack justingoItem = new ItemBuilder(Material.END_CRYSTAL)
				.name("Justingo")
				.lore(List.of("Justingo's Milk", "&f&lMILK"))
				.glow(true).build();

		assertEquals(Material.END_CRYSTAL, justingoItem.getType());
		assertNotNull(justingoItem.getItemMeta());
		assertEquals("Justingo", justingoItem.getItemMeta().getDisplayName());
		assertEquals(List.of("Justingo's Milk", "§f§lMILK"), justingoItem.getItemMeta().getLore());
		assertTrue(justingoItem.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS));

		// Test config input.
		ItemStack configItem = new ItemBuilder(Material.BARRIER)
				.config(plugin.getConfig().getConfigurationSection("items.claimer"))
				.placeholder("%markers%", 1)
				.placeholder("%max_markers%", 10)
				.build();

		assertNotEquals(Material.BARRIER, configItem.getType());
		assertEquals(Material.GOLDEN_SHOVEL, configItem.getType());
		assertNotNull(configItem.getItemMeta());
		assertEquals("§4§l§k|||§r §c§lDomain §f§lClaimer §4§l§k|||", configItem.getItemMeta().getDisplayName());
		assertNotNull(configItem.getItemMeta().getLore());
		assertTrue(configItem.getItemMeta().getLore().contains("§7Markers Set: §e1§7/§e10"));
		assertTrue(justingoItem.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS));

		// Test another input.
		ItemStack testItem = new ItemBuilder(Material.SOUL_SAND)
				.material(Material.BASALT)
				.material((String) null)
				.build();

		assertNotEquals(Material.SOUL_SAND, testItem.getType());
		assertEquals(Material.BASALT, testItem.getType());
		assertNotNull(testItem.getItemMeta());
		assertFalse(testItem.getItemMeta().hasDisplayName());
		assertNull(testItem.getItemMeta().getLore());
		assertFalse(testItem.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS));
	}
}