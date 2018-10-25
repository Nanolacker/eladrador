package com.eladrador.common.item;

import com.eladrador.common.GColor;

/**
 * Represents the rarity and value of a GItemStack. The color of the title of a
 * GitemStack's image is determined by such.
 * 
 */
public enum ItemQuality {
	POOR(GColor.ITEM_QUALITY_POOR), COMMON(GColor.ITEM_QUALITY_COMMON), UNCOMMON(GColor.ITEM_QUALITY_UNCOMMON), RARE(
			GColor.ITEM_QUALITY_RARE), EPIC(GColor.ITEM_QUALITY_EPIC), LEGENDARY(
					GColor.ITEM_QUALITY_LEGENDARY), DIVINE(GColor.ITEM_QUALITY_DIVINE);

	private GColor color;

	private ItemQuality(GColor color) {
		this.color = color;
	}

	public GColor getColor() {
		return color;
	}

	@Override
	public String toString() {
		switch (this) {
		case POOR:
			return "Poor";
		case COMMON:
			return "Common";
		case UNCOMMON:
			return "Uncommon";
		case RARE:
			return "Rare";
		case EPIC:
			return "Epic";
		case LEGENDARY:
			return "Legendary";
		case DIVINE:
			return "Divine";
		default:
			return null;
		}
	}

}
