package com.eladrador.common.ui;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import com.eladrador.common.Debug;
import com.eladrador.common.item.DiscardButtonConfirmMenu;
import com.eladrador.common.scheduling.DelayedTask;

/**
 * A listener that handles events pertaining to UI, such as clicking on buttons.
 */
public final class UIListener implements Listener {

	/*
	 * Creates a UIProfile for a player when they join.
	 */
	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		UIProfile.createNewProfile(player);
	}

	/*
	 * Deletes the player's UIProfile, as the information is no longer necessary
	 * given the fact that they've disconnected. <b>Note:</b> An {@code
	 * InventoryCloseEvent} will be fired for any player that disconnects with an
	 * inventory open.
	 */
	@EventHandler
	private void onPlayerDisconnect(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		UIProfile.deleteProfile(player);
		player.getInventory().clear();
	}

	@EventHandler
	private void onMenuOpen(InventoryOpenEvent event) {
		Player player = (Player) event.getPlayer();
		UIProfile profile = UIProfile.forPlayer(player);
		if (profile == null) {
			return;
		}
		profile.setIsMenuOpen(true);
	}

	@EventHandler
	private void onMenuClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		UIProfile profile = UIProfile.forPlayer(player);
		if (profile == null) {
			return;
		}
		UpperMenu topMenu = profile.getOpenUpperMenu();
		if (topMenu != null) {
			topMenu.onClose(player);
		}
		Button cursorButton = profile.getButtonOnCursor();
		if (cursorButton != null) {
			UpperMenu discardConfirmMenu = new DiscardButtonConfirmMenu(cursorButton, true, null);
			profile.setButtonOnCursor(null);
			profile.openMenu(discardConfirmMenu);
		} else {
			profile.setOpenUpperMenu(null);
			profile.setIsMenuOpen(false);
		}
	}

	/*
	 * When a player clicks on a button and or clicks with a button on their cursor
	 * in an inventory view. The effect of the cursor button always takes priority
	 * over a button clicked inside a menu.
	 */
	@EventHandler
	private void onButtonClickInInventory(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		UIProfile profile = UIProfile.forPlayer(player);
		if (profile == null) {
			return;
		}
		ClickType clickType = event.getClick();
		if (clickType == ClickType.NUMBER_KEY) {
			// does not permit the use of number keys in an inventory view
			event.setCancelled(true);
			return;
		}
		Inventory inventory = event.getClickedInventory();
		Button cursorButton = profile.getButtonOnCursor();
		boolean isButtonOnCursor = cursorButton != null;
		Button toggledButton = null;
		if (isButtonOnCursor) {
			toggledButton = cursorButton;
		}
		ButtonAddress addressClicked;
		if (inventory == null) {
			addressClicked = null;
		} else {
			AbstractMenu menu = inventory.getType() == InventoryType.PLAYER ? profile.getLowerMenu()
					: profile.getOpenUpperMenu();
			int buttonIndex = event.getSlot();
			addressClicked = new ButtonAddress(menu, buttonIndex);
			if (!isButtonOnCursor) {
				toggledButton = menu.getButton(buttonIndex);
			}
		}
		if (toggledButton != null) {
			event.setCancelled(true);
			// so the final keyword can be added for use in an anonymous inner class
			final Button finalButton = toggledButton;
			ButtonToggleType toggleType = isButtonOnCursor ? ButtonToggleType.forClickTypeOnCursor(clickType)
					: ButtonToggleType.forClickTypeInMenu(clickType);
			ButtonToggleEvent toggleEvent = new ButtonToggleEvent(player, toggleType, addressClicked, event);
			// must be delayed so that the event can fully cancel
			new DelayedTask(0.0) {

				@Override
				public void run() {
					finalButton.onToggle(toggleEvent);
				}

			}.start();
		}
	}

	@EventHandler
	private void onButtonDragInInventory(InventoryDragEvent event) {
		Player player = (Player) event.getWhoClicked();
		UIProfile profile = UIProfile.forPlayer(player);
		if (profile == null) {
			return;
		}
		Set<Integer> rawSlots = event.getRawSlots();
		int rawButtonIndex = rawSlots.toArray(new Integer[rawSlots.size()])[0];
		// InventoryDragEvent.getInventory() always returns the top inventory, so we
		// must compare the slots to find out which inventory the player truly clicked
		Inventory inventory = rawButtonIndex < 54 ? event.getInventory() : player.getInventory();
		AbstractMenu menu;
		ButtonAddress addressClicked;
		Button button = profile.getButtonOnCursor();
		if (inventory == null) {
			menu = null;
			addressClicked = null;
		} else {
			InventoryType invType = inventory.getType();
			boolean isPlayerInventory = invType == InventoryType.PLAYER || invType == InventoryType.CRAFTING;
			if (isPlayerInventory) {
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
			event.setCancelled(true);
			// so the final keyword can be added for use in an anonymous inner class
			final Button finalButton = button;
			DragType dragType = event.getType();
			ButtonToggleType toggleType = ButtonToggleType.forDragTypeOnCursor(dragType);
			ButtonToggleEvent toggleEvent = new ButtonToggleEvent(player, toggleType, addressClicked, event);
			// must be delayed so that the event can fully cancel
			new DelayedTask(0.0) {

				@Override
				public void run() {
					finalButton.onToggle(toggleEvent);
				}

			}.start();
		}
	}

	/*
	 * When a player activates a Button on their hotbar by clicking their mouse.
	 */
	@EventHandler
	private void onButtonHotbarClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		UIProfile profile = UIProfile.forPlayer(player);
		if (profile == null) {
			return;
		}
		LowerMenu invMenu = profile.getLowerMenu();
		// 0 is the index of the item in the hand
		Button button = invMenu.getButton(0);
		if (button != null) {
			Action action = event.getAction();
			ButtonToggleType toggleType = ButtonToggleType.forAction(action);
			ButtonAddress address = new ButtonAddress(invMenu, 0);
			ButtonToggleEvent toggleEvent = new ButtonToggleEvent(player, toggleType, address, event);
			button.onToggle(toggleEvent);
		}
	}

	/*
	 * When a player activates a Button on their hotbar by scrolling to it or
	 * tapping the associated number key.
	 */
	@EventHandler
	private void onButtonHotbarActivate(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		UIProfile profile = UIProfile.forPlayer(player);
		if (profile == null) {
			return;
		}
		LowerMenu invMenu = profile.getLowerMenu();
		int slot = event.getNewSlot();
		Button button = invMenu.getButton(slot);
		if (button != null) {
			ButtonAddress address = new ButtonAddress(profile.getLowerMenu(), slot);
			ButtonToggleEvent toggleEvent = new ButtonToggleEvent(player, ButtonToggleType.RIGHT_CLICK_IN_HAND, address,
					event);
			button.onToggle(toggleEvent);
		}
		// resets the Player's held item to the main slot
		player.getInventory().setHeldItemSlot(0);
	}

	@EventHandler
	private void onThrowButton(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		UIProfile profile = UIProfile.forPlayer(player);
		if (profile == null) {
			return;
		}
		event.setCancelled(true);
		LowerMenu menu = profile.getLowerMenu();
		// 0 because selection is locked to first slot
		Button button = menu.getButton(0);
		if (button != null) {
			ButtonToggleType toggleType = ButtonToggleType.TAP_Q_IN_HAND;
			ButtonAddress address = new ButtonAddress(menu, 0);
			ButtonToggleEvent toggleEvent = new ButtonToggleEvent(player, toggleType, address, event);
			button.onToggle(toggleEvent);
		}
	}

}
