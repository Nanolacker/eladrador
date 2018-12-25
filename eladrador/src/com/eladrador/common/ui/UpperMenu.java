package com.eladrador.common.ui;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.eladrador.common.GPlugin;

/**
 * Represents a menu whose image occupies the top part of the inventory view
 * when opened.
 */
public class TopMenu extends AbstractMenu {

	private String title;

	public TopMenu(String title, TopMenuSize size) {
		super(size.getValue());
		this.title = title;
	}

	public void open(Player player) {
		Inventory image = getNewImage();
		player.openInventory(image);
		registerImage(image);
		UIProfile profile = UIListener.profiles.get(player);
		profile.setTopMenuOpen(this);
	}

	private Inventory getNewImage() {
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
	 * Override to handle the player closing the menu. Called when the player closes
	 * the menu.
	 */
	public void onClose(Player player) {
		// override this (optional)
	}

	public enum TopMenuSize {
		NINE(9), EIGHTEEN(18), TWENTY_SEVEN(27), THIRTY_SIX(36), FORTY_FIVE(45), FIFTY_FOUR(54);

		private int value;

		private TopMenuSize(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

	}

}
