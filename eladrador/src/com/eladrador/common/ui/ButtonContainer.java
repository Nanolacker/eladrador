package com.eladrador.common.ui;

import java.util.Arrays;

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
		Button oldButton = buttons[index];
		buttons[index] = button;

		ButtonAddress address = new ButtonAddress(this, index);
		if (oldButton != null) {
			oldButton.unregisterAddress(address);
		}
		if (button != null) {
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

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof ButtonContainer)) {
			return false;
		}
		ButtonContainer container = (ButtonContainer) obj;
		return size == container.size && Arrays.equals(buttons, container.buttons);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(buttons);
	}

}
