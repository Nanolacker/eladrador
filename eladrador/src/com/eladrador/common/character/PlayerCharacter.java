package com.eladrador.common.character;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.eladrador.common.item.EquipmentItem;
import com.eladrador.common.item.MainHandItem;
import com.eladrador.common.player.PlayerBackground;
import com.eladrador.common.player.PlayerClass;
import com.eladrador.common.quest.Quest;
import com.eladrador.common.quest.persistence.QuestStatus;
import com.eladrador.common.zone.Zone;

public interface PlayerCharacter extends GameCharacter {
	
	public Player getBukkitPlayer();

	public void sendMessage(String message);

	public PlayerClass getPlayerClass();

	public PlayerBackground getPlayerBackground();

	public double getCurrentMana();

	public void setCurrentMana(double currentMana);

	public double getMaxMana();

	public void setMaxMana(double maxMana);

	public double getXp();

	public void addXp(int xp);

	public Inventory getInventory();

	public QuestStatus getQuestStatus(Quest quest);

	public Zone getZone();

	public void setZone(Zone zone);

	public MainHandItem getMainHand();

	public void setMainHand(MainHandItem mainHand);

	public EquipmentItem getHead();

	public void setHead(EquipmentItem head);

	public EquipmentItem getChest();

	public void setChest(EquipmentItem chest);

	public EquipmentItem getLegs();

	public void setLegs(EquipmentItem legs);

	public EquipmentItem getFeet();

	public void setFeet(EquipmentItem feet);

	public int getCurrency();

	public void addCurrency(int currency);

	public void subtractCurrency(int currency);

	public void save();

}
