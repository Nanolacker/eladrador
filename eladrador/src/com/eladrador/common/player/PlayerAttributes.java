package com.eladrador.common.player;

import java.io.Serializable;
import java.util.HashMap;

public class PlayerCharacterAttributes implements Serializable {

	private static final long serialVersionUID = -8139934939139984475L;

	public static enum PlayerCharacterAttribute {
		STRENGTH, INTELLIGENCE, DEXTERITY, VITALITY, PIETY
	}

	private final HashMap<PlayerCharacterAttribute, Double> attributeMap;

	PlayerCharacterAttributes() {
		attributeMap = new HashMap<PlayerCharacterAttribute, Double>();
	}

	public double getValue(PlayerCharacterAttribute attribute) {
		return attributeMap.get(attribute);
	}

}
