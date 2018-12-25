package com.eladrador.common.ui;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.eladrador.common.GPlugin;

/**
 * Represents a menu whose image occupies the top part of the inventory view.
 */
public class UpperMenu extends ButtonContainer {

	/**
	 * The title of this menu's images.
	 */
	private String title;

	/**
	 * Constructs a new upper menu with the specified title and size.
	 */
	public UpperMenu(String title, UpperMenuSize size) {
		super(size.getValue());
		this.title = title;
	}

	/**
	 * Returns a fresh image representing this menu.
	 */
	Inventory getNewImage() {
		Server server = GPlugin.getBukkitServer();
		Inventory image = server.createInventory(null, size, title);
		for (int i = 0; i < buttons.length; i++) {
			Button button = buttons[i];
			if (button != null) {
				ItemStack buttonImage = button.getNewImage();
				image.setItem(i, buttonImage);
			}
		}
		return image;
	}

	/**
	 * Override to handle the player closing the menu. Invoked when the player
	 * closes the menu.
	 */
	protected void onClose(Player player) {
		// override this (optional)
	}

	/**
	 * Wraps numbers that are multiples of 9 and less than or equal to 54 that
	 * represent the possible sizes of a {@link UpperMenu}.
	 */
	public enum UpperMenuSize {
		
		NINE(9), EIGHTEEN(18), TWENTY_SEVEN(27), THIRTY_SIX(36), FORTY_FIVE(45), FIFTY_FOUR(54);

		/**
		 * The {@code int} value of this size.
		 */
		private int value;

		private UpperMenuSize(int value) {
			this.value = value;
		}

		/**
		 * Returns the {@code int} value of this size.
		 */
		int getValue() {
			return value;
		}

	}

}
