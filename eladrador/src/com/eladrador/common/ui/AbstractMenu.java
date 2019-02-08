package com.eladrador.common.ui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractMenu extends ButtonContainer {

	/**
	 * The image of this menu (i.e. the Minecraft inventory counterpart through
	 * which players can interact).
	 */
	protected Inventory image;

	/**
	 * Constructs a new menu with the specified size.
	 */
	AbstractMenu(Inventory image) {
		super(image.getSize());
		this.image = image;
	}

	/**
	 * Returns the image associated with this menu (i.e. the Minecraft inventory
	 * counterpart through which the player can interact with this menu).
	 */
	Inventory getImage() {
		return image;
	}

	@Override
	void updateButtonImage(int index) {
		Button button = buttons[index];
		ItemStack buttonImage = button == null ? null : button.image();
		image.setItem(index, buttonImage);
	}

}
