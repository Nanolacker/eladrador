package com.eladrador.common.ui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Represents a menu whose image occupies the lower part of an inventory view
 * (i.e. the player's inventory).
 */
public class LowerMenu extends ButtonContainer {

	/**
	 * The one and only image of this menu.
	 */
	private Inventory image;

	/**
	 * Constructs a new lower menu for the specified player.
	 */
	LowerMenu(Player player) {
		super(41);
		image = player.getInventory();
		images.add(image);
	}

	@Override
	public void addButton(Button button, int index) {
		super.addButton(button, index);
		image.setItem(index, button.getNewImage());
	}

	/**
	 * A {@link RuntimeException} will be thrown when invoked.
	 */
	@Override
	protected void registerImage(Inventory image) {
		throw new RuntimeException("Images cannot be registered to a " + getClass().getName() + ".");
	}

	/**
	 * A {@link RuntimeException} will be thrown when invoked.
	 */
	@Override
	protected void unregisterImage(Inventory image) {
		throw new RuntimeException("Images cannot be unregistered from a " + getClass().getName() + ".");
	}

}
