package com.eladrador.common.quest;

/**
 * Stores info regarding a player's status in completing a quest.
 */
public class QuestStatus {

	private String questName;
	private int phaseIndex;
	private QuestObjectiveChainStatus[] objectiveChainStatuses;

	public QuestStatus(Quest quest) {
		questName = quest.getName();
		phaseIndex = 0;
		renewObjectiveChainStatuses();
	}

	private void renewObjectiveChainStatuses() {
		Quest quest = Quest.forName(questName);
		QuestPhase phase = quest.getPhases()[phaseIndex];
		QuestObjectiveChain[] objectiveChains = phase.getObjectiveChains();
		objectiveChainStatuses = new QuestObjectiveChainStatus[objectiveChains.length];
		for (int i = 0; i < objectiveChains.length; i++) {
			QuestObjectiveChain objectiveChain = objectiveChains[i];
			objectiveChainStatuses[i] = new QuestObjectiveChainStatus(objectiveChain);
		}
	}

	public Quest getQuest() {
		return Quest.forName(questName);
	}

	public QuestPhase getActivePhase() {
		QuestPhase phase = getQuest().getPhases()[phaseIndex];
		return phase;
	}

	public void setActivePhase(QuestPhase phase) {
		phaseIndex = phase.getIndex();
	}

	public QuestObjectiveChainStatus[] getObjectiveChainStatuses() {
		return objectiveChainStatuses;
	}

}
