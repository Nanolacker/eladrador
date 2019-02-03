package com.eladrador.common.player;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.Listener;

import com.eladrador.common.zone.Zone;

/**
 * Represents the background of a player character and governs certain perks
 * they have as a result.
 *
 */
public abstract class PlayerBackground implements Listener {

	private static final HashMap<Integer, PlayerBackground> BACKGROUND_MAP = new HashMap<Integer, PlayerBackground>();

	private String name;
	private int id;
	private ChatColor displayColor;
	private Zone startZone;
	private Location startLoc;

	protected PlayerBackground(String name, int id, Zone startZone, Location startLoc) {
		this.name = name;
		this.id = id;
		this.startZone = startZone;
		this.startLoc = startLoc;
		BACKGROUND_MAP.put(id, this);
	}

	public static PlayerBackground byID(int id) {
		return BACKGROUND_MAP.get(id);
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return id;
	}

	public Zone getStartZone() {
		return startZone;
	}

	public Location getStartLocation() {
		return startLoc;
	}

}
