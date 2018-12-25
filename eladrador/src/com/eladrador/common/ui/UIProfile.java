package com.eladrador.common.ui;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.eladrador.common.Debug;
import com.eladrador.common.scheduling.DelayedTask;

/**
 * An bridge between players and the UI.
 */
public class UIProfile {

	/**
	 * Keeps track of player-UI data.
	 */
	static final HashMap<Player, UIProfile> PROFILES_MAP = new HashMap<Player, UIProfile>();

	/**
	 * The player that this profile represents.
	 */
	private final Player player;
	/**
	 * The {@link LowerMenu} that the player can interact with by clicking in their
	 * inventory.
	 */
	private final LowerMenu lowerMenu;

	/**
	 * The {@link UpperMenu} that is currently open. {@code null} if there is none
	 * open.
	 */
	private UpperMenu openUpperMenu;

	/**
	 * The {@link Button} whose image is held in the off hand slot of a player.
	 */
	private Button offHandButton;
	/**
	 * The {@link Button} whose image is on the cursor of a player while they have a
	 * menu open.
	 */
	private Button buttonOnCursor;
	/**
	 * Whether the player associated with this profile has a menu open, be it lower
	 * or upper.
	 */
	private boolean isMenuOpen;

	/**
	 * Constructs a new UIProfile for a Player. Only to be invoked when said Player
	 * is joining.
	 */
	UIProfile(Player player) {
		this.player = player;
		lowerMenu = new LowerMenu(player);
		openUpperMenu = null;
		offHandButton = null;
		buttonOnCursor = null;
		isMenuOpen = false;
	}

	/**
	 * Returns the UIProfile representing the specified player.
	 */
	public static UIProfile byPlayer(Player player) {
		return PROFILES_MAP.get(player);
	}

	/**
	 * Creates and stores a new UIProfile representing the specified player.
	 */
	static void createNewProfile(Player player) {
		PROFILES_MAP.put(player, new UIProfile(player));
	}

	/**
	 * Removes the UIProfile representing the specified player from storage.
	 */
	static void remove(Player player) {
		PROFILES_MAP.remove(player);
	}

	/**
	 * Returns the {@link LowerMenu} stored in this {@link UIProfile}.
	 */
	public LowerMenu getLowerMenu() {
		return lowerMenu;
	}

	/**
	 * Returns the {@link UpperMenu} that is currently open by the Player
	 * represented by this profile ({@code null} if none is open).
	 */
	public UpperMenu getOpenUpperMenu() {
		return openUpperMenu;
	}

	/**
	 * Opens a {@link UpperMenu} for the player of this profile.
	 * 
	 * @param upperMenu the {@link UpperMenu} to be opened
	 */
	public void openUpperMenu(UpperMenu upperMenu) {
		Inventory image = upperMenu.getNewImage();
		closeMenus();
		// A delay is necessary if the player is opening a menu immediately after
		// closing one. Odd behavior arises otherwise.
		DelayedTask openInventory = new DelayedTask(0.0) {

			@Override
			public void run() {
				player.openInventory(image);
			}

		};
		openInventory.start();
		upperMenu.registerImage(image);
		setOpenUpperMenu(upperMenu);
	}

	/**
	 * Closes any menus that the player represented by this profile has open, if
	 * any.
	 */
	public void closeMenus() {
		if (isMenuOpen) {
			player.closeInventory();
		}
	}

	/**
	 * Returns the button whose image is on the cursor of a player while their
	 * inventory is open.
	 */
	public Button getButtonOnCursor() {
		return buttonOnCursor;
	}

	/**
	 * Sets the button whose image is on the cursor of a player while their
	 * inventory is open.
	 */
	public void setButtonOnCursor(Button button) {
		buttonOnCursor = button;
		if (button != null) {
			player.setItemOnCursor(button.getNewImage());
		}
	}

	/**
	 * Returns the Button held in the off hand of the Player represented by this
	 * profile.
	 */
	public Button getOffHandButton() {
		return offHandButton;
	}

	/**
	 * Sets the value stored concerning the Button held in the off hand of the
	 * player represented by this profile.
	 */
	public void setOffHandButton(Button button) {
		offHandButton = button;
	}

	/**
	 * Returns whether the player associated with this profile has any menu open.
	 */
	public boolean getIsMenuOpen() {
		return isMenuOpen;
	}

	/**
	 * Sets the value stored concerning what upper menu is open by the player
	 * represented by this profile.
	 */
	void setOpenUpperMenu(UpperMenu menu) {
		openUpperMenu = menu;
	}

	/**
	 * Sets the value stored concerning whether or not a menu is open by the player
	 * represented by this profile.
	 */
	void setIsMenuOpen(boolean open) {
		isMenuOpen = open;
	}

}
