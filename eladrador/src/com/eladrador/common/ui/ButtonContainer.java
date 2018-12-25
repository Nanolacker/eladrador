package com.eladrador.common.ui;

import java.util.ArrayList;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractMenu {

	protected int size;
	protected Button[] buttons;
	protected ArrayList<Inventory> images;

	public AbstractMenu(int size) {
		this.size = size;
		buttons = new Button[size];
		images = new ArrayList<Inventory>();
	}

	public int getSize() {
		return size;
	}

	public Button getButton(int index) {
		return buttons[index];
	}

	public void addButton(Button button, int index) {
		if (index >= size) {
			throw new IllegalArgumentException("Cannot add a button at that index (" + index
					+ "). It must be added at an index lower than " + size);
		}
		buttons[index] = button;
		ButtonAddress address = new ButtonAddress(this, index);
		button.addAddress(address);
		ItemStack buttonImage = button.getNewImage();
		for (int i = 0; i < images.size(); i++) {
			Inventory image = images.get(i);
			image.setItem(index, buttonImage);
		}
	}

	public void removeButton(int index) {
		Button button = buttons[index];
		if (button == null) {
			try {
				throw new IllegalArgumentException("No button found at index " + index);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		} else {
			buttons[index] = null;
			ButtonAddress address = new ButtonAddress(this, index);
			button.removeAddress(address);
		}
	}

	protected ArrayList<Inventory> getImages() {
		return images;
	}

	protected void registerImage(Inventory image) {
		images.add(image);
	}

	protected void unregisterImage(Inventory image) {
		images.remove(image);
	}

}
