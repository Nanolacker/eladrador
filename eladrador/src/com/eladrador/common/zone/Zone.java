package com.eladrador.common.zone;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import com.eladrador.common.character.AbstractCharacter;
import com.eladrador.common.character.CharacterCollider;
import com.eladrador.common.character.PlayerCharacterOLD;
import com.eladrador.common.physics.Collider;

import character.PlayerCharacter;

/**
 * Represents an area of interest within game. Size can range from that of small
 * bandit camps to that of continents.
 */
public class Zone {

	/**
	 * Keys are the zone IDs.
	 */
	private static final HashMap<String, Zone> ZONE_MAP = new HashMap<>();

	private World world;
	private String name;
	private ChatColor displayColor;
	private int level;

	public Zone(World world, String name, ChatColor displayColor, int level, BoundingBox... entranceBoxes) {
		this.world = world;
		this.name = name;
		this.displayColor = displayColor;
		this.level = level;
		for (int i = 0; i < entranceBoxes.length; i++) {
			BoundingBox boundingBox = entranceBoxes[i];
			ZoneEntranceCollider collider = new ZoneEntranceCollider(this, boundingBox);
			collider.setActive(true);
		}
		ZONE_MAP.put(name, this);
	}

	public static Zone forName(String name) {
		return ZONE_MAP.get(name);
	}

	public void displayEntranceMessage(PlayerCharacter playerCharacter) {
		String message = ChatColor.GOLD + "Lv. " + level + " " + displayColor + name;
		playerCharacter.sendMessage(message);
	}

	public World getWorld() {
		return world;
	}

	public String getName() {
		return name;
	}

	public ChatColor getDisplayColor() {
		return displayColor;
	}

	private static class ZoneEntranceCollider extends Collider {

		private Zone zone;

		public ZoneEntranceCollider(Zone zone, BoundingBox boundingBox) {
			super(zone.getWorld(), boundingBox);
			this.zone = zone;
			setDrawingEnabled(true);
		}

		@Override
		protected void onCollisionEnter(Collider other) {
			if (other instanceof CharacterCollider) {
				AbstractCharacter character = ((CharacterCollider) other).getCharacter();
				if (character instanceof PlayerCharacterOLD) {
					Zone playerZone = ((PlayerCharacterOLD) character).getZone();
					if (playerZone != this.zone) {
						((PlayerCharacterOLD) character).setZone(zone);
					}
				}
			}
		}

		@Override
		protected void onCollisionExit(Collider other) {
			// nothing
		}

	}

}
