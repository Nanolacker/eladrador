package com.eladrador.common.ui;

/**
 * Represents a location of a {@link Button}. Stores the {@link AbstractMenu}
 * and the index that the it resides in.
 */
public class ButtonAddress {

	/**
	 * The container that the associated button resides in.
	 */
	private AbstractMenu container;
	/**
	 * The index of a container that the associated button resides in.
	 */
	private int index;

	ButtonAddress(AbstractMenu menu, int index) {
		this.container = menu;
		this.index = index;
	}

	/**
	 * Returns the menu that the associated button resides in.
	 */
	public AbstractMenu getMenu() {
		return container;
	}

	/**
	 * Returns the index of a container that the associated button resides in.
	 */
	public int getIndex() {
		return index;
	}

}
