package com.eladrador.common.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import com.eladrador.common.GPlugin;

/**
 * Represents a menu whose image occupies the top part of the inventory view.
 */
public class UpperMenu extends AbstractMenu {

	/**
	 * 
	 * /** Constructs a new upper menu with the specified title and size.
	 */
	public UpperMenu(String title, int size) {
		super(GPlugin.getBukkitServer().createInventory(null, size, title));
		populateImage();
	}

	/**
	 * Constructs a new upper menu with the specified title, type, and size.
	 */
	public UpperMenu(String title, InventoryType inventoryType) {
		super(GPlugin.getBukkitServer().createInventory(null, inventoryType, title));
		populateImage();
	}

	/**
	 * Override to handle the player closing the menu. Invoked when the player
	 * closes the menu.
	 */
	protected void onClose(Player player) {
		// override this (optional)
	}

}
