package com.eladrador.common.effects;

import com.eladrador.common.character.AbstractCharacter;
import com.eladrador.common.scheduling.DelayedTask;

public abstract class TimedCharacterEffect extends CharacterEffect {

	private double duration;

	protected TimedCharacterEffect(double duration) {
		this.duration = duration;
	}

	@Override
	public void apply(AbstractCharacter chara) {
		DelayedTask stopTask = new DelayedTask(duration) {

			@Override
			public void run() {
				onRemove(chara);
			}

		};
		stopTask.start();
	}

}
