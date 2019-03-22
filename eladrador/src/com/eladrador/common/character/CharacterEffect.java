package com.eladrador.common.character;

import character.IGameCharacter;

public interface CharacterEffect {

	public void onApply(IGameCharacter character);

	public void onRemove(IGameCharacter character);

}
