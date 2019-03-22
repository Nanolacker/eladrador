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
public class PlayerBackground implements Listener {

	private static final HashMap<String, PlayerBackground> BACKGROUND_MAP = new HashMap<>();

	private String name;
	private ChatColor displayColor;
	private Zone startZone;
	private Location startLoc;

	public PlayerBackground(String name, Zone startZone, Location startLoc) {
		this.name = name;
		this.startZone = startZone;
		this.startLoc = startLoc;
		BACKGROUND_MAP.put(name, this);
	}

	public static PlayerBackground forName(String name) {
		return BACKGROUND_MAP.get(name);
	}

	public String getName() {
		return name;
	}

	public Zone getStartZone() {
		return startZone;
	}

	public Location getStartLocation() {
		return startLoc;
	}

}
