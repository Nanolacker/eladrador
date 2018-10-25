package com.eladrador.common.ui;

import org.bukkit.event.inventory.ClickType;

public enum ButtonToggleType {
	/**
	 * e.g. left-clicking
	 */
	STANDARD,
	/**
	 * e.g. right-clicking
	 */
	SPECIAL,
	/**
	 * left-clicking with an item in hand, outside of inventory view
	 */
	IN_HAND,
	/**
	 * shift-clicking in inventory
	 */
	SHIFT_CLICK,
	/**
	 * e.g. pressing 'q'
	 */
	DISCARD;

	public static ButtonToggleType byClickType(ClickType clickType) {
		switch (clickType) {
		case CONTROL_DROP:
			return null;
		case CREATIVE:
			return null;
		case DOUBLE_CLICK:
			return STANDARD;
		case DROP:
			return null;
		case LEFT:
			return STANDARD;
		case MIDDLE:
			return null;
		case NUMBER_KEY:
			return null;
		case RIGHT:
			return SPECIAL;
		case SHIFT_LEFT:
			return SHIFT_CLICK;
		case SHIFT_RIGHT:
			return SHIFT_CLICK;
		case UNKNOWN:
			return null;
		case WINDOW_BORDER_LEFT:
			return null;
		case WINDOW_BORDER_RIGHT:
			return null;
		default:
			return null;
		}
	}

}
