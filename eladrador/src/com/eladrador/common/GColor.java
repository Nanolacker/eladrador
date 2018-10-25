package com.eladrador.common;

import org.bukkit.ChatColor;

public enum GColor {
	/**
	 * Color for indicating that the level of something is being displayed ("Lv.").
	 */
	LEVEL_INDICATION(ChatColor.GOLD),
	/**
	 * Color for displaying the level of something.
	 */
	LEVEL_NUMBER(ChatColor.GOLD),
	/**
	 * Color for displaying the name of a player character.
	 */
	PLAYER_CHARACTER_NAME(ChatColor.BLUE),
	/**
	 * Color for displaying the name of a friendly character.
	 */
	FRIENDLY_CHARACTER_NAME(ChatColor.GREEN),
	/**
	 * Color for displaying the name of a neutral character.
	 */
	NEUTRAL_CHARACTER_NAME(ChatColor.GRAY),
	/**
	 * Color for displaying the name of an enemy character.
	 */
	ENEMY_CHARACTER_NAME(ChatColor.RED),
	/**
	 * Color for poor items.
	 */
	ITEM_QUALITY_POOR(ChatColor.GRAY),
	/**
	 * Color for common items.
	 */
	ITEM_QUALITY_COMMON(ChatColor.WHITE),
	/**
	 * Color for uncommon items.
	 */
	ITEM_QUALITY_UNCOMMON(ChatColor.GREEN),
	/**
	 * Color for rare items.
	 */
	ITEM_QUALITY_RARE(ChatColor.BLUE),
	/**
	 * Color for epic items.
	 */
	ITEM_QUALITY_EPIC(ChatColor.LIGHT_PURPLE),
	/**
	 * Color for legendary items.
	 */
	ITEM_QUALITY_LEGENDARY(ChatColor.GOLD),
	/**
	 * Color for divine items.
	 */
	ITEM_QUALITY_DIVINE(ChatColor.AQUA);

	private ChatColor bukkitColor;

	private GColor(ChatColor bukkitColor) {
		this.bukkitColor = bukkitColor;
	}

	public ChatColor getBukkitColor() {
		return bukkitColor;
	}

	@Override
	public String toString() {
		return bukkitColor.toString();
	}

}
