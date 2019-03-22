package com.eladrador.common.character;

import org.bukkit.Location;

import com.eladrador.common.physics.Collider;

public abstract class CharacterCollider extends Collider {

	private AbstractCharacter character;

	/**
	 * 
	 * @param center relative to character's location
	 */
	public CharacterCollider(AbstractCharacter character, Location center, double lengthX, double lengthY,
			double lengthZ) {
		super(center, lengthX, lengthY, lengthZ);
		this.character = character;
	}

	public AbstractCharacter getCharacter() {
		return character;
	}

}
