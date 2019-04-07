package com.eladrador.common.player;

import com.eladrador.common.character.PlayerCharacter;

/**
 * Denotes an object that can be interacted with by a player character. Usually
 * implemented by colliders.
 */
public interface PlayerCharacterInteractable {

	public void onInteract(PlayerCharacter pc);

}
