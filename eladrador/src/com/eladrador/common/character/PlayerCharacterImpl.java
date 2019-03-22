package com.eladrador.common.character;

import java.util.HashMap;

import org.apache.commons.lang3.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.eladrador.common.MMORPGPlugin;
import com.eladrador.common.item.EquipmentItem;
import com.eladrador.common.item.MainHandItem;
import com.eladrador.common.physics.Collider;
import com.eladrador.common.player.PlayerBackground;
import com.eladrador.common.player.ActionBar;
import com.eladrador.common.player.PlayerAttributes;
import com.eladrador.common.player.PlayerCharacterListener;
import com.eladrador.common.player.PlayerCharacterPersistence;
import com.eladrador.common.player.PlayerCharacterSaveData;
import com.eladrador.common.player.PlayerClass;
import com.eladrador.common.quest.Quest;
import com.eladrador.common.quest.persistence.QuestStatus;
import com.eladrador.common.utils.MathUtils;
import com.eladrador.common.zone.Zone;

import character.PlayerCharacter;

public class PlayerCharacterImpl extends GameCharacterImpl implements PlayerCharacter {

	private static final HashMap<Player, PlayerCharacterOLD> playerCharacterMap = new HashMap<>();
	private static final double[] XP_CHART = { 25, 50 };
	private static final int MAX_LEVEL = XP_CHART.length + 1;
	private static final double MAX_XP = sumXpChart();

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

	private Player bukkitPlayer;
	private int saveSlot;
	private PlayerBackground background;
	private PlayerClass playerClass;
	private PlayerAttributes attributes;
	/**
	 * Keys are names of quests.
	 */
	private HashMap<String, QuestStatus> questStatusMap;
	private Zone zone;
	private double xp;
	private double maxMana;
	private double currentMana;
	private MainHandItem mainHand;
	private EquipmentItem head;
	private EquipmentItem chest;
	private EquipmentItem legs;
	private EquipmentItem feet;
	private int currency;
	private CharacterCollider hitbox;
	private CharacterEntityMovementSynchronizer movementSyncer;

	private PlayerCharacterImpl(Player bukkitPlayer, int saveSlot, PlayerBackground background, PlayerClass playerClass,
			HashMap<String, QuestStatus> questStatusMap, Zone zone, double xp, double maxHealth, double currentHealth,
			double maxMana, double currentMana, PlayerAttributes attributes, Location location) {
		super(bukkitPlayer.getName(), levelForXp(xp), location, maxHealth);
		this.bukkitPlayer = bukkitPlayer;
		this.saveSlot = saveSlot;
		this.background = background;
		this.playerClass = playerClass;
		this.questStatusMap = questStatusMap;
		this.zone = zone;
		this.xp = xp;
		super.setCurrentHealth(currentHealth);
		this.maxMana = maxMana;
		this.currentMana = currentMana;
		this.attributes = attributes;
	}

	@Override
	public Player getBukkitPlayer() {
		return bukkitPlayer;
	}

	@Override
	public void sendMessage(String message) {
		bukkitPlayer.sendMessage(message);
	}

	@Override
	public PlayerClass getPlayerClass() {
		return playerClass;
	}

	@Override
	public PlayerBackground getPlayerBackground() {
		return background;
	}

	@Override
	public void setCurrentHealth(double currentHealth) {
		super.setCurrentHealth(currentHealth);
		updateHealthBar();
		updateActionBar();
	}

	@Override
	public void setMaxHealth(double maxHealth) {
		super.setMaxHealth(maxHealth);
		updateHealthBar();
		updateActionBar();
	}

	@Override
	public double getCurrentMana() {
		return currentMana;
	}

	@Override
	public void setCurrentMana(double currentMana) {
		currentMana = MathUtils.clamp(currentMana, 0.0, maxMana);
		this.currentMana = currentMana;
		updateManaBar();
		updateActionBar();
	}

	@Override
	public double getMaxMana() {
		return maxMana;
	}

	@Override
	public void setMaxMana(double maxMana) {
		this.maxMana = maxMana;
		updateManaBar();
		updateActionBar();
	}

	@Override
	public double getXp() {
		return xp;
	}

	@Override
	public void addXp(int xp) {
		Validate.isTrue(xp > 0.0, "Cannot add a negative xp");
		this.xp = MathUtils.clamp(this.xp + xp, 0.0, MAX_XP);
		int currentLevel = getLevel();
		int newLevel = levelForXp(this.xp);
		for (int i = currentLevel; i < newLevel; i++) {
			levelUp(newLevel);
		}
		setLevel(newLevel);
		updateExpBar();
		updateActionBar();
	}

	public static int levelForXp(double xp) {
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

	private void updateHealthBar() {
		double currentHealth = getCurrentHealth();
		double maxHealth = getMaxHealth();
		double bukkitPlayerHealth = currentHealth / maxHealth * 20;
		bukkitPlayer.setHealth(bukkitPlayerHealth);
	}

	private void updateManaBar() {
		int foodLevel = (int) (currentMana / maxMana * 20);
		bukkitPlayer.setFoodLevel(foodLevel);
	}

	private void updateExpBar() {
		double xpTowardNextLevel = getXpTowardNextLevel();
		double xpRequiredThisLevel = getXpRequiredThisLevel();
		float exp = xpRequiredThisLevel == 0.0 ? 0.0f : (float) (xpTowardNextLevel / xpRequiredThisLevel);
		bukkitPlayer.setExp(exp);
		int level = getLevel();
		bukkitPlayer.setLevel(level);
	}

	private void updateActionBar() {
		double currentHealth = getCurrentHealth();
		double maxHealth = getMaxHealth();
		double xpTowardNextLevel = getXpTowardNextLevel();
		double xpRequiredThisLevel = getXpRequiredThisLevel();
		String text = String.format(
				ChatColor.RED + "%.0f/%.0f HP    " + ChatColor.BLUE + "%.0f/%.0f MP    " + ChatColor.AQUA
						+ "%.0f/%.0f XP",
				currentHealth, maxHealth, currentMana, maxMana, xpTowardNextLevel, xpRequiredThisLevel);
		ActionBar actionBar = new ActionBar(text);
		actionBar.send(bukkitPlayer);
	}

	private double getXpTowardNextLevel() {
		int level = getLevel();
		double tempXp = xp;
		for (int i = 0; i < level - 1; i++) {
			tempXp -= XP_CHART[i];
		}
		return tempXp;
	}

	private double getXpRequiredThisLevel() {
		int level = getLevel();
		if (level == MAX_LEVEL) {
			return 0.0;
		}
		return XP_CHART[level - 1];
	}

	private void levelUp(int level) {
		bukkitPlayer.sendTitle(ChatColor.GOLD + "Level Up!", "");

	}

	@Override
	public Inventory getInventory() {
		return bukkitPlayer.getInventory();
	}

	@Override
	public QuestStatus getQuestStatus(Quest quest) {
		return questStatusMap.get(quest.getName());
	}

	@Override
	public Zone getZone() {
		return zone;
	}

	@Override
	public void setZone(Zone zone) {
		this.zone = zone;
		zone.displayEntranceMessage(this);
	}

	@Override
	public MainHandItem getMainHand() {
		return mainHand;
	}

	@Override
	public void setMainHand(MainHandItem mainHand) {
		if (this.mainHand != null) {
			this.mainHand.onUnequip(this);
		}
		this.mainHand = mainHand;
		if (mainHand != null) {
			mainHand.onEquip(this);
		}
	}

	@Override
	public EquipmentItem getHead() {
		return head;
	}

	@Override
	public void setHead(EquipmentItem head) {
		if (this.head != null) {
			this.head.onUnequip(this);
		}
		this.head = head;
		if (head != null) {
			head.onEquip(this);
		}
	}

	@Override
	public EquipmentItem getChest() {
		return chest;
	}

	@Override
	public void setChest(EquipmentItem chest) {
		if (this.chest != null) {
			this.chest.onUnequip(this);
		}
		this.chest = chest;
		if (chest != null) {
			chest.onEquip(this);
		}
	}

	@Override
	public EquipmentItem getLegs() {
		return legs;
	}

	@Override
	public void setLegs(EquipmentItem legs) {
		if (this.legs != null) {
			this.legs.onUnequip(this);
		}
		this.legs = legs;
		if (legs != null) {
			legs.onEquip(this);
		}
	}

	@Override
	public EquipmentItem getFeet() {
		return feet;
	}

	@Override
	public void setFeet(EquipmentItem feet) {
		if (this.feet != null) {
			this.feet.onUnequip(this);
		}
		this.feet = feet;
		if (feet != null) {
			feet.onEquip(this);
		}
	}

	@Override
	public int getCurrency() {
		return currency;
	}

	private void setCurrency(int currency) {
		this.currency = currency;
		// TODO: update some other stuff
	}

	@Override
	public void addCurrency(int currency) {
		setCurrency(this.currency + currency);
	}

	@Override
	public void subtractCurrency(int currency) {
		setCurrency(this.currency - currency);
	}

	@Override
	public void save() {
		String name = getName();
		Location location = getLocation();
		double maxHealth = getMaxHealth();
		double currentHealth = getCurrentHealth();
		PlayerCharacterSaveData data = new PlayerCharacterSaveData(name, saveSlot, background.getName(),
				playerClass.getName(), questStatusMap, zone.getName(), xp, maxHealth, currentHealth, maxMana,
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
