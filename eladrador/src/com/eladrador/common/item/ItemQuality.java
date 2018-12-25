package com.eladrador.common.item;

import org.bukkit.ChatColor;

/**
 * Represents the rarity and value of an item. The color of the title of an
 * item's image is determined by such.
 * 
 */
public enum ItemQuality {
	POOR(ChatColor.GRAY), COMMON(ChatColor.WHITE), UNCOMMON(ChatColor.GREEN), RARE(ChatColor.BLUE),
	EPIC(ChatColor.DARK_PURPLE), LEGENDARY(ChatColor.GOLD), DIVINE(ChatColor.AQUA);

	private ChatColor color;

	private ItemQuality(ChatColor color) {
		this.color = color;
	}

	public ChatColor getColor() {
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
