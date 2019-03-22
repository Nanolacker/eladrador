package com.eladrador.common.character;

import org.bukkit.Location;

public abstract class NonPlayerCharacterImpl extends AbstractCharacter {

	protected NonPlayerCharacterImpl(String name, int level, double maxHealth, Location location) {
		super(name, level, maxHealth, location);
	}

}
