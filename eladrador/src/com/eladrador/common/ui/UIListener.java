package com.eladrador.common.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import com.eladrador.common.Debug;

/**
 * A listener that handles events pertaining to UI, such as clicking on buttons.
 */
public class UIListener implements Listener {

	/**
	 * Creates a UIProfile for a player when they join.
	 */
	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		UIProfile.createNewProfile(player);
	}

	/**
	 * Deletes the player's UIProfile, as the information is no longer necessary
	 * given the fact that they've disconnected. <b>Note:</b> An
	 * {@code InventoryCloseEvent} will be fired for any player that disconnects
	 * with an inventory open.
	 */
	@EventHandler
	private void onPlayerDisconnect(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		UIProfile.remove(player);
	}

	@EventHandler
	private void onMenuOpen(InventoryOpenEvent event) {
		Player player = (Player) event.getPlayer();
		UIProfile profile = UIProfile.byPlayer(player);
		if (profile == null) {
			return;
		}
		profile.setIsMenuOpen(true);
	}

	@EventHandler
	private void onMenuClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		UIProfile profile = UIProfile.byPlayer(player);
		if (profile == null) {
			return;
		}
		UpperMenu topMenu = profile.getOpenUpperMenu();
		if (topMenu != null) {
			Inventory image = event.getInventory();
			topMenu.unregisterImage(image);
			topMenu.onClose(player);
		}
		Button cursorButton = profile.getButtonOnCursor();
		if (cursorButton != null) {
			UpperMenu discardConfirmMenu = new DiscardButtonOnCursorConfirmMenu(cursorButton);
			profile.setButtonOnCursor(null);
			profile.openUpperMenu(discardConfirmMenu);
		} else {
			profile.setOpenUpperMenu(null);
			profile.setIsMenuOpen(false);
		}
	}

	/**
	 * When a player clicks on a button in an inventory view.
	 */
	@EventHandler
	private void onButtonClickInInventory(InventoryClickEvent event) {
		ClickType clickType = event.getClick();
		if (clickType == ClickType.NUMBER_KEY) {
			// does not permit the use of number keys in an inventory view
			event.setCancelled(true);
			return;
		}
		Player player = (Player) event.getWhoClicked();
		UIProfile profile = UIProfile.byPlayer(player);
		if (profile == null) {
			return;
		}
		Inventory inventory = event.getClickedInventory();
		ButtonContainer menu;
		ButtonAddress addressClicked;
		Button button = profile.getButtonOnCursor();
		if (inventory == null) {
			menu = null;
			addressClicked = null;
		} else {
			if (inventory.getType() == InventoryType.PLAYER) {
				menu = profile.getLowerMenu();
			} else {
				menu = profile.getOpenUpperMenu();
			}
			int buttonIndex = event.getSlot();
			addressClicked = new ButtonAddress(menu, buttonIndex);
			if (button == null) {
				button = menu.getButton(buttonIndex);
			}
		}
		if (button != null) {
			ButtonToggleType toggleType = ButtonToggleType.byClickType(clickType);
			button.onToggle(player, toggleType, addressClicked);
			event.setCancelled(true);
		}
	}

	@EventHandler
	private void onButtonDragInInventory(InventoryDragEvent event) {
		Player player = (Player) event.getWhoClicked();
		UIProfile profile = UIProfile.byPlayer(player);
		if (profile == null) {
			return;
		}
		Inventory inventory = event.getInventory();
		ButtonContainer menu;
		ButtonAddress addressClicked;
		Button button = profile.getButtonOnCursor();
		if (inventory == null) {
			menu = null;
			addressClicked = null;
		} else {
			if (inventory.getType() == InventoryType.PLAYER) {
				menu = profile.getLowerMenu();
			} else {
				menu = profile.getOpenUpperMenu();
			}
			int buttonIndex = (int) event.getInventorySlots().toArray()[0];
			addressClicked = new ButtonAddress(menu, buttonIndex);
			if (button == null) {
				button = menu.getButton(buttonIndex);
			}
		}
		if (button != null) {
			ButtonToggleType toggleType = ButtonToggleType.STANDARD;
			button.onToggle(player, toggleType, addressClicked);
			event.setCancelled(true);
		}
	}

	/**
	 * When a player activates a Button on their hotbar by clicking their mouse.
	 */
	@EventHandler
	private void onButtonHotbarClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		UIProfile profile = UIProfile.byPlayer(player);
		if (profile == null) {
			return;
		}
		LowerMenu invMenu = profile.getLowerMenu();
		// 0 is the index of the item in the hand
		Button button = invMenu.getButton(0);
		if (button != null) {
			Action action = event.getAction();
			ButtonToggleType toggleType;
			switch (action) {
			case LEFT_CLICK_AIR:
				toggleType = ButtonToggleType.IN_HAND;
				break;
			case LEFT_CLICK_BLOCK:
				toggleType = ButtonToggleType.IN_HAND;
				break;
			case PHYSICAL:
				toggleType = null;
				break;
			case RIGHT_CLICK_AIR:
				toggleType = ButtonToggleType.SPECIAL;
				break;
			case RIGHT_CLICK_BLOCK:
				toggleType = ButtonToggleType.SPECIAL;
				break;
			default:
				toggleType = null;
				break;
			}
			button.onToggle(player, toggleType, null);
		}
	}

	/**
	 * When a player activates a Button on their hotbar by scrolling to it or
	 * tapping the associated number key.
	 */
	@EventHandler
	private void onButtonHotbarActivate(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		UIProfile profile = UIProfile.byPlayer(player);
		if (profile == null) {
			return;
		}
		LowerMenu invMenu = profile.getLowerMenu();
		int slot = event.getNewSlot();
		Button button = invMenu.getButton(slot);
		if (button != null) {
			button.onToggle(player, ButtonToggleType.SPECIAL, null);
		}
		// resets the Player's held item to the main slot
		player.getInventory().setHeldItemSlot(0);
	}

	@EventHandler
	private void onTapQ(PlayerDropItemEvent event) {
		// determines whether the item was dropped in the inventory view, or by tapping
		// 'Q' with the item selected on the hotbar
		Player player = event.getPlayer();
		UIProfile profile = UIProfile.byPlayer(player);
		if (profile == null) {
			return;
		}
		if (!profile.getIsMenuOpen()) {
			player.sendMessage("q");
		} else {
			player.sendMessage("not q");
		}
	}

	@EventHandler
	private void onThrowClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (event.getSlotType().equals(SlotType.OUTSIDE)) {
			event.setCancelled(true);
		}
	}

}
