package me.colingrimes.midnight.listener;

import me.colingrimes.midnight.event.ArmorEquipEvent;
import me.colingrimes.midnight.event.SimpleClickEvent;
import me.colingrimes.midnight.event.util.EquipAction;
import me.colingrimes.midnight.event.util.SimpleAction;
import me.colingrimes.midnight.listener.util.ArmorType;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.util.Common;
import me.colingrimes.midnight.util.bukkit.Players;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class ArmorEquipListeners implements Listener {

	/**
	 * ArmorEquipEvent is run when armor is equipped through player inventory.
	 *
	 * @param event inventory click event
	 */
	@EventHandler
	public void onSimpleClick(SimpleClickEvent event) {
		if (!(event.getInventory() instanceof CraftingInventory)) {
			return;
		}

		Player player = event.getPlayer();
		ItemStack cursor = event.getCursor().orElse(null);
		ItemStack clicked = event.getClicked().orElse(null);
		boolean clickedArmor = event.getSlotType() == InventoryType.SlotType.ARMOR;

		// Accounts for PICKING armor up (unequip).
		if (clickedArmor && event.getAction() == SimpleAction.PICKUP) {
			Common.call(new ArmorEquipEvent(player, EquipAction.UNEQUIP, null, clicked));
		}

		// Accounts for PLACING armor down (equip).
		else if (clickedArmor && event.getAction() == SimpleAction.PLACE) {
			Common.call(new ArmorEquipEvent(player, EquipAction.EQUIP, cursor, null));
		}

		// Accounts for DROPPING armor off (unequip).
		else if (clickedArmor && event.getAction() == SimpleAction.DROP) {
			Common.call(new ArmorEquipEvent(player, EquipAction.UNEQUIP, null, clicked));
		}

		// Accounts for SWAPPING armor (swap).
		else if (clickedArmor && event.getAction() == SimpleAction.SWAP) {
			Common.call(new ArmorEquipEvent(player, EquipAction.SWAP, cursor, clicked));
		}

		// Accounts for SHIFT+CLICKING armor (equip).
		if (event.getSpecificAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
			Optional<ArmorType> armorType = ArmorType.fromItem(clicked);
			if (armorType.isPresent() && armorType.get().getFrom(player).isEmpty()) {
				Common.call(new ArmorEquipEvent(player, EquipAction.EQUIP, clicked, null));
			}
		}

		// Accounts for KEYPADDING armor (all).
		if (event.getSpecificAction() == InventoryAction.HOTBAR_SWAP) {
			cursor = player.getInventory().getItem(event.getHotbarButton());

			// The "clicked" is the item in the slot you are swapping with.
			// The "cursor" is the item in the hotbar slot.
			boolean isClickedArmor = ArmorType.fromItem(clicked).isPresent();
			boolean isCursorArmor = ArmorType.fromItem(cursor).isPresent();

			if (isClickedArmor && isCursorArmor) {
				Common.call(new ArmorEquipEvent(player, EquipAction.SWAP, cursor, clicked));
			} else if (isCursorArmor) {
				Common.call(new ArmorEquipEvent(player, EquipAction.EQUIP, cursor, null));
			} else if (isClickedArmor) {
				Common.call(new ArmorEquipEvent(player, EquipAction.UNEQUIP, null, clicked));
			}
		}
	}

	/**
	 * ArmorEquipEvent is run when armor is equipped from dragging it on.
	 *
	 * @param event inventory drag event
	 */
	@EventHandler
	public void onInventoryDrag(InventoryDragEvent event) {
		if (!(event.getInventory() instanceof CraftingInventory)) {
			return;
		}

		Player player = (Player) event.getWhoClicked();
		int slot = event.getRawSlots().stream().findFirst().orElse(-1);

		// Armor slots are between 5-8.
		if (slot < 5 || slot > 8) {
			return;
		}

		ItemStack newArmor = event.getOldCursor();
		ItemStack oldArmor = event.getCursor();

		if (oldArmor != null) {
			Common.call(new ArmorEquipEvent(player, EquipAction.SWAP, newArmor, oldArmor));
		} else {
			Common.call(new ArmorEquipEvent(player, EquipAction.EQUIP, newArmor, null));
		}
	}

	/**
	 * ArmorEquipEvent is run when armor is equipped through right-clicking the hand.
	 *
	 * @param event player interact event
	 */
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		Player player = event.getPlayer();
		ItemStack item = null;

		if (event.getHand() == EquipmentSlot.HAND) {
			item = player.getInventory().getItemInMainHand();
		} else if (event.getHand() == EquipmentSlot.OFF_HAND) {
			item = player.getInventory().getItemInOffHand();
		}

		Optional<ArmorType> armor = ArmorType.fromItem(item);

		// Item is a valid armor piece & player doesn't have that armor type equipped yet.
		if (armor.isPresent() && armor.get().getFrom(player).isEmpty()) {
			Common.call(new ArmorEquipEvent(player, EquipAction.EQUIP, item, null));
		}
	}

	/**
	 * ArmorEquipEvent is run when armor is broken.
	 *
	 * @param event player item break event
	 */
	@EventHandler
	public void onPlayerItemBreak(PlayerItemBreakEvent event) {
		if (ArmorType.fromItem(event.getBrokenItem()).isPresent()) {
			Common.call(new ArmorEquipEvent(event.getPlayer(), EquipAction.UNEQUIP, null, event.getBrokenItem()));
		}
	}

	/**
	 * ArmorEquipEvent is run when player is dead.
	 *
	 * @param event player death event
	 */
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		for (ItemStack armor : event.getEntity().getInventory().getArmorContents()) {
			if (armor != null) {
				Common.call(new ArmorEquipEvent(event.getEntity(), EquipAction.UNEQUIP, null, armor));
			}
		}
	}

	/**
	 * ArmorEquipEvent is run when armor is equipped through dispenser.
	 *
	 * @param event block dispense event
	 */
	@EventHandler
	public void onBlockDispense(BlockDispenseEvent event) {
		ArmorType armorType = ArmorType.fromItem(event.getItem()).orElse(null);
		Player player = Players.find(event.getBlock().getLocation(), 1).orElse(null);

		// Player doesn't have the armor type equipped yet.
		if (armorType == null || player == null || armorType.getFrom(player).isEmpty()) {
			return;
		}

		// Check if armor was equipped through dispenser (1 tick delay is needed).
		Scheduler.SYNC.run(() -> {
			if (armorType.getFrom(player).isPresent()) {
				Common.call(new ArmorEquipEvent(player, EquipAction.EQUIP, event.getItem(), null));
			}
		});
	}
}
