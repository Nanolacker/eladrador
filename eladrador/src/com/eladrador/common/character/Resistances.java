package com.eladrador.common.character;

import java.util.HashMap;

public class Resistances {

	private HashMap<DamageType, Double> resistanceMap;

	public Resistances() {
		resistanceMap = new HashMap<>();
		for (DamageType damageType : DamageType.values()) {
			resistanceMap.put(damageType, 0.0);
		}
	}

	public double getResistance(DamageType damageType) {
		return resistanceMap.get(damageType);
	}

	public void setResistance(DamageType damageType, double resistance) {
		resistanceMap.put(damageType, resistance);
	}

}
