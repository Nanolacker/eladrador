package com.eladrador.common.character;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import com.eladrador.common.AbstractGameManager;
import com.eladrador.common.GPlugin;

/**
 * Contains a number of utilities pertaining to characters.
 */
public final class Characters {

	private static AbstractGameManager gameManager;

	static {
		gameManager = GPlugin.getGameManager();
	}

	private Characters() {
		// not to be instantiated
	}

	/**
	 * 
	 * @param loc
	 * @param distX
	 * @param distY
	 * @param distZ
	 * @return
	 */
	public static ArrayList<AbstractCharacter> nearbyCharacters(Location loc, double distX, double distY,
			double distZ) {
		ArrayList<AbstractCharacter> nearbyChars = new ArrayList<AbstractCharacter>();
		World world = loc.getWorld();
		Collection<Entity> nearbyEntities = world.getNearbyEntities(loc, distX, distY, distZ);
		if (!nearbyEntities.isEmpty()) {
			for (Entity e : nearbyEntities) {
				AbstractCharacter character = gameManager.charaByEntity(e);
				if (character != null) {
					nearbyChars.add(character);
				}
			}
		}
		return nearbyChars;
	}
}
