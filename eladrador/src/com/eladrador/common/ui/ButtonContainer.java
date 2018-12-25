package com.eladrador.common.ui;

import java.util.ArrayList;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * A container for buttons with which players can interact. Superclass for all
 * menus.
 */
public abstract class ButtonContainer {

	/**
	 * The size of this container, or number of buttons that can fit in this
	 * container.
	 */
	protected int size;
	/**
	 * The buttons that reside within this container.
	 */
	protected Button[] buttons;
	/**
	 * The images of this container (i.e. the Minecraft inventory counterparts
	 * through which the player can interact with this container).
	 */
	protected ArrayList<Inventory> images;

	/**
	 * Constructs a new container with the specified size.
	 */
	ButtonContainer(int size) {
		this.size = size;
		buttons = new Button[size];
		images = new ArrayList<Inventory>();
	}

	/**
	 * Returns the size of this container, or number of buttons that can fit in this
	 * container.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Returns the button that resides within this container at the specified index.
	 * Returns {@code null} if there is none.
	 */
	public Button getButton(int index) {
		return buttons[index];
	}

	/**
	 * Adds the specified button to this container at the specified index and
	 * updates this container's images accordingly.
	 * 
	 * @throws IllegalArgumentException if the specified index is less than 0, or if
	 *                                  the specified index is equal to or greater
	 *                                  than the size of this container
	 */
	public void addButton(Button button, int index) {
		if (index < 0) {
			throw new IllegalArgumentException(
					"Cannot add a button at that index (" + index + "). The index must be greater than 0");
		}
		if (index < 0 || index >= size) {
			throw new IllegalArgumentException("Cannot add a button at that index (" + index
					+ "). The index must be lower than this container's size: " + size);
		}
		buttons[index] = button;
		ButtonAddress address = new ButtonAddress(this, index);
		button.registerAddress(address);
		ItemStack buttonImage = button.getNewImage();
		for (int i = 0; i < images.size(); i++) {
			Inventory image = images.get(i);
			image.setItem(index, buttonImage);
		}
	}

	/**
	 * Removes the button at the specified index of this container, if there is any.
	 * 
	 * @throws IllegalArgumentException if the specified index is less than 0, or if
	 *                                  the specified index is equal to or greater
	 *                                  than the size of this container
	 */
	public void removeButton(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("Cannot attempt to remove a button at that index (" + index
					+ "). The index must be greater than 0");
		}
		if (index >= size) {
			throw new IllegalArgumentException("Cannot attempt to remove a button at that index (" + index
					+ "). The index must be lower than this container's size: " + size);
		}
		Button button = buttons[index];
		if (button != null) {
			buttons[index] = null;
			ButtonAddress address = new ButtonAddress(this, index);
			button.unregisterAddress(address);
			for (int i = 0; i < images.size(); i++) {
				Inventory image = images.get(i);
				image.setItem(index, null);
			}
		}
	}

	/**
	 * Returns the images associated with this container (i.e. the Minecraft
	 * inventory counterparts through which the player can interact with this
	 * container).
	 */
	ArrayList<Inventory> getImages() {
		return images;
	}

	/**
	 * Associates the specified inventory image with this container.
	 */
	void registerImage(Inventory image) {
		images.add(image);
	}

	/**
	 * Unassociates the specified inventory image with this container.
	 */
	void unregisterImage(Inventory image) {
		images.remove(image);
	}

}
