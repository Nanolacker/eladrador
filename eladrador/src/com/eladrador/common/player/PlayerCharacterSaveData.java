package com.eladrador.common.player;

import java.io.Serializable;
import java.util.HashMap;

import org.bukkit.Location;

import com.eladrador.common.quest.persistence.QuestState;

final class PlayerCharacterSaveData implements Serializable {

	private static final long serialVersionUID = -5372132800050348883L;

	String name;
	int saveSlot;
	int playerBackgroundID;
	int playerClassID;
	HashMap<Integer, QuestState> questStateMap;
	int zoneID;

	// stats
	double xp;
	double maxHealth;
	double currentHealth;
	double maxMana;
	double currentMana;
	PlayerCharacterAttributes attributes;

	// location
	String worldName;
	double x, y, z;
	float yaw, pitch;

	PlayerCharacterSaveData(String name, int saveSlot, int playerBackgroundID, int playerClassID,
			HashMap<Integer, QuestState> questStateMap, int zoneID, double xp, double maxHealth, double currentHealth,
			double maxMana, double currentMana, PlayerCharacterAttributes attributes, Location location) {
		this.name = name;
		this.saveSlot = saveSlot;
		this.playerBackgroundID = playerBackgroundID;
		this.playerClassID = playerClassID;
		this.questStateMap = questStateMap;
		this.zoneID = zoneID;
		this.xp = xp;

		this.maxHealth = maxHealth;
		this.currentHealth = currentHealth;
		this.maxMana = maxMana;
		this.currentMana = currentMana;
		this.attributes = attributes;

		worldName = location.getWorld().getName();
		x = location.getX();
		y = location.getY();
		z = location.getZ();
		yaw = location.getYaw();
		pitch = location.getPitch();
	}

}
