package com.eladrador.common.ui;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

public class UIListener implements Listener {

	/**
	 * Keeps track of player-UI data.
	 */
	static HashMap<Player, UIProfile> profiles;

	static {
		profiles = new HashMap<Player, UIProfile>();
	}

	/**
	 * Creates a UIProfile for a player when they join.
	 */
	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		profiles.put(player, new UIProfile(player));
	}

	/**
	 * Deletes the player's UIProfile, as the information is no longer necessary
	 * given the fact that they've disconnected. Note: An InventoryCloseEvent will
	 * be fired for any player that disconnects with an inventory open.
	 */
	@EventHandler
	private void onPlayerDisconnect(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		profiles.remove(player);
	}

	@EventHandler
	private void onMenuOpen(InventoryOpenEvent event) {
		Player player = (Player) event.getPlayer();
		UIProfile profile = profiles.get(player);
		profile.setIsMenuOpen(true);
	}

	@EventHandler
	private void onMenuClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		UIProfile profile = profiles.get(player);
		TopMenu topMenu = profile.getOpenTopMenu();
		if (topMenu != null) {
			Inventory image = event.getInventory();
			topMenu.unregisterImage(image);
			topMenu.onClose(player);
			profile.setTopMenuOpen(null);
		}
		profile.setIsMenuOpen(false);
	}

	/**
	 * When a player clicks a Button in an inventory view.
	 */
	@EventHandler
	private void onButtonInventoryClick(InventoryClickEvent event) {
		ClickType clickType = event.getClick();
		if (clickType == ClickType.NUMBER_KEY) {
			// does not permit the use of number keys in an inventory view
			event.setCancelled(true);
			return;
		}
		Player player = (Player) event.getWhoClicked();
		Inventory inventory = event.getClickedInventory();
		if (inventory != null) {
			UIProfile profile = profiles.get(player);
			AbstractMenu menu;
			if (inventory.getType() == InventoryType.PLAYER) {
				menu = profile.getInventoryMenu();
			} else {
				menu = profile.getOpenTopMenu();
			}
			int buttonIndex = event.getSlot();
			Button button = menu.getButton(buttonIndex);
			if (button != null) {
				ButtonToggleType toggleType = ButtonToggleType.byClickType(clickType);
				ButtonAddress addressClicked = new ButtonAddress(menu, buttonIndex);
				button.onToggle(player, toggleType, addressClicked);
				event.setCancelled(true);
			}
		}
	}

	/**
	 * When a player activates a Button on their hotbar by clicking their mouse.
	 */
	@EventHandler
	private void onButtonHotbarClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		UIProfile profile = profiles.get(player);
		PlayerInventoryMenu invMenu = profile.getInventoryMenu();

		// 0 is the index of the item in their hand
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
		UIProfile profile = profiles.get(player);
		PlayerInventoryMenu invMenu = profile.getInventoryMenu();
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
		UIProfile profile = profiles.get(player);
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
