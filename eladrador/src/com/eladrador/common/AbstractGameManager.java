package com.eladrador.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import com.eladrador.common.character.AbstractCharacter;
import com.eladrador.common.quest.Quest;
import com.eladrador.common.zone.Zone;

import net.minecraft.server.v1_13_R2.Entity;

/**
 * Integral for operating the game and storing important data.
 */
public abstract class AbstractGameManager {

	private HashMap<UUID, AbstractCharacter> charaMap;
	private ArrayList<Quest> quests;
	/**
	 * Keys are the names of the worlds.
	 */
	private HashMap<String, World> worldMap;
	private HashMap<World, ArrayList<Zone>> zoneMap;

	protected AbstractGameManager() {
		charaMap = new HashMap<UUID, AbstractCharacter>();
		quests = new ArrayList<Quest>();
		worldMap = new HashMap<String, World>();
		zoneMap = new HashMap<World, ArrayList<Zone>>();
	}

	public abstract void onEnable();

	public abstract void onDisable();

	/**
	 * Registers a quest so that it can be used properly. All quests to be used must
	 * be registered here.
	 * 
	 * @param quest the quest to register
	 */
	protected void registerQuest(Quest quest) {
		quests.add(quest);
	}

	public ArrayList<Quest> getQuests() {
		return quests;
	}

	/**
	 * Loads and registers a World to be used. Only register Worlds that are a part
	 * of the actual story of the game (i.e. don't register lobby or main menu
	 * worlds). IMPORTANT: REGISTER ALL WORLDS BEFORE REGISTERING ANY ZONES.
	 * 
	 * @param worldName the name of the World being registered
	 */
	protected void registerWorld(String worldName) {
		Server server = GPlugin.getBukkitServer();
		World world = server.createWorld(new WorldCreator(worldName));
		worldMap.put(worldName, world);
		zoneMap.put(world, new ArrayList<Zone>());
	}

	/**
	 * IMPORTANT: REGISTER ALL WORLDS BEFORE REGISTERING ANY ZONES.
	 * 
	 * @param zone the zone being registered
	 */
	protected void registerZone(Zone zone) {
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

	public ArrayList<Zone> getZones(World world) {
		return zoneMap.get(world);
	}

	/**
	 * Prepares a character to be used and recognized properly.
	 * 
	 * @param chara the character to be registered
	 */
	public void registerChara(AbstractCharacter chara) {
		charaMap.put(chara.getId(), chara);
	}

	/**
	 * Returns a character that is associated with an NMS entity
	 * 
	 * @param entity the NMS entity
	 * @return the character that is associated with the NMS entity, null if no such
	 *         character exists
	 */
	public AbstractCharacter charaByEntity(Entity entity) {
		Set<String> scoreboardTagSet = entity.getScoreboardTags();
		UUID id = idFromScorebaordTagSet(scoreboardTagSet);
		return charaById(id);
	}

	/**
	 * Returns a character that is associated with an Bukkit entity
	 * 
	 * @param entity the Bukkit entity
	 * @return the character that is associated with the Bukkit entity, null if no
	 *         such character exists
	 */
	public AbstractCharacter charaByEntity(org.bukkit.entity.Entity entity) {
		Set<String> scoreboardTagSet = entity.getScoreboardTags();
		UUID id = idFromScorebaordTagSet(scoreboardTagSet);
		return charaById(id);
	}

	/**
	 * Returns a character that matched an id.
	 * 
	 * @param id the id of the character
	 * @return the character whose is a matches, null if no such character exists
	 */
	public AbstractCharacter charaById(UUID id) {
		return charaMap.get(id);
	}

	/**
	 * 
	 * @param tagSet a set of tags found on an entity
	 * @return a UUID belonging to a character if one is found in tagSet
	 */
	private UUID idFromScorebaordTagSet(Set<String> tagSet) {
		Iterator<String> tags = tagSet.iterator();
		while (tags.hasNext()) {
			String unparsedId = tags.next();
			if (unparsedId.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
				return UUID.fromString(unparsedId);
			}
		}
		return null;
	}

	public World worldForName(String worldName) {
		return worldMap.get(worldName);
	}

}
