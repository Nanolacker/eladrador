package com.eladrador.common.item.types;

import com.eladrador.common.character.PlayerCharacter;

/**
 * Interface for GItemStacks with which a player can perform an action.
 *
 */
public interface ItemActionable {

	/**
	 * Called when the owner PlayerCharacter executes the action associated with
	 * this GItemStack.
	 */
	public void onActionUse(PlayerCharacter owner);

	/**
	 * The description of the action associated with this GItemStack.
	 */
	public abstract String getActionDescription(PlayerCharacter owner);

}
