package com.eladrador.common.ui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class OffHand extends ButtonContainer {

	private Player player;

	OffHand(Player player) {
		super(1);
		this.player = player;
	}

	@Override
	void updateButtonImage(int index) {
		validateIndex(index);
		Button button = buttons[0];
		ItemStack image = button == null ? null : button.image();
		player.getInventory().setItemInOffHand(image);
	}

}
