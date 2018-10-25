package com.eladrador.common.player;

/**
 * Represents an ability unlockable and usable by a player, in either a passive
 * or active form.
 * 
 */
public abstract class Skill {

	PlayerClass playerClass;
	private int minLvl;

	protected Skill(PlayerClass playerClass, int minLvl) {
		this.playerClass = playerClass;
		this.minLvl = minLvl;
	}

	public PlayerClass getPlayerClass() {
		return playerClass;
	}

	public int getMinimumLevel() {
		return minLvl;
	}

}
