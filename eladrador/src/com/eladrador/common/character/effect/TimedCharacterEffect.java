package com.eladrador.common.character.effect;

import com.eladrador.common.character.AbstractCharacter;

public abstract class TimedCharacterEffect implements CharacterEffect{

	private double duration;
	
	protected TimedCharacterEffect(double duration) {
		this.duration = duration;
	}
	
	@Override
	public void apply(AbstractCharacter chara) {
		
	}

	@Override
	public void remove(AbstractCharacter chara) {
		// TODO Auto-generated method stub
		
	}
	
}
