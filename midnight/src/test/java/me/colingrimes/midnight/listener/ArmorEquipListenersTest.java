package me.colingrimes.midnight.listener;

import static org.mockito.Mockito.*;

import me.colingrimes.midnight.event.ArmorEquipEvent;
import me.colingrimes.midnight.event.SimpleClickEvent;
import me.colingrimes.midnight.event.util.EquipAction;
import me.colingrimes.midnight.event.util.SimpleAction;
import me.colingrimes.midnight.util.Common;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Optional;

public class ArmorEquipListenersTest {

	private static final ArmorEquipListeners armorEquipListeners = new ArmorEquipListeners();

	private Player mockPlayer;
	private PlayerInventory mockInventory;
	private PlayerInventory mockEmptyInventory;

	private ItemStack mockArmor1;
	private ItemStack mockArmor2;
	private ItemStack mockCursor;
	private ItemStack mockClicked;
	private ItemStack mockNonArmor;

	private SimpleClickEvent simpleClickEvent;
	private MockedStatic<Common> mockCommon;

	@BeforeEach
	public void setUp() {
		mockPlayer = mock(Player.class);
		mockInventory = mock(PlayerInventory.class);
		mockEmptyInventory = mock(PlayerInventory.class);
		mockArmor1 = mock(ItemStack.class);
		mockArmor2 = mock(ItemStack.class);
		mockCursor = mock(ItemStack.class);
		mockClicked = mock(ItemStack.class);
		mockNonArmor = mock(ItemStack.class);

		when(mockArmor1.getType()).thenReturn(Material.TURTLE_HELMET);
		when(mockArmor2.getType()).thenReturn(Material.ELYTRA);
		when(mockCursor.getType()).thenReturn(Material.LEATHER_LEGGINGS);
		when(mockClicked.getType()).thenReturn(Material.CHAINMAIL_BOOTS);
		when(mockNonArmor.getType()).thenReturn(Material.AIR);
		when(mockInventory.getItem(0)).thenReturn(mockArmor1);
		when(mockInventory.getArmorContents()).thenReturn(new ItemStack[]{mockArmor1, mockArmor2, mockCursor, mockClicked});
		when(mockEmptyInventory.getArmorContents()).thenReturn(new ItemStack[4]);

		simpleClickEvent = mock(SimpleClickEvent.class);
		mockCommon = mockStatic(Common.class);

		when(simpleClickEvent.getInventory()).thenReturn(mock(CraftingInventory.class));
		when(simpleClickEvent.getPlayer()).thenReturn(mockPlayer);
		when(simpleClickEvent.getCursor()).thenReturn(Optional.of(mockCursor));
		when(simpleClickEvent.getClicked()).thenReturn(Optional.of(mockClicked));
	}

	@AfterEach
	public void tearDown() {
		mockCommon.close();
	}

	/**
	 * Tests picking up armor.
	 * Armor is only picked up when the slot type is ARMOR and the action is PICKUP.
	 */
	@Test
	public void testOnSimpleClick_PickArmorUp() {
		when(simpleClickEvent.getSlotType()).thenReturn(InventoryType.SlotType.ARMOR);
		when(simpleClickEvent.getAction()).thenReturn(SimpleAction.PICKUP);
		armorEquipListeners.onSimpleClick(simpleClickEvent);
		mockCommon.verify(() -> Common.call(new ArmorEquipEvent(mockPlayer, EquipAction.UNEQUIP, null, mockClicked)));
	}

	/**
	 * Tests placing armor down.
	 * Armor is only placed down when the slot type is ARMOR and the action is PLACE.
	 */
	@Test
	public void testOnSimpleClick_PlaceArmorDown() {
		when(simpleClickEvent.getSlotType()).thenReturn(InventoryType.SlotType.ARMOR);
		when(simpleClickEvent.getAction()).thenReturn(SimpleAction.PLACE);
		armorEquipListeners.onSimpleClick(simpleClickEvent);
		mockCommon.verify(() -> Common.call(new ArmorEquipEvent(mockPlayer, EquipAction.EQUIP, mockCursor, null)));
	}

	/**
	 * Tests dropping armor off.
	 * Armor is only dropped off when the slot type is ARMOR and the action is DROP.
	 */
	@Test
	public void testOnSimpleClick_DropArmorOff() {
		when(simpleClickEvent.getSlotType()).thenReturn(InventoryType.SlotType.ARMOR);
		when(simpleClickEvent.getAction()).thenReturn(SimpleAction.DROP);
		armorEquipListeners.onSimpleClick(simpleClickEvent);
		mockCommon.verify(() -> Common.call(new ArmorEquipEvent(mockPlayer, EquipAction.UNEQUIP, null, mockClicked)));
	}

	/**
	 * Tests swapping armor.
	 * Armor is only swapped when the slot type is ARMOR and the action is SWAP.
	 */
	@Test
	public void testOnSimpleClick_SwapArmor() {
		when(simpleClickEvent.getSlotType()).thenReturn(InventoryType.SlotType.ARMOR);
		when(simpleClickEvent.getAction()).thenReturn(SimpleAction.SWAP);
		armorEquipListeners.onSimpleClick(simpleClickEvent);
		mockCommon.verify(() -> Common.call(new ArmorEquipEvent(mockPlayer, EquipAction.SWAP, mockCursor, mockClicked)));
	}

	/**
	 * Tests shift+clicking armor.
	 * Armor is only shift+clicked when the specific action is MOVE_TO_OTHER_INVENTORY and armor is not already equipped.
	 */
	@Test
	public void testOnSimpleClick_ShiftClickArmor() {
		when(simpleClickEvent.getSpecificAction()).thenReturn(InventoryAction.MOVE_TO_OTHER_INVENTORY);

		when(mockPlayer.getInventory()).thenReturn(mockEmptyInventory);
		armorEquipListeners.onSimpleClick(simpleClickEvent);
		mockCommon.verify(() -> Common.call(new ArmorEquipEvent(mockPlayer, EquipAction.EQUIP, mockClicked, null)));

		when(mockPlayer.getInventory()).thenReturn(mockInventory);
		armorEquipListeners.onSimpleClick(simpleClickEvent);
		mockCommon.verifyNoMoreInteractions();
	}

	/**
	 * Tests keypadding armor.
	 * Armor is only keypadded when the specific action is HOTBAR_SWAP.
	 */
	@Test
	public void testOnSimpleClick_KeypadArmor() {
		when(simpleClickEvent.getSpecificAction()).thenReturn(InventoryAction.HOTBAR_SWAP);
		when(mockPlayer.getInventory()).thenReturn(mockInventory);

		when(simpleClickEvent.getClicked()).thenReturn(Optional.of(mockClicked));
		when(simpleClickEvent.getHotbarButton()).thenReturn(0);
		armorEquipListeners.onSimpleClick(simpleClickEvent);
		mockCommon.verify(() -> Common.call(new ArmorEquipEvent(mockPlayer, EquipAction.SWAP, mockArmor1, mockClicked)));

		when(simpleClickEvent.getClicked()).thenReturn(Optional.empty());
		when(simpleClickEvent.getHotbarButton()).thenReturn(0);
		armorEquipListeners.onSimpleClick(simpleClickEvent);
		mockCommon.verify(() -> Common.call(new ArmorEquipEvent(mockPlayer, EquipAction.EQUIP, mockArmor1, null)));

		when(simpleClickEvent.getClicked()).thenReturn(Optional.of(mockClicked));
		when(simpleClickEvent.getHotbarButton()).thenReturn(1);
		armorEquipListeners.onSimpleClick(simpleClickEvent);
		mockCommon.verify(() -> Common.call(new ArmorEquipEvent(mockPlayer, EquipAction.UNEQUIP, null, mockClicked)));
	}
}
