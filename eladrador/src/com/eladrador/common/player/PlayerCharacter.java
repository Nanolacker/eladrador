package com.eladrador.common.player;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.eladrador.common.Debug;
import com.eladrador.common.GPlugin;
import com.eladrador.common.character.AbstractCharacter;
import com.eladrador.common.character.CharacterCollider;
import com.eladrador.common.collision.Collider;
import com.eladrador.common.item.ItemContainer;
import com.eladrador.common.item.types.GItemStack;
import com.eladrador.common.quest.persistence.QuestState;
import com.eladrador.common.utils.CharacterEntityMovementSynchronizer;
import com.eladrador.common.utils.CharacterEntityMovementSynchronizer.MovementSynchronizeMode;
import com.eladrador.common.zone.Zone;

public class PlayerCharacter extends AbstractCharacter {

	private static final HashMap<Player, PlayerCharacter> PLAYER_CHARACTER_MAP = new HashMap<Player, PlayerCharacter>();

	private Player bukkitPlayer;
	private int saveSlot;
	private PlayerBackground background;
	private PlayerClass playerClass;
	/**
	 * Keys are the quest IDs.
	 */
	private HashMap<Integer, QuestState> questStateMap;
	private Zone zone;
	private ItemContainer inventory;
	private ItemContainer hotbar;
	private GItemStack inHandItem;
	private double xp;
	private double maxMana;
	private double currentMana;
	private PlayerCharacterAttributes attributes;
	/**
	 * The hitbox of the player.
	 */
	private CharacterCollider hitbox;

	/**
	 * Returns a new, level 1 {@code PlayerCharacter}.
	 * {@link PlayerCharacter#setBukkitPlayer(Player)} must be invoked on the
	 * returned {@code PlayerCharacter}.
	 */
	private PlayerCharacter(Player bukkitPlayer, int saveSlot, PlayerBackground background, PlayerClass playerClass,
			HashMap<Integer, QuestState> questStateMap, Zone zone, double xp, double maxHealth, double currentHealth,
			double maxMana, double currentMana, PlayerCharacterAttributes attributes, Location location) {
		super(bukkitPlayer.getName(), levelFromXP(xp), maxHealth, location);
		this.saveSlot = saveSlot;
		this.background = background;
		this.playerClass = playerClass;
		this.questStateMap = questStateMap;
		this.zone = zone;
		this.xp = xp;
		this.currentHealth = currentHealth;
		this.maxMana = maxMana;
		this.currentMana = currentMana;
		this.attributes = attributes;
		this.location = location;
	}

	/**
	 * Creates a save for a new {@code PlayerCharacter}.
	 */
	public static void createNew(Player bukkitPlayer, int saveSlot, PlayerBackground background,
			PlayerClass playerClass) {
		/*
		 * RECALCULATE LATER
		 */
		int maxHealth = 1;
		int maxMana = 1;

		PlayerCharacterSaveData data = new PlayerCharacterSaveData(bukkitPlayer.getName(), saveSlot, background.getID(),
				playerClass.getID(), new HashMap<Integer, QuestState>(), background.getStartZone().getID(), 0,
				maxHealth, maxHealth, maxMana, maxMana, new PlayerCharacterAttributes(), background.getStartLocation());
		PlayerCharacterPersistence.storeData(data);
	}

	/**
	 * Returns the stored {@code PlayerCharacter} that corresponds to the specified
	 * {@code Player} and save slot. {@link PlayerCharacter#setBukkitPlayer(Player)}
	 * must be invoked on the returned {@code PlayerCharacter}.
	 */
	public static PlayerCharacter retrieve(Player bukkitPlayer, int saveSlot) {
		PlayerCharacterSaveData data = PlayerCharacterPersistence.retrieveData(bukkitPlayer, saveSlot);
		PlayerClass playerClass = PlayerClass.byID(data.playerClassID);
		PlayerBackground background = PlayerBackground.byID(data.playerBackgroundID);
		Zone zone = Zone.byID(data.zoneID);
		World world = GPlugin.getGameManager().worldByName(data.worldName);
		Location location = new Location(world, data.x, data.y, data.z, data.yaw, data.pitch);
		return new PlayerCharacter(bukkitPlayer, data.saveSlot, background, playerClass, data.questStateMap, zone,
				data.xp, data.maxHealth, data.currentHealth, data.maxMana, data.currentMana, data.attributes, location);
	}

	public static void deleteData(Player bukkitPlayer, int saveSlot) {
		PlayerCharacterPersistence.deleteData(bukkitPlayer, saveSlot);
	}

	/**
	 * Returns {@code true} if a {@code PlayerCharacter} has been created that
	 * corresponds to the specified Bukkit Player and save slot.
	 */
	public static boolean saveExists(Player bukkitPlayer, int saveSlot) {
		return PlayerCharacterPersistence.saveExists(bukkitPlayer, saveSlot);
	}

	public static PlayerCharacter byBukkitPlayer(Player bukkitPlayer) {
		return PLAYER_CHARACTER_MAP.get(bukkitPlayer);
	}

	/**
	 * Returns the Minecraft player this PlayerCharacter is attached to.
	 * 
	 * @return the Minecraft player this PlayerCharacter is attached to
	 */
	public Player getBukkitPlayer() {
		return bukkitPlayer;
	}

	/**
	 * Sets the Minecraft player entity this PlayerCharacter is associated with.
	 * 
	 * @param bukkitPlayer the Minecraft player entity to associate with this
	 *                     PlayerCharacter
	 */
	public void setBukkitPlayer(Player bukkitPlayer) {
		this.bukkitPlayer = bukkitPlayer;
		bukkitPlayer.teleport(location);
		Location hitboxCenter = location.add(0, 1, 0);
		CharacterEntityMovementSynchronizer syncer = new CharacterEntityMovementSynchronizer(this, bukkitPlayer,
				MovementSynchronizeMode.CHARACTER_FOLLOWS_ENTITY);
		syncer.setEnabled(true);
		PLAYER_CHARACTER_MAP.put(bukkitPlayer, this);
		hitbox = new PlayerCharacterCollider(this, hitboxCenter);
		hitbox.setActive(true);
	}

	public int getSaveSlot() {
		return saveSlot;
	}

	public PlayerBackground getBackground() {
		return background;
	}

	public PlayerClass getPlayerClass() {
		return playerClass;
	}

	public HashMap<Integer, QuestState> getQuestStateMap() {
		return questStateMap;
	}

	public ItemContainer getInventory() {
		return inventory;
	}

	public ItemContainer getHotbar() {
		return hotbar;
	}

	public GItemStack getInHandItem() {
		return inHandItem;
	}

	@Override
	protected Location getNameplateLocation() {
		return location.clone().add(0, 2, 0);
	}

	/**
	 * Sends a message to the player associated with this {@code PlayerCharacter}.
	 * 
	 * @param message the message to be sent
	 */
	public void sendMessage(String message) {
		bukkitPlayer.sendMessage(message);
	}

	@Override
	public void setLocation(Location location) {
		super.setLocation(location);
		hitbox.setCenter(location.clone().add(0, 1, 0));
	}

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
		zone.displayEntranceMessage(this);
	}

	public static int levelFromXP(double xp) {

		/*
		 * RECALCULATE LATER
		 */
		return 1;

	}

	public double getXP() {
		return xp;
	}

	public double getMaxMana() {
		return maxMana;
	}

	public double getCurrentMana() {
		return currentMana;
	}

	public PlayerCharacterAttributes getAttributes() {
		return attributes;
	}

	/**
	 * Stores the specified {@code PlayerCharacter} to be retrieved later.
	 */
	public void saveData() {
		hitbox.setActive(false);
		PlayerCharacterSaveData data = new PlayerCharacterSaveData(name, saveSlot, background.getID(),
				playerClass.getID(), questStateMap, zone.getID(), xp, maxHealth, currentHealth, maxMana, currentMana,
				attributes, location);
		PlayerCharacterPersistence.storeData(data);
	}

	private static final class PlayerCharacterCollider extends CharacterCollider {

		private PlayerCharacterCollider(PlayerCharacter pc, Location center) {
			super(pc, center, 1.0, 2.0, 1.0);
			setDrawingEnabled(true);
		}

		@Override
		protected void onCollisionEnter(Collider other) {
		}

		@Override
		protected void onCollisionExit(Collider other) {
		}

	}

}
