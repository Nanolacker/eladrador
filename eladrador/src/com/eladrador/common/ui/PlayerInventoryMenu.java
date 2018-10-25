package com.eladrador.common.ui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PlayerInventoryMenu extends AbstractMenu {

	private Inventory image;

	public PlayerInventoryMenu(Player player) {
		super(41);
		image = player.getInventory();
		images.add(image);
	}

	@Override
	public void addButton(Button button, int index) {
		super.addButton(button, index);
		image.setItem(index, button.getNewImage());
	}

	@Override
	protected void registerImage(Inventory image) {
		throw new RuntimeException("Images cannot be registered to a " + getClass().getName() + ".");
	}

	@Override
	protected void unregisterImage(Inventory image) {
		throw new RuntimeException("Images cannot be unregistered from a " + getClass().getName() + ".");
	}

}
