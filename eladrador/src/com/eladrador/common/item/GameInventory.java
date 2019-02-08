package com.eladrador.common.item;

import com.eladrador.common.ui.AbstractMenu;
import com.eladrador.common.ui.Button;

public class GameInventory {

	private AbstractMenu menu;

	public GameInventory(AbstractMenu menu) {
		this.menu = menu;
	}

	public GameItem getItem(int index) {
		validateIndex(index);
		Button button = menu.getButton(index);
		if (button instanceof GameItemButton) {
			return ((GameItemButton) button).getItem();
		}
		return null;
	}

	public void addItem(GameItem item) {
		// do this
	}

	public void setItem(int index, GameItem item) {
		validateIndex(index);
		GameItemButton button = new GameItemButton(item);
		menu.setButton(index, button);
	}

	private void validateIndex(int index) {
		int size = menu.getSize();
		if (index < 0) {
			throw new IndexOutOfBoundsException("Index (" + index + ") negative");
		} else if (index >= size) {
			throw new IndexOutOfBoundsException(
					"Index (" + index + ") greater than this inventory's size (" + size + ")");
		}
	}

}
