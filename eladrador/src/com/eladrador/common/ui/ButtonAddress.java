package com.eladrador.common.ui;

/**
 * Represents a location of a {@link Button}. Stores the {@link ButtonContainer}
 * and the index that the it resides in.
 */
public class ButtonAddress {

	/**
	 * The container that the associated button resides in.
	 */
	private ButtonContainer container;
	/**
	 * The index of a container that the associated button resides in.
	 */
	private int index;

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

}
