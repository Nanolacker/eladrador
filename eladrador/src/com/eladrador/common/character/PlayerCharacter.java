package com.eladrador.common.character;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.eladrador.common.Zone;
import com.eladrador.common.item.ItemContainer;
import com.eladrador.common.item.types.GItemStack;
import com.eladrador.common.player.PlayerBackground;
import com.eladrador.common.player.PlayerClass;
import com.eladrador.common.quest.persistence.QuestState;

public class PlayerCharacter extends AbstractCharacter {

	private Player bukkitPlayer;
	private PlayerBackground background;
	private PlayerClass playerClass;
	private HashMap<Integer, QuestState> questStateMap;
	private ItemContainer inventory;
	private ItemContainer hotbar;
	private GItemStack inHandItem;
	private Zone zone;
	private double maxMana;
	private double currentMana;

	/**
	 * Constructs a new level 1 PlayerCharacter.
	 * 
	 * @param bukkitPlayer the player entity to associate with this PlayerCharacter
	 */
	public PlayerCharacter(Player bukkitPlayer, PlayerBackground background, PlayerClass playerClass) {
		super(bukkitPlayer.getName(), 1, 0, null);// CHANGE
		setBukkitPlayer(bukkitPlayer);
		this.background = background;
		this.playerClass = playerClass;
		questStateMap = new HashMap<Integer, QuestState>();
		inventory = new ItemContainer(27);
		hotbar = new ItemContainer(8);

		zone = background.getStartZone();
		// calculate mana
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

	public void save() {
	}

	@Override
	protected void updateNameplatePosition() {
		nameplate.setLocation(location.clone().add(0, 2, 0));
	}

}
