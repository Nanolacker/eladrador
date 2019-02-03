package com.eladrador.common.ui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Represents a menu whose image occupies the lower part of an inventory view
 * (i.e. the player's inventory).
 */
public class LowerMenu extends AbstractMenu {

	/**
	 * Constructs a new lower menu for the specified player.
	 */
	LowerMenu(Player player) {
		super(player.getInventory());
	}

}
