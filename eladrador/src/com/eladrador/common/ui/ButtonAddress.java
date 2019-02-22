package com.eladrador.common.ui;

import java.util.Objects;

/**
 * Represents a location of a {@link Button}. Stores the {@link ButtonContainer}
 * and the index that the it resides in.
 */
public class ButtonAddress {

	/**
	 * The container that the associated button resides in.
	 */
	private final ButtonContainer container;
	/**
	 * The index of a container that the associated button resides in.
	 */
	private final int index;

	ButtonAddress(ButtonContainer container, int index) {
		this.container = container;
		this.index = index;
	}

	/**
	 * Returns the container that the associated button resides in.
	 */
	public ButtonContainer getContainer() {
		return container;
	}

	/**
	 * Returns the index of a container that the associated button resides in.
	 */
	public int getIndex() {
		return index;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof ButtonAddress)) {
			return false;
		}
		ButtonAddress address = (ButtonAddress) obj;
		return container.equals(address.container) && index == address.index;
	}

	@Override
	public int hashCode() {
		return Objects.hash(container, index);
	}

}
