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
import com.eladrador.common.item.DiscardGameItemStackConfirmMenu;
import com.eladrador.common.item.GameItemStackButton;
import com.eladrador.common.scheduling.DelayedTask;
import com.eladrador.common.utils.MathUtils;

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
		player.setCanPickupItems(false);
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
		if (cursorButton instanceof GameItemStackButton) {
			UpperMenu discardConfirmMenu = new DiscardGameItemStackConfirmMenu((GameItemStackButton) cursorButton,
					null);
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
		event.setCancelled(true);

		ClickType clickType = event.getClick();

		Inventory inventory = event.getClickedInventory();
		int clickedSlot = event.getSlot();
		Button cursorButton = profile.getButtonOnCursor();
		boolean isButtonOnCursor = cursorButton != null;
		Button toggledButton = null;
		if (isButtonOnCursor) {
			toggledButton = cursorButton;
		}
		ButtonAddress addressClicked;
		if (inventory == null) {
			// clickedSlot will be -1 or -999
			addressClicked = new ButtonAddress(null, clickedSlot);
		} else {
			InventoryType invType = inventory.getType();
			if (invType == InventoryType.CRAFTING) {
				return;
			}
			AbstractMenu menu = invType == InventoryType.PLAYER ? profile.getLowerMenu() : profile.getOpenUpperMenu();
			if (menu == null) {
				// this happens when the player clicks on an inventory on top that is not
				// associated with any upper menu
				return;
			}
			addressClicked = new ButtonAddress(menu, clickedSlot);
			if (!isButtonOnCursor) {
				toggledButton = menu.getButton(clickedSlot);
			}
		}
		if (toggledButton != null) {
			// so the final keyword can be added for use in an anonymous inner class
			final Button finalButton = toggledButton;
			ButtonToggleType toggleType = isButtonOnCursor ? ButtonToggleType.forClickTypeOnCursor(clickType)
					: ButtonToggleType.forClickTypeInMenu(clickType);
			if (toggleType == null) {
				// if the click type is not supported
				return;
			}
			ButtonToggleEvent toggleEvent = new ButtonToggleEvent(player, toggleType, addressClicked, event);
			// must be delayed so that the event can fully cancel
			new DelayedTask() {

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
		event.setCancelled(true);

		Set<Integer> rawSlots = event.getRawSlots();
		int rawButtonIndex = rawSlots.toArray(new Integer[rawSlots.size()])[0];
		boolean isCraftingSlot = MathUtils.checkInInterval(rawButtonIndex, 1, true, 4, true);
		if (isCraftingSlot) {
			return;
		}
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
		if (menu == null) {
			// this happens when the player clicks on an inventory on top that is not
			// associated with any upper menu
			return;
		}
		if (button != null) {
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
	 * When a player activates the button in their hand by clicking their mouse.
	 */
	@EventHandler
	private void onInHandClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		UIProfile profile = UIProfile.forPlayer(player);
		if (profile == null) {
			return;
		}
		event.setCancelled(true);

		Action action = event.getAction();
		ButtonToggleType toggleType = ButtonToggleType.forAction(action);
		if (toggleType == null) {
			// if the action is not supported
			return;
		}

		LowerMenu invMenu = profile.getLowerMenu();
		// 0 is the index of the item in the hand
		Button button = invMenu.getButton(0);
		if (button == null) {
			return;
		}

		ButtonAddress address = new ButtonAddress(invMenu, 0);
		ButtonToggleEvent toggleEvent = new ButtonToggleEvent(player, toggleType, address, event);
		button.onToggle(toggleEvent);
	}

	/*
	 * When a player toggles a Button on their hotbar by scrolling to it or tapping
	 * the associated number key.
	 */
	@EventHandler
	private void onButtonHotbarToggle(PlayerItemHeldEvent event) {
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
			ButtonToggleEvent toggleEvent = new ButtonToggleEvent(player, ButtonToggleType.HOTBAR, address, event);
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

		ButtonContainer container = profile.getLowerMenu();
		int slot = 0;
		Button button = container.getButton(slot);
		if (button != null) {
			ButtonToggleType toggleType = ButtonToggleType.TAP_Q_IN_MAIN_HAND;
			ButtonAddress address = new ButtonAddress(container, slot);
			ButtonToggleEvent toggleEvent = new ButtonToggleEvent(player, toggleType, address, event);
			button.onToggle(toggleEvent);
		}
	}

}
