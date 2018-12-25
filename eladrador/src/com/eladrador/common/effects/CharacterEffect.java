package com.eladrador.common.effects;

import com.eladrador.common.character.AbstractCharacter;

public abstract class CharacterEffect {

	public void apply(AbstractCharacter chara) {
		onApply(chara);
	}

	public void remove(AbstractCharacter chara) {
		onRemove(chara);
	}

	protected abstract void onApply(AbstractCharacter chara);

	protected abstract void onRemove(AbstractCharacter chara);

}
