package com.eladrador.common.quest;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.eladrador.common.character.PlayerCharacter;
import com.eladrador.common.ui.Button;

/**
 * Remember to call {@link Quest#initialize()} on all Quest instances.
 */
public class Quest {

	/**
	 * Keys are the quest names.
	 */
	private static final HashMap<String, Quest> QUEST_MAP = new HashMap<>();

	private final String name;
	/**
	 * The level at which this quest becomes available to a player of that level.
	 */
	private final int level;
	private final QuestPhase[] phases;
	private final Material icon;
	/**
	 * The names of prerequisite quests. Stored as Strings for serialization reasons.
	 */
	private final String[] prerequisites;

	public Quest(String name, int minLvl, QuestPhase[] phases, Material icon, Quest... prerequisites) {
		this.name = name;
		this.level = minLvl;
		this.phases = phases;
		this.icon = icon;
		this.prerequisites = new String[prerequisites.length];
		for (int i = 0; i < prerequisites.length; i++) {
			Quest prerequisite = prerequisites[i];
			this.prerequisites[i] = prerequisite.getName();
		}
	}

	/**
	 * This must be called before this quest can be used.
	 */
	public void initialize() {
		for (int i = 0; i < phases.length; i++) {
			QuestPhase phase = phases[i];
			phase.setIndex(i);
			QuestObjectiveChain[] objectiveChains = phase.getObjectiveChains();
			for (int j = 0; j < objectiveChains.length; j++) {
				QuestObjectiveChain objectiveChain = objectiveChains[j];
				objectiveChain.setIndex(j);
				QuestObjective[] objectives = objectiveChain.getObjectives();
				for (int k = 0; k < objectives.length; k++) {
					QuestObjective objective = objectives[k];
					objective.setIndex(k);
				}
			}
		}
		QUEST_MAP.put(name, this);
	}

	public static Quest[] getQuests() {
		return QUEST_MAP.values().toArray(new Quest[QUEST_MAP.size()]);
	}

	public static Quest forName(String name) {
		return QUEST_MAP.get(name);
	}

	public String getName() {
		return name;
	}

	/**
	 * Returns the level at which this quest becomes available to a player of that
	 * level.
	 */
	public int getLevel() {
		return level;
	}

	public QuestPhase[] getPhases() {
		return phases;
	}

	public Material getIcon() {
		return icon;
	}

	/**
	 * Convenience method for grabbing an objective from this quest.
	 * 
	 * @param phaseIndex          the index of the phase in this quest
	 * @param objectiveChainIndex the index of the objective chain in that phase
	 * @param objectiveIndex      the index of the objective in that objective chain
	 */
	public QuestObjective getObjective(int phaseIndex, int objectiveChainIndex, int objectiveIndex) {
		return phases[phaseIndex].getObjectiveChains()[objectiveChainIndex].getObjectives()[objectiveChainIndex];
	}

	public QuestStatusType getStatusType(PlayerCharacter pc) {
		QuestStatus status = pc.getQuestStatus(this);
		if (status == null) {
			return QuestStatusType.NOT_AVAILABLE;
		}
		QuestPhase activePhase = status.getActivePhase();
		if (activePhase == null) {
			return QuestStatusType.COMPLETE;
		}
		if (activePhase.getIndex() == 0) {
			return QuestStatusType.NOT_STARTED;
		}
		return QuestStatusType.ACTIVE;
	}

	public Button getQuestLogButton(PlayerCharacter pc) {
		ItemStack itemStack = new ItemStack(icon);
		return new Button(pc.getName() + "_" + this.getName() + "_QUEST_LOG_BUTTON", itemStack) {
			@Override
			protected void onToggle(Player player) {
				PlayerCharacter pc = PlayerCharacter.forBukkitPlayer(player);
				QuestStatus status = pc.getQuestStatus(Quest.this);
				QuestPhase phase = status.getActivePhase();

				pc.setTrackedQuestObjective(objective);
			}
		};
	}

}
