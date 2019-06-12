package com.eladrador.common.player;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;

import com.eladrador.common.character.PlayerCharacter;
import com.eladrador.common.quest.Quest;
import com.eladrador.common.quest.QuestObjective;
import com.eladrador.common.quest.QuestStatus;
import com.eladrador.common.quest.QuestStatusType;

public final class PlayerQuestManager {

	PlayerCharacter pc;
	/**
	 * Keys are names of quests.
	 */
	private final HashMap<String, QuestStatus> questStatusMap;
	private QuestObjective trackedQuestObjective;

	public PlayerQuestManager(PlayerCharacter pc) {
		this.pc = pc;
		questStatusMap = new HashMap<>();

	}

	public void openQuestLog() {
		Inventory inventory = Bukkit.createInventory(null, 9 * 6, ChatColor.BLACK + "Quest Log");
		ArrayList<Quest> activeQuests = new ArrayList<>();
		ArrayList<Quest> availableInactiveQuests = new ArrayList<>();
		for (Quest quest : Quest.getQuests()) {
			QuestStatusType statusType = quest.getStatusType(pc);
			if (statusType == QuestStatusType.ACTIVE) {
				activeQuests.add(quest);
			} else if (statusType == QuestStatusType.NOT_STARTED) {
				availableInactiveQuests.add(quest);
			}
		}
		ArrayList<Quest> quests = new ArrayList<>();
		quests.addAll(activeQuests);
		quests.addAll(availableInactiveQuests);
		for (Quest quest : quests) {
			inventory.addItem(quest.getQuestLogButton(pc).itemStack());
		}
		pc.getBukkitPlayer().openInventory(inventory);
	}

	public QuestStatus getQuestStatus(Quest quest) {
		return questStatusMap.get(quest.getName());
	}

}
