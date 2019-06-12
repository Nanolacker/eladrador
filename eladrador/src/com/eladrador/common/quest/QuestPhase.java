package com.eladrador.common.quest;

public class QuestPhase {

	private final QuestObjectiveChain[] objectiveChains;
	private transient Quest quest;
	private transient int index;

	public QuestPhase(QuestObjectiveChain[] objectiveChains) {
		this.objectiveChains = objectiveChains;
	}

	public QuestObjectiveChain[] getObjectiveChains() {
		return objectiveChains;
	}

	public Quest getQuest() {
		return quest;
	}

	void setQuest(Quest quest) {
		this.quest = quest;
	}

	int getIndex() {
		return index;
	}

	void setIndex(int index) {
		this.index = index;
	}

}
