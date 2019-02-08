package com.eladrador.common.ui;

import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * A bridge between players and the custom UI.
 */
public class UIProfile {

	/**
	 * Keeps track of player-UI data.
	 */
	private static final HashMap<Player, UIProfile> profilesMap = new HashMap<Player, UIProfile>();

	/**
	 * The player that this {@code UIProfile} represents.
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
	private Cursor cursor;
	private OffHand offHand;
	/**
	 * Whether the player associated with this {@code UIProfile} has a menu open, be
	 * it lower or upper.
	 */
	private boolean menuIsOpen;

	/**
	 * Constructs a new UIProfile for a player. Only to be invoked when said player
	 * is joining.
	 */
	UIProfile(Player player) {
		this.player = player;
		lowerMenu = new LowerMenu(player);
		openUpperMenu = null;
		cursor = new Cursor(player);
		offHand = new OffHand(player);
		menuIsOpen = false;
	}

	/**
	 * Returns the {@code UIProfile} corresponding to the specified player.
	 */
	public static UIProfile forPlayer(Player player) {
		return profilesMap.get(player);
	}

	/**
	 * Creates and stores a new {@code UIProfile} corresponding to the specified
	 * player.
	 */
	static void createNewProfile(Player player) {
		profilesMap.put(player, new UIProfile(player));
	}

	/**
	 * Deletes the {@code UIProfile} representing the specified player from storage.
	 */
	static void deleteProfile(Player player) {
		profilesMap.remove(player);
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
	 * @param menu the menu to be opened
	 */
	public void openMenu(UpperMenu menu) {
		closeMenus();
		Inventory image = menu.getImage();
		player.openInventory(image);
		openUpperMenu = menu;
		menuIsOpen = true;
	}

	/**
	 * Closes any menus that the player represented by this profile has open, if
	 * any.
	 */
	public void closeMenus() {
		if (menuIsOpen) {
			player.closeInventory();
		}
	}

	/**
	 * Returns the {@link Button} whose image is on the cursor of a player while
	 * their inventory is open.
	 */
	public Button getButtonOnCursor() {
		return cursor.getButton(0);
	}

	/**
	 * Sets the {@link Button} whose image is on the cursor of a player while their
	 * inventory is open.
	 */
	public void setButtonOnCursor(Button button) {
		cursor.setButton(0, button);
	}

	/**
	 * Returns the {@link Button} held in the off hand of the Player represented by
	 * this profile.
	 */
	public Button getOffHandButton() {
		return offHand.getButton(0);
	}

	public void setOffHandButton(Button button) {
		offHand.setButton(0, button);
	}

	/**
	 * Returns whether the player corresponding to this {@code UIProfile} has any
	 * menu open.
	 */
	public boolean getIsMenuOpen() {
		return menuIsOpen;
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
		menuIsOpen = open;
	}

}
