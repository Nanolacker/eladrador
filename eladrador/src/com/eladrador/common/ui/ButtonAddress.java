package com.eladrador.common.ui;

public class ButtonAddress {

	private AbstractMenu menu;
	private int index;

	ButtonAddress(AbstractMenu menu, int index) {
		this.menu = menu;
		this.index = index;
	}

	public AbstractMenu getMenu() {
		return menu;
	}

	public int getIndex() {
		return index;
	}

}
