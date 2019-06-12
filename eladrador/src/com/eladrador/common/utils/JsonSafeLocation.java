package com.eladrador.common.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * An immutable representation of a location that is safe to use with JSON.
 */
public final class JsonSafeLocation {

	private final String worldName;
	private final double x, y, z;
	private final float yaw, pitch;

	/**
	 * Creates a representation of a location that is safe to use with JSON that is
	 * based on the specified Location.
	 */
	public JsonSafeLocation(Location location) {
		worldName = location.getWorld().getName();
		x = location.getX();
		y = location.getY();
		z = location.getZ();
		yaw = location.getYaw();
		pitch = location.getPitch();
	}

	public Location toLocation() {
		World world = Bukkit.getWorld(worldName);
		return new Location(world, x, y, z, yaw, pitch);
	}

}
