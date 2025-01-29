package me.colingrimes.midnight;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.Mockito.*;

public class BukkitMocks implements AutoCloseable {

	// Mock common items.
	@Mock public ItemStack item;
	@Mock public ItemStack item2;
	@Mock public ItemStack item3;
	@Mock public ItemStack helmet;
	@Mock public ItemStack chestplate;
	@Mock public ItemStack leggings;
	@Mock public ItemStack boots;

	// Mock world + location.
	@Mock public World world;
	@Mock public Location location;

	// Mock players.
	@Mock public Player player;
	@Mock public Player player2;
	
	private final AutoCloseable closeable;

	public BukkitMocks() {
		this.closeable = MockitoAnnotations.openMocks(this);

		// Setup item mocks.
		lenient().when(item.getType()).thenReturn(Material.STICK);
		lenient().when(item2.getType()).thenReturn(Material.DIAMOND);
		lenient().when(item3.getType()).thenReturn(Material.DEAD_BUSH);
		lenient().when(helmet.getType()).thenReturn(Material.TURTLE_HELMET);
		lenient().when(chestplate.getType()).thenReturn(Material.ELYTRA);
		lenient().when(leggings.getType()).thenReturn(Material.LEATHER_LEGGINGS);
		lenient().when(boots.getType()).thenReturn(Material.CHAINMAIL_BOOTS);

		// Setup location mock.
		lenient().when(location.getWorld()).thenReturn(world);
		lenient().when(location.getX()).thenReturn(1.0);
		lenient().when(location.getY()).thenReturn(2.0);
		lenient().when(location.getZ()).thenReturn(3.0);

		// Setup player mocks.
		lenient().when(player.getName()).thenReturn("Colin");
		lenient().when(player.getUniqueId()).thenReturn(UUID.fromString("00000000-0000-0000-0000-000000000001"));
		lenient().when(player.hasPermission("test.permission")).thenReturn(true);
		lenient().when(player.getWorld()).thenReturn(world);
		lenient().when(player.getLocation()).thenReturn(location);

		// Setup player 1 inventory (full).
		PlayerInventory inv1 = mock(PlayerInventory.class);
		lenient().when(player.getInventory()).thenReturn(inv1);
		lenient().when(inv1.getItem(0)).thenReturn(helmet);
		lenient().when(inv1.getContents()).thenReturn(new ItemStack[]{item, item2, item3});
		lenient().when(inv1.getArmorContents()).thenReturn(new ItemStack[]{helmet, chestplate, leggings, boots});

		// Setup player 2 inventory (empty).
		PlayerInventory inv2 = mock(PlayerInventory.class);
		lenient().when(player2.getInventory()).thenReturn(inv2);
		lenient().when(inv2.getArmorContents()).thenReturn(new ItemStack[4]);
	}

	@Override
	public void close() throws Exception {
		if (closeable != null) {
			closeable.close();
		}
	}
}
