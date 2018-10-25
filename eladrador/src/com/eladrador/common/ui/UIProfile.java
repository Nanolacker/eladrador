package com.eladrador.common.ui;

import org.bukkit.entity.Player;

/**
 * Stores data concerning a Player and the user interface.
 */
class UIProfile {

	private PlayerInventoryMenu inventoryMenu;
	private Button mainHandButton;
	private Button offHandButton;
	private Button buttonOnCursor;
	private TopMenu openTopMenu;
	private boolean isMenuOpen;

	/**
	 * Constructs a new UIProfile for a Player. Only to be called when said Player
	 * is joining for the first time.
	 */
	UIProfile(Player player) {
		inventoryMenu = new PlayerInventoryMenu(player);
		mainHandButton = null;
		offHandButton = null;
		buttonOnCursor = null;
		openTopMenu = null;
		isMenuOpen = false;
	}

	/**
	 * Returns the PlayerInventoryMenu stored in this UIProfile.
	 */
	PlayerInventoryMenu getInventoryMenu() {
		return inventoryMenu;
	}

	/**
	 * Returns the Button held in the main hand of the Player represented by this
	 * UIProfile.
	 */
	Button getMainHandButton() {
		return mainHandButton;
	}

	/**
	 * Sets the value stored concerning the Button held in the main hand of the
	 * player represented by this UIProfile.
	 */
	void setMainHandButton(Button button) {
		mainHandButton = button;
	}

	/**
	 * Returns the Button held in the off hand of the Player represented by this
	 * UIProfile.
	 */
	Button getOffHandButton() {
		return offHandButton;
	}

	/**
	 * Sets the value stored concerning the Button held in the off hand of the
	 * player represented by this UIProfile.
	 */
	void setOffHandButton(Button button) {
		offHandButton = button;
	}

	/**
	 * Returns the TopMenu that is currently open by the Player represented by this
	 * UIProfile (null if no TopMenu is open).
	 */
	TopMenu getOpenTopMenu() {
		return openTopMenu;
	}

	/**
	 * Sets the value stored concerning the TopMenu currently open by the player
	 * represented by this UIProfile.
	 */
	void setTopMenuOpen(TopMenu menu) {
		openTopMenu = menu;
	}

	/**
	 * Returns the value stored concerning whether or not a menu is open by the
	 * player represented by this UIProfile.
	 */
	boolean getIsMenuOpen() {
		return isMenuOpen;
	}

	/**
	 * Sets the value stored concerning whether or not a menu is open by the player
	 * represented by this UIProfile.
	 */
	void setIsMenuOpen(boolean open) {
		isMenuOpen = open;
	}

}
