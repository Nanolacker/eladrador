package com.eladrador.common.zone;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import com.eladrador.common.Debug;
import com.eladrador.common.character.AbstractCharacter;
import com.eladrador.common.character.CharacterCollider;
import com.eladrador.common.collision.Collider;
import com.eladrador.common.player.PlayerCharacter;

/**
 * Represents an area of interest within game. Size can range from that of small
 * bandit camps to that of continents.
 */
public abstract class Zone {

	/**
	 * Keys are the zone IDs.
	 */
	private static final HashMap<Integer, Zone> ZONE_MAP = new HashMap<Integer, Zone>();

	private World world;
	private String name;
	private int id;
	private ChatColor displayColor;
	private int level;

	protected Zone(World world, String name, int id, ChatColor displayColor, int level, BoundingBox... entranceBoxes) {
		this.world = world;
		this.name = name;
		this.id = id;
		this.displayColor = displayColor;
		this.level = level;
		for (int i = 0; i < entranceBoxes.length; i++) {
			BoundingBox boundingBox = entranceBoxes[i];
			ZoneEntranceCollider collider = new ZoneEntranceCollider(this, boundingBox);
			collider.setActive(true);
		}
		ZONE_MAP.put(id, this);
	}

	public static Zone byID(int id) {
		return ZONE_MAP.get(id);
	}

	public void displayEntranceMessage(PlayerCharacter pc) {
		Player p = pc.getBukkitPlayer();
		String msg = ChatColor.GOLD + "Lv. " + level + " " + displayColor + name;
		p.sendMessage(msg);
	}

	public World getWorld() {
		return world;
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return id;
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
				if (character instanceof PlayerCharacter) {
					Zone playerZone = ((PlayerCharacter) character).getZone();
					if (playerZone != this.zone) {
						((PlayerCharacter) character).setZone(zone);
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
