package com.eladrador.common.ui;

import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;

/**
 * A way in which a button can be toggled.
 */
public enum ButtonToggleType {
	/**
	 * Left-clicking with button in hand outside of menu.
	 */
	LEFT_CLICK_IN_HAND,
	/**
	 * Right-clicking with button in hand outside of menu.
	 */
	RIGHT_CLICK_IN_HAND,
	/**
	 * Tapping 'Q' with the button in the offhand slot.
	 */
	TAP_Q_IN_MAIN_HAND,
	/**
	 * Left-clicking in menu.
	 */
	LEFT_CLICK_IN_MENU,
	/**
	 * Right-clicking in menu.
	 */
	RIGHT_CLICK_IN_MENU,
	/**
	 * Left-clicking in menu while holding down shift.
	 */
	SHIFT_LEFT_CLICK_IN_MENU,
	/**
	 * Right-clicking in menu while holding down shift.
	 */
	SHIFT_RIGHT_CLICK_IN_MENU,
	/**
	 * Left-clicking over a menu slot when the button is on the cursor.
	 */
	LEFT_CLICK_ON_CURSOR,
	/**
	 * Right-clicking over a menu slot when the button is on the cursor.
	 */
	RIGHT_CLICK_ON_CURSOR,
	/**
	 * Tapping the associated number key to toggle a button.
	 */
	HOTBAR;

	/**
	 * Returns the {@link ButtonToggleType} that corresponds to the specified
	 * {@link ClickType} when a {@link Button}'s image is clicked in a menu.
	 * 
	 * @param clickType how the {@code Button}'s image is clicked
	 * @return the appropriate {@code ButtonToggleType}
	 */
	static ButtonToggleType forClickTypeInMenu(ClickType clickType) {
		switch (clickType) {
		case CONTROL_DROP:
			return null;
		case CREATIVE:
			return LEFT_CLICK_IN_MENU;
		case DOUBLE_CLICK:
			return LEFT_CLICK_IN_MENU;
		case DROP:
			return null;
		case LEFT:
			return LEFT_CLICK_IN_MENU;
		case MIDDLE:
			return null;
		case NUMBER_KEY:
			return null;
		case RIGHT:
			return RIGHT_CLICK_IN_MENU;
		case SHIFT_LEFT:
			return SHIFT_LEFT_CLICK_IN_MENU;
		case SHIFT_RIGHT:
			return SHIFT_RIGHT_CLICK_IN_MENU;
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

	/**
	 * Returns the {@link ButtonToggleType} that corresponds to the specified
	 * {@link ClickType} when a player clicks while a {@link Button}'s image is on
	 * their cursor.
	 * 
	 * @param clickType how the player clicked
	 * @return the appropriate {@code ButtonToggleType}
	 */
	static ButtonToggleType forClickTypeOnCursor(ClickType clickType) {
		switch (clickType) {
		case CONTROL_DROP:
			return null;
		case CREATIVE:
			return LEFT_CLICK_ON_CURSOR;
		case DOUBLE_CLICK:
			return LEFT_CLICK_ON_CURSOR;
		case DROP:
			return null;
		case LEFT:
			return LEFT_CLICK_ON_CURSOR;
		case MIDDLE:
			return null;
		case NUMBER_KEY:
			return null;
		case RIGHT:
			return RIGHT_CLICK_ON_CURSOR;
		case SHIFT_LEFT:
			// shift clicking acts wonky, will maybe add support later
			return null;
		case SHIFT_RIGHT:
			// shift clicking acts wonky, will maybe add support later
			return null;
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

	static ButtonToggleType forDragTypeOnCursor(DragType dragType) {
		switch (dragType) {
		case EVEN:
			return LEFT_CLICK_ON_CURSOR;
		case SINGLE:
			return RIGHT_CLICK_ON_CURSOR;
		default:
			return null;
		}
	}

	/**
	 * Returns the {@link ButtonToggleType} that corresponds to the specified
	 * {@link Action}.
	 * 
	 * @param action the type of action performed
	 * @return the appropriate {@code ButtonToggleType}
	 */
	static ButtonToggleType forAction(Action action) {
		ButtonToggleType toggleType;
		switch (action) {
		case LEFT_CLICK_AIR:
			toggleType = ButtonToggleType.LEFT_CLICK_IN_HAND;
			break;
		case LEFT_CLICK_BLOCK:
			toggleType = ButtonToggleType.LEFT_CLICK_IN_HAND;
			break;
		case PHYSICAL:
			toggleType = null;
			break;
		case RIGHT_CLICK_AIR:
			toggleType = ButtonToggleType.RIGHT_CLICK_IN_HAND;
			break;
		case RIGHT_CLICK_BLOCK:
			toggleType = ButtonToggleType.RIGHT_CLICK_IN_HAND;
			break;
		default:
			toggleType = null;
			break;
		}
		return toggleType;
	}

}
