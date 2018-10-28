package com.eladrador.common.utils;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * A static-only class that contains various utilities pertaining to
 * {@code Player}s.
 */
public final class Players {

	/**
	 * Hides the constructor
	 */
	private Players() {
		// not to be instantiated
	}

	/**
	 * Returns whether there is a {@code Player} near an {@code Location}.
	 * 
	 * @param location the {@code Location} from which nearby {@code Player}s will
	 *                 be searched for
	 * @param distX    the maximum distance on the x-axis that a {@code Player} can
	 *                 be from the location
	 * @param distY    the maximum distance on the y-axis that a {@code Player} can
	 *                 be from the location
	 * @param distZ    the maximum distance on the z-axis that a {@code Player} can
	 *                 be from the location
	 */
	public static boolean playerNearby(Location location, double distX, double distY, double distZ) {
		World world = location.getWorld();
		Collection<Entity> nearbyEntities = world.getNearbyEntities(location, distX, distY, distZ);
		if (!nearbyEntities.isEmpty()) {
			for (Entity entity : nearbyEntities) {
				if (entity instanceof Player) {
					return true;
				}
			}
		}
		return false;
	}

}
