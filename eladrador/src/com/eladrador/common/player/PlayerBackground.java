package com.eladrador.common.player;

import org.bukkit.Location;
import org.bukkit.event.Listener;

import com.eladrador.common.Zone;

/**
 * Represents where a PlayerCharacter comes from in the world and what perks
 * they have as a result.
 *
 */
public abstract class PlayerBackground implements Listener {

	private String name;
	private Location startLoc;
	private Zone startZone;

	protected PlayerBackground(String name, Location startLoc) {
		this.name = name;
		this.startLoc = startLoc;
	}

	public String getName() {
		return name;
	}

	public Location getStartLocation() {
		return startLoc;
	}

	public Zone getStartZone() {
		return startZone;
	}

}