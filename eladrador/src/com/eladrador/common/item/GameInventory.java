package com.eladrador.common.item;

import com.eladrador.common.ui.Button;
import com.eladrador.common.ui.ButtonContainer;

public class GameInventory {

	private ButtonContainer buttonContainer;

	public GameInventory(ButtonContainer buttonContainer) {
		this.buttonContainer = buttonContainer;
	}

	public GameItemStack getItemStack(int index) {
		validateIndex(index);
		Button button = buttonContainer.getButton(index);
		if (button instanceof GameItemStackButton) {
			return ((GameItemStackButton) button).getItemStack();
		}
		return null;
	}

	public void addItemStack(GameItem itemStack) {
		// do this
	}

	public void setItem(int index, GameItemStack itemStack) {
		validateIndex(index);
		GameItemStackButton button = itemStack.getButton();
		buttonContainer.setButton(index, button);
	}

	private void validateIndex(int index) {
		int size = buttonContainer.getSize();
		if (index < 0) {
			throw new IndexOutOfBoundsException("Index (" + index + ") negative");
		} else if (index >= size) {
			throw new IndexOutOfBoundsException(
					"Index (" + index + ") greater than this inventory's size (" + size + ")");
		}
	}

}
