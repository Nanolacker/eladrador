package com.eladrador.common.item;

public enum GameItemType {

	CONSUMABLE, SHORT_SWORD;

	public boolean canBeInMainHand() {
		switch (this) {
		case CONSUMABLE:
			return false;
		case SHORT_SWORD:
			return true;
		default:
			return false;
		}
	}

	public boolean canHaveSpecialUse() {
		switch (this) {
		case CONSUMABLE:
			return true;
		case SHORT_SWORD:
			return true;
		default:
			return false;
		}
	}

}
