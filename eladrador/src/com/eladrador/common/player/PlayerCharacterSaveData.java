package com.eladrador.common.player;

import java.io.Serializable;
import java.util.HashMap;

import org.bukkit.Location;

import com.eladrador.common.quest.persistence.QuestState;

/**
 * A class used for storing PlayerCharacter data.
 */
public final class PlayerCharacterSaveData implements Serializable {

	private static final long serialVersionUID = -5372132800050348883L;

	public final String name;
	public final int saveSlot;
	public final int playerBackgroundID;
	public final int playerClassID;
	public final HashMap<Integer, QuestState> questStateMap;
	public final int zoneID;

	// stats
	public final double xp;
	public final double maxHealth;
	public final double currentHealth;
	public final double maxMana;
	public final double currentMana;
	public final PlayerCharacterAttributes attributes;

	// location
	public final String worldName;
	public final double x, y, z;
	public final float yaw, pitch;

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
