package com.eladrador.common.item;

public enum GameItemType {

	CONSUMABLE, QUEST, SHORT_SWORD, ARMOR_FEET, ARMOR_LEGS, ARMOR_CHEST, ARMOR_HEAD;

	public boolean canBeInMainHand() {
		switch (this) {
		case SHORT_SWORD:
			return true;
		default:
			return false;
		}
	}

	public boolean canBeUsedInOffHand() {
		switch (this) {
		case CONSUMABLE:
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

	@Override
	public String toString() {
		switch (this) {
		case ARMOR_CHEST:
			return "Chest";
		case ARMOR_FEET:
			return "Feet";
		case ARMOR_HEAD:
			return "Head";
		case ARMOR_LEGS:
			return "Legs";
		case CONSUMABLE:
			return "Consumable";
		case QUEST:
			return "Quest Item";
		case SHORT_SWORD:
			return "Short Sword";
		default:
			return null;
		}
	}

}
