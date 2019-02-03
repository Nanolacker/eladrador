package com.eladrador.common.item;

import org.bukkit.entity.Player;

import com.eladrador.common.player.PlayerCharacter;
import com.eladrador.common.ui.AbstractMenu;
import com.eladrador.common.ui.LowerMenu;
import com.eladrador.common.ui.UIProfile;

public class GameInventory {

	private GameItem[] contents;
	private AbstractMenu menu;

	public GameInventory(int size) {
		contents = new GameItem[44];
	}

	public void addItem(GameItem item) {
		for (int i = 0; i < contents.length; i++) {
			if (contents[i] == null) {
				contents[i] = item;
				return;
			}
		}
	}

	public void setItem(int index, GameItem item) {
		// validate arguments

		contents[index] = item;
		GameItemButton button = new GameItemButton(item);
		menu.setButton(index, button);
	}

}
