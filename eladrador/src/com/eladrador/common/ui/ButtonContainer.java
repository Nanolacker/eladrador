package com.eladrador.common.ui;

public abstract class ButtonContainer {

	protected int size;
	protected Button[] buttons;

	ButtonContainer(int size) {
		this.size = size;
		buttons = new Button[size];
	}

	public Button getButton(int index) {
		validateIndex(index);
		return buttons[index];
	}

	public void setButton(int index, Button button) {
		validateIndex(index);
		buttons[index] = button;

		Button oldButton = buttons[index];
		if (oldButton != null) {
			ButtonAddress address = new ButtonAddress(this, index);
			oldButton.unregisterAddress(address);
		}
		if (button != null) {
			ButtonAddress address = new ButtonAddress(this, index);
			button.registerAddress(address);
		}
		updateButtonImage(index);
	}

	/**
	 * Returns the size of this container, or number of buttons that can fit.
	 */
	public int getSize() {
		return size;
	}

	abstract void updateButtonImage(int index);

	protected void validateIndex(int index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException("Negative index (" + index + ")");
		} else if (index >= size) {
			throw new IndexOutOfBoundsException("Index too high (" + index + ")");
		}
	}

}
