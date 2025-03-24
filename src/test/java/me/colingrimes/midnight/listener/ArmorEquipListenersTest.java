package me.colingrimes.midnight.listener;

import me.colingrimes.midnight.MockSetup;
import me.colingrimes.midnight.event.ArmorEquipEvent;
import me.colingrimes.midnight.event.SimpleClickEvent;
import me.colingrimes.midnight.event.util.EquipAction;
import me.colingrimes.midnight.event.util.SimpleAction;
import me.colingrimes.midnight.util.Common;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArmorEquipListenersTest extends MockSetup {

	private static final ArmorEquipListeners armorEquipListeners = new ArmorEquipListeners();
	private ItemStack mockCursor;
	private ItemStack mockClicked;
	@Mock private SimpleClickEvent simpleClickEvent;

	@BeforeEach
	void setUp() {
		this.mockCursor = bukkit.leggings;
		this.mockClicked = bukkit.boots;
		when(simpleClickEvent.getInventory()).thenReturn(mock(CraftingInventory.class));
		when(simpleClickEvent.getPlayer()).thenReturn(bukkit.player);
		when(simpleClickEvent.getCursor()).thenReturn(Optional.of(mockCursor));
		when(simpleClickEvent.getClicked()).thenReturn(Optional.of(mockClicked));
	}

	/**
	 * Tests picking up armor.
	 * Armor is only picked up when the slot type is ARMOR and the action is PICKUP.
	 */
	@Test
	void testOnSimpleClick_PickArmorUp() {
		when(simpleClickEvent.getSlotType()).thenReturn(InventoryType.SlotType.ARMOR);
		when(simpleClickEvent.getAction()).thenReturn(SimpleAction.PICKUP);
		armorEquipListeners.onSimpleClick(simpleClickEvent);
		common.verify(() -> Common.call(new ArmorEquipEvent(bukkit.player, EquipAction.UNEQUIP, null, mockClicked)));
	}

	/**
	 * Tests placing armor down.
	 * Armor is only placed down when the slot type is ARMOR and the action is PLACE.
	 */
	@Test
	void testOnSimpleClick_PlaceArmorDown() {
		when(simpleClickEvent.getSlotType()).thenReturn(InventoryType.SlotType.ARMOR);
		when(simpleClickEvent.getAction()).thenReturn(SimpleAction.PLACE);
		armorEquipListeners.onSimpleClick(simpleClickEvent);
		common.verify(() -> Common.call(new ArmorEquipEvent(bukkit.player, EquipAction.EQUIP, mockCursor, null)));
	}

	/**
	 * Tests dropping armor off.
	 * Armor is only dropped off when the slot type is ARMOR and the action is DROP.
	 */
	@Test
	void testOnSimpleClick_DropArmorOff() {
		when(simpleClickEvent.getSlotType()).thenReturn(InventoryType.SlotType.ARMOR);
		when(simpleClickEvent.getAction()).thenReturn(SimpleAction.DROP);
		armorEquipListeners.onSimpleClick(simpleClickEvent);
		common.verify(() -> Common.call(new ArmorEquipEvent(bukkit.player, EquipAction.UNEQUIP, null, mockClicked)));
	}

	/**
	 * Tests swapping armor.
	 * Armor is only swapped when the slot type is ARMOR and the action is SWAP.
	 */
	@Test
	void testOnSimpleClick_SwapArmor() {
		when(simpleClickEvent.getSlotType()).thenReturn(InventoryType.SlotType.ARMOR);
		when(simpleClickEvent.getAction()).thenReturn(SimpleAction.SWAP);
		armorEquipListeners.onSimpleClick(simpleClickEvent);
		common.verify(() -> Common.call(new ArmorEquipEvent(bukkit.player, EquipAction.SWAP, mockCursor, mockClicked)));
	}

	/**
	 * Tests shift+clicking armor.
	 * Armor is only shift+clicked when the specific action is MOVE_TO_OTHER_INVENTORY and armor is not already equipped.
	 */
	@Test
	void testOnSimpleClick_ShiftClickArmor() {
		when(simpleClickEvent.getSpecificAction()).thenReturn(InventoryAction.MOVE_TO_OTHER_INVENTORY);

		when(simpleClickEvent.getPlayer()).thenReturn(bukkit.player2);
		armorEquipListeners.onSimpleClick(simpleClickEvent);
		common.verify(() -> Common.call(new ArmorEquipEvent(bukkit.player2, EquipAction.EQUIP, mockClicked, null)), times(1));

		when(simpleClickEvent.getPlayer()).thenReturn(bukkit.player);
		armorEquipListeners.onSimpleClick(simpleClickEvent);
		common.verify(() -> Common.call(any()), times(1));
	}

	/**
	 * Tests keypadding armor.
	 * Armor is only keypadded when the specific action is HOTBAR_SWAP.
	 */
	@Test
	void testOnSimpleClick_KeypadArmor() {
		when(simpleClickEvent.getSpecificAction()).thenReturn(InventoryAction.HOTBAR_SWAP);
		when(simpleClickEvent.getHotbarButton()).thenReturn(0);
		armorEquipListeners.onSimpleClick(simpleClickEvent);
		common.verify(() -> Common.call(new ArmorEquipEvent(bukkit.player, EquipAction.SWAP, bukkit.helmet, mockClicked)));

		when(simpleClickEvent.getClicked()).thenReturn(Optional.empty());
		when(simpleClickEvent.getHotbarButton()).thenReturn(0);
		armorEquipListeners.onSimpleClick(simpleClickEvent);
		common.verify(() -> Common.call(new ArmorEquipEvent(bukkit.player, EquipAction.EQUIP, bukkit.helmet, null)));

		when(simpleClickEvent.getClicked()).thenReturn(Optional.of(mockClicked));
		when(simpleClickEvent.getHotbarButton()).thenReturn(1);
		armorEquipListeners.onSimpleClick(simpleClickEvent);
		common.verify(() -> Common.call(new ArmorEquipEvent(bukkit.player, EquipAction.UNEQUIP, null, mockClicked)));
	}
}
