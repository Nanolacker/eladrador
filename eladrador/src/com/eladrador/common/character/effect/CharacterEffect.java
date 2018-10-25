package com.eladrador.common.character.effect;

import com.eladrador.common.character.AbstractCharacter;

public interface CharacterEffect {

	public abstract void apply(AbstractCharacter chara);
	
	public abstract void remove(AbstractCharacter chara);
	
}
