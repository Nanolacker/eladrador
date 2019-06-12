package com.eladrador.common;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import com.eladrador.common.character.NonPlayerCharacter;
import com.eladrador.common.character.PlayerCharacter;
import com.eladrador.common.quest.Quest;
import com.eladrador.common.zone.Zone;

public final class GameManager {

	/**
	 * Keys are the names of the worlds.
	 */
	private static final HashMap<String, World> worldMap;
	private static final HashMap<World, ArrayList<Zone>> zoneMap;
	private static final HashMap<Player, PlayerCharacter> playerCharacterMap;
	private static final ArrayList<NonPlayerCharacter> nonPlayerCharacters;

	static {
		worldMap = new HashMap<>();
		zoneMap = new HashMap<>();
		playerCharacterMap = new HashMap<>();
		nonPlayerCharacters = new ArrayList<>();
	}

	/**
	 * Loads and registers a World to be used. Only register Worlds that are a part
	 * of the actual story of the game here (i.e. don't register lobby or main menu
	 * worlds). IMPORTANT: REGISTER ALL WORLDS BEFORE REGISTERING ANY ZONES.
	 * 
	 * @param worldName the name of the World being registered
	 */
	public static void registerWorld(String worldName) {
		World world = Bukkit.createWorld(new WorldCreator(worldName));
		worldMap.put(worldName, world);
		zoneMap.put(world, new ArrayList<Zone>());
	}

	public static World worldForName(String worldName) {
		return worldMap.get(worldName);
	}

	/**
	 * IMPORTANT: REGISTER ALL WORLDS BEFORE REGISTERING ANY ZONES.
	 * 
	 * @param zone the zone being registered
	 */
	public static void registerZone(Zone zone) {
		World world = zone.getWorld();
		if (zoneMap.containsKey(world)) {
			ArrayList<Zone> zoneList = zoneMap.get(world);
			zoneList.add(zone);
		} else {
			try {
				throw new IllegalArgumentException("The world of zone " + "(" + zone.getName()
						+ ") has not been registered. Be sure to register it with registerWorld(World) before registering any zones");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}

	public static ArrayList<Zone> getZones(World world) {
		return zoneMap.get(world);
	}

	/**
	 * The PlayerCharacter and the Player associated with it will become fully
	 * linked.
	 */
	public static void registerPlayerCharacter(PlayerCharacter pc) {
		playerCharacterMap.put(pc.getBukkitPlayer(), pc);
	}

	public static PlayerCharacter playerCharacterForBukkitPlayer(Player player) {
		return null;
	}

	/**
	 * The NonPlayerCharacter will be spawned when nearby players are present and
	 * despawned when nearby players are absent.
	 */
	public static void registerNonPlayerCharacter(NonPlayerCharacter npc) {
		nonPlayerCharacters.add(npc);
	}

}
