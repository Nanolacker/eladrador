package com.eladrador.common.player;

import java.io.Serializable;
import java.util.HashMap;

import org.bukkit.Location;

import com.eladrador.common.quest.Quest;
import com.eladrador.common.quest.QuestStatus;

/**
 * A class used for storing PlayerCharacter data.
 */
public final class PlayerCharacterSaveData implements Serializable {

	private static final long serialVersionUID = -5372132800050348883L;

	private final String playerName;
	private final int saveSlot;
	private final String playerBackgroundName;
	private final String playerClassName;
	private final HashMap<String, QuestStatus> questStateMap;
	private final String zoneName;

	// stats
	private final double xp;
	private final double maxHealth;
	private final double currentHealth;
	private final double maxMana;
	private final double currentMana;
	private final PlayerAttributes attributes;

	// location
	private final String worldName;
	private final double locX, locY, locZ;
	private final float yaw, pitch;

	public PlayerCharacterSaveData(String playerName, int saveSlot, String playerBackgroundName, String playerClassName,
			HashMap<String, QuestStatus> questStatusMap, String zoneName, double xp, double maxHealth,
			double currentHealth, double maxMana, double currentMana, PlayerAttributes attributes, Location location) {
		this.playerName = playerName;
		this.saveSlot = saveSlot;
		this.playerBackgroundName = playerBackgroundName;
		this.playerClassName = playerClassName;
		this.questStateMap = questStatusMap;
		this.zoneName = zoneName;
		this.xp = xp;

		this.maxHealth = maxHealth;
		this.currentHealth = currentHealth;
		this.maxMana = maxMana;
		this.currentMana = currentMana;
		this.attributes = attributes;

		worldName = location.getWorld().getName();
		locX = location.getX();
		locY = location.getY();
		locZ = location.getZ();
		yaw = location.getYaw();
		pitch = location.getPitch();
	}

	public String getPlayerName() {
		return playerName;
	}

	public int getSaveSlot() {
		return saveSlot;
	}

	public String getPlayerBackgroundName() {
		return playerBackgroundName;
	}

	public String getPlayerClassName() {
		return playerClassName;
	}

	public HashMap<String, QuestStatus> getQuestStateMap() {
		return questStateMap;
	}

	public String getZoneName() {
		return zoneName;
	}

	public double getXp() {
		return xp;
	}

	public double getMaxHealth() {
		return maxHealth;
	}

	public double getCurrentHealth() {
		return currentHealth;
	}

	public double getMaxMana() {
		return maxMana;
	}

	public double getCurrentMana() {
		return currentMana;
	}

	public PlayerAttributes getAttributes() {
		return attributes;
	}

	public String getWorldName() {
		return worldName;
	}

	public double getLocX() {
		return locX;
	}

	public double getLocY() {
		return locY;
	}

	public double getLocZ() {
		return locZ;
	}

	public float getYaw() {
		return yaw;
	}

	public float getPitch() {
		return pitch;
	}

}
