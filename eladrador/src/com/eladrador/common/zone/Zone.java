package com.eladrador.common;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.eladrador.common.character.AbstractCharacter;
import com.eladrador.common.character.PlayerCharacter;
import com.eladrador.common.collision.AABB;

import net.md_5.bungee.api.ChatColor;

/**
 * Represents an area of interest within game. Size can range from that of small
 * bandit camps to that of continents.
 */
public abstract class Zone {

	private World world;
	private String name;
	private int level;
	private ChatColor displayColor;

	protected Zone(World world, String name, int level, ChatColor displayColor) {
		this.world = world;
		this.name = name;
		this.level = level;
		this.displayColor = displayColor;
	}

	protected void registerBoundaryComponent(double xMin, double xMax, double yMin, double yMax, double zMin,
			double zMax) {
		ZoneBoundaryComponent component = new ZoneBoundaryComponent(this, xMin, xMax, yMin, yMax, zMin, zMax);
	}

	public void displayEntranceMessage(PlayerCharacter pc) {
		Player p = pc.getBukkitPlayer();
		String msg = GColor.LEVEL_INDICATION + "Lv. " + GColor.LEVEL_NUMBER + level + " " + displayColor + name;
	}

	public World getWorld() {
		return world;
	}

	public String getName() {
		return name;
	}

}

class ZoneBoundaryComponent extends AABB {

	private Zone zone;

	/**
	 * Constructs a new axis-aligned bounding box that sets a player's zone when
	 * they enter. Nothing happens when the player exits. The maximum value on any
	 * axis must be greater than the minimum value on said axis for proper function.
	 * 
	 * @param world the world this box will exist in
	 * @param xMin  the minimum x value that exists within this box
	 * @param xMax  the maximum x value that exists within this box
	 * @param yMin  the minimum y value that exists within this box
	 * @param yMax  the maximum y value that exists within this box
	 * @param zMin  the minimum z value that exists within this box
	 * @param zMax  the maximum z value that exists within this box
	 */
	ZoneBoundaryComponent(Zone zone, double xMin, double xMax, double yMin, double yMax, double zMin, double zMax) {
		super(zone.getWorld(), xMin, xMax, yMin, yMax, zMin, zMax);
		this.zone = zone;
	}

	@Override
	protected void onCollisionEnter(AABB other) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onCollisionExit(AABB other) {
		// nothing
	}

}
