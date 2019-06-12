package com.eladrador.common.character._old;

import java.util.HashMap;

import org.apache.commons.lang3.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import com.eladrador.common.MMORPGPlugin;
import com.eladrador.common.character.CharacterCollider;
import com.eladrador.common.character.CharacterEntityMovementSynchronizer;
import com.eladrador.common.character.CharacterEntityMovementSynchronizer.MovementSynchronizeMode;
import com.eladrador.common.item.EquipmentItem;
import com.eladrador.common.item.MainHandItem;
import com.eladrador.common.physics.Collider;
import com.eladrador.common.player.ActionBar;
import com.eladrador.common.player.PlayerBackground;
import com.eladrador.common.player.PlayerAttributes;
import com.eladrador.common.player.PlayerCharacterListener;
import com.eladrador.common.player.PlayerCharacterPersistence;
import com.eladrador.common.player.PlayerCharacterSaveData;
import com.eladrador.common.player.PlayerClass;
import com.eladrador.common.quest.QuestStatus;
import com.eladrador.common.utils.MathUtils;
import com.eladrador.common.zone.Zone;

class PlayerCharacterOLD extends AbstractCharacter {

	private static final HashMap<Player, PlayerCharacterOLD> playerCharacterMap = new HashMap<Player, PlayerCharacterOLD>();
	private static final double[] XP_CHART = { 25, 50 };
	private static final int MAX_LEVEL = XP_CHART.length + 1;
	private static final double MAX_XP = sumXpChart();

	private Player bukkitPlayer;
	private int saveSlot;
	private PlayerBackground background;
	private PlayerClass playerClass;
	/**
	 * Keys are the quest IDs.
	 */
	private HashMap<Integer, QuestStatus> questStateMap;
	private Zone zone;
	private double xp;
	private double maxMana;
	private double currentMana;
	private PlayerAttributes attributes;
	private MainHandItem mainHand;
	private EquipmentItem head;
	private EquipmentItem chest;
	private EquipmentItem legs;
	private EquipmentItem feet;
	private CharacterCollider hitbox;
	private CharacterEntityMovementSynchronizer movementSyncer;

	static {
		MMORPGPlugin.registerEvents(new PlayerCharacterListener());
	}

	/**
	 * Used to initialize {@link PlayerCharacterOLD#MAX_XP}.
	 */
	private static double sumXpChart() {
		double sum = 0.0;
		for (double xp : XP_CHART) {
			sum += xp;
		}
		return sum;
	}

	private PlayerCharacterOLD(Player bukkitPlayer, int saveSlot, PlayerBackground background, PlayerClass playerClass,
			HashMap<Integer, QuestStatus> questStateMap, Zone zone, double xp, double maxHealth, double currentHealth,
			double maxMana, double currentMana, PlayerAttributes attributes, Location location) {
		super(bukkitPlayer.getName(), levelForXP(xp), maxHealth, location);
		this.bukkitPlayer = bukkitPlayer;
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
	}

	/**
	 * Creates a save for a new {@code PlayerCharacter}.
	 */
	public static void createNew(Player bukkitPlayer, int saveSlot, PlayerBackground background,
			PlayerClass playerClass) {
		/*
		 * RECALCULATE LATER
		 */
		int maxHealth = 15;
		int maxMana = 15;

		PlayerCharacterSaveData data = new PlayerCharacterSaveData(bukkitPlayer.getName(), saveSlot,
				background.getName(), playerClass.getName(), new HashMap<String, QuestStatus>(),
				background.getStartZone().getName(), 0, maxHealth, maxHealth, maxMana, maxMana, new PlayerAttributes(),
				background.getStartLocation());

		PlayerCharacterPersistence.storeData(data);
	}

	/**
	 * Returns the stored {@code PlayerCharacter} that corresponds to the specified
	 * {@code Player} and save slot.
	 * {@link PlayerCharacterOLD#setBukkitPlayer(Player)} must be invoked on the
	 * returned {@code PlayerCharacter}.
	 */
	public static PlayerCharacterOLD retrieve(Player bukkitPlayer, int saveSlot) {
		PlayerCharacterSaveData data = PlayerCharacterPersistence.retrieveData(bukkitPlayer, saveSlot);
		PlayerClass playerClass = PlayerClass.forName(data.getPlayerClassName());
		PlayerBackground background = PlayerBackground.forName(data.getPlayerBackgroundName());
		Zone zone = Zone.forName(data.getZoneName());
		World world = MMORPGPlugin.getGameManager().worldForName(data.getWorldName());
		Location location = new Location(world, data.getLocX(), data.getLocY(), data.getLocZ(), data.getYaw(),
				data.getPitch());
		return new PlayerCharacterOLD(bukkitPlayer, data.getSaveSlot(), background, playerClass,
				data.getQuestStateMap(), zone, data.getXp(), data.getMaxHealth(), data.getCurrentHealth(),
				data.getMaxMana(), data.getCurrentMana(), data.getAttributes(), location);
	}

	/**
	 * Returns true if the specified Bukkit player is attached to a player
	 * character.
	 */
	public static boolean bukkitPlayerIsAttachedToPlayerCharacter(Player bukkitPlayer) {
		return playerCharacterMap.containsKey(bukkitPlayer);
	}

	/**
	 * Returns the {@code PlayerCharacter} that corresponds to the specified
	 * {@code Player}.
	 * 
	 * @param bukkitPlayer the {@code Player}
	 * @return
	 */
	public static PlayerCharacterOLD forBukkitPlayer(Player bukkitPlayer) {
		return playerCharacterMap.get(bukkitPlayer);
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
		Location location = getLocation();
		bukkitPlayer.teleport(location);
		Location hitboxCenter = location.clone().add(0, 1, 0);
		movementSyncer = new CharacterEntityMovementSynchronizer(this, bukkitPlayer,
				MovementSynchronizeMode.CHARACTER_FOLLOWS_ENTITY);
		movementSyncer.setEnabled(true);
		playerCharacterMap.put(bukkitPlayer, this);
		hitbox = new PlayerCharacterCollider(this, hitboxCenter);
		hitbox.setActive(true);

		updateHealthBar();
		updateFoodBar();
		updateActionBar();
		updateXpBar();
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

	public HashMap<Integer, QuestStatus> getQuestStateMap() {
		return questStateMap;
	}

	public PlayerInventory getInventory() {
		return bukkitPlayer.getInventory();
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

	public static int levelForXP(double xp) {
		int level = 0;
		while (xp >= 0.0) {
			if (level == MAX_LEVEL - 1) {
				return MAX_LEVEL;
			}
			xp -= XP_CHART[level];
			level++;
		}
		return level;
	}

	public double getXp() {
		return xp;
	}

	public void addXp(double toAdd) {
		Validate.isTrue(toAdd > 0.0, "Added xp must be positive");
		xp = MathUtils.clamp(xp + toAdd, 0.0, MAX_XP);
		int newLevel = levelForXP(xp);
		for (int i = this.level; i < newLevel; i++) {
			levelUp(newLevel);
		}
		this.level = newLevel;
		updateActionBar();
		updateXpBar();
	}

	private double getXpTowardNextLevel() {
		double tempXp = xp;
		for (int i = 0; i < level - 1; i++) {
			tempXp -= XP_CHART[i];
		}
		return tempXp;
	}

	private double getXpRequiredThisLevel() {
		if (level == MAX_LEVEL) {
			return 0.0;
		}
		return XP_CHART[level - 1];
	}

	private void updateXpBar() {
		double xpTowardNextLevel = getXpTowardNextLevel();
		double xpRequiredThisLevel = getXpRequiredThisLevel();
		float exp = xpRequiredThisLevel == 0.0 ? 0.0f : (float) (xpTowardNextLevel / xpRequiredThisLevel);
		bukkitPlayer.setExp(exp);
		bukkitPlayer.setLevel(level);
	}

	private void levelUp(int level) {
		bukkitPlayer.sendTitle(ChatColor.GOLD + "Level Up!", "");
	}

	@Override
	protected void setCurrentHealth(double currentHealth) {
		if (currentHealth < 0.0) {
			currentHealth = 0.0;
		}
		super.setCurrentHealth(currentHealth);
		updateHealthBar();
		updateActionBar();
	}

	private void updateHealthBar() {
		double bukkitPlayerHealth = currentHealth / maxHealth * 20;
		bukkitPlayer.setHealth(bukkitPlayerHealth);
	}

	public double getMaxMana() {
		return maxMana;
	}

	public double getCurrentMana() {
		return currentMana;
	}

	public void setCurrentMana(double currentMana) {
		if (currentMana < 0.0) {
			currentMana = 0.0;
		}
		this.currentMana = currentMana;
		updateFoodBar();
		updateActionBar();
	}

	private void updateFoodBar() {
		int foodLevel = (int) (currentMana / maxMana * 20);
		bukkitPlayer.setFoodLevel(foodLevel);
	}

	public PlayerAttributes getAttributes() {
		return attributes;
	}

	public MainHandItem getMainHand() {
		return mainHand;
	}

	public void setMainHand(MainHandItem mainHand) {
		if (this.mainHand != null) {
			this.mainHand.onUnequip(this);
		}
		this.mainHand = mainHand;
		if (mainHand != null) {
			mainHand.onEquip(this);
		}
	}

	public EquipmentItem getHead() {
		return head;
	}

	public void setHead(EquipmentItem head) {
		if (this.head != null) {
			this.head.onUnequip(this);
		}
		this.head = head;
		if (head != null) {
			head.onEquip(this);
		}
	}

	public EquipmentItem getChest() {
		return chest;
	}

	public void setChest(EquipmentItem chest) {
		if (this.chest != null) {
			this.chest.onUnequip(this);
		}
		this.chest = chest;
		if (chest != null) {
			chest.onEquip(this);
		}
	}

	public EquipmentItem getLegs() {
		return legs;
	}

	public void setLegs(EquipmentItem legs) {
		if (this.legs != null) {
			this.legs.onUnequip(this);
		}
		this.legs = legs;
		if (legs != null) {
			legs.onEquip(this);
		}
	}

	public EquipmentItem getFeet() {
		return feet;
	}

	public void setFeet(EquipmentItem feet) {
		if (this.feet != null) {
			this.feet.onUnequip(this);
		}
		this.feet = feet;
		if (feet != null) {
			feet.onEquip(this);
		}
	}

	/**
	 * Stores the specified {@code PlayerCharacter} to be retrieved later.
	 */
	public void saveData() {
		PlayerCharacterSaveData data = new PlayerCharacterSaveData(name, saveSlot, background.getName(),
				playerClass.getName(), questStateMap, zone.getName(), xp, maxHealth, currentHealth, maxMana,
				currentMana, attributes, location);
		PlayerCharacterPersistence.storeData(data);

		playerCharacterMap.remove(bukkitPlayer);
		hitbox.setActive(false);
		movementSyncer.setEnabled(false);
	}

	private static final class PlayerCharacterCollider extends CharacterCollider {

		private PlayerCharacterCollider(PlayerCharacterOLD pc, Location center) {
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
