package com.eladrador.common.character;

import org.bukkit.Location;

import com.eladrador.common.physics.Collider;

public class CharacterCollider extends Collider {

	private GameCharacter character;

	/**
	 * 
	 * @param center relative to character's location
	 */
	public CharacterCollider(GameCharacter character, Location center, double lengthX, double lengthY, double lengthZ) {
		super(center, lengthX, lengthY, lengthZ);
		this.character = character;
	}

	public GameCharacter getCharacter() {
		return character;
	}

	@Override
	protected void onCollisionEnter(Collider other) {
	}

	@Override
	protected void onCollisionExit(Collider other) {
	}

}
