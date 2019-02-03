package com.eladrador.common.item;

public enum GameItemType {

	CONSUMABLE, SHORT_SWORD;

	public boolean isWeapon() {
		return this == SHORT_SWORD;
	}

}
