package com.eladrador.common.ui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * A container of buttons with which players can interact. Superclass for all
 * menus.
 */
public abstract class AbstractMenu {

	/**
	 * The image of this menu (i.e. the Minecraft inventory counterpart through
	 * which players can interact).
	 */
	protected Inventory image;
	/**
	 * The size of this menu, or number of buttons that can fit.
	 */
	protected int size;
	/**
	 * The buttons that reside within this menu.
	 */
	protected Button[] buttons;

	/**
	 * Constructs a new menu with the specified size.
	 */
	AbstractMenu(Inventory image) {
		this.image = image;
		this.size = image.getSize();
		buttons = new Button[size];
	}

	/**
	 * Returns the size of this menu, or number of buttons that can fit.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Returns the button that resides within this menu at the specified index.
	 * Returns {@code null} if there is none.
	 */
	public Button getButton(int index) {
		return buttons[index];
	}

	/**
	 * Sets the specified button to this menu at the specified index and updates
	 * this menu's images accordingly.
	 * 
	 * @throws IllegalArgumentException if the specified index is less than 0, or if
	 *                                  the specified index is equal to or greater
	 *                                  than the size of this menu
	 */
	public void setButton(int index, Button button) {
		if (index < 0) {
			throw new IllegalArgumentException(
					"Cannot set a button at that index (" + index + "). The index must be greater than 0");
		}
		if (index >= size) {
			throw new IllegalArgumentException("Cannot set a button at that index (" + index
					+ "). The index must be lower than this menu's size: " + size);
		}
		buttons[index] = button;
		ButtonAddress address = new ButtonAddress(this, index);
		button.registerAddress(address);
		ItemStack buttonImage = button.image();
		image.setItem(index, buttonImage);
	}

	/**
	 * Removes the button at the specified index of this menu, if there is any.
	 * 
	 * @throws IllegalArgumentException if the specified index is less than 0, or if
	 *                                  the specified index is equal to or greater
	 *                                  than the size of this menu
	 */
	public void removeButton(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("Cannot attempt to remove a button at that index (" + index
					+ "). The index must be greater than 0");
		}
		if (index >= size) {
			throw new IllegalArgumentException("Cannot attempt to remove a button at that index (" + index
					+ "). The index must be lower than this menu's size: " + size);
		}
		Button button = buttons[index];
		if (button != null) {
			buttons[index] = null;
			ButtonAddress address = new ButtonAddress(this, index);
			button.unregisterAddress(address);
			image.setItem(index, null);
		}
	}

	/**
	 * Returns the image associated with this menu (i.e. the Minecraft inventory
	 * counterpart through which the player can interact with this menu).
	 */
	Inventory getImage() {
		return image;
	}

	/**
	 * Populates this menu's image with item stacks.
	 */
	protected void populateImage() {
		for (int i = 0; i < buttons.length; i++) {
			Button button = buttons[i];
			if (button != null) {
				ItemStack buttonImage = button.image();
				image.setItem(i, buttonImage);
			}
		}
	}

}
