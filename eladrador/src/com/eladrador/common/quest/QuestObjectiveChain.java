package com.eladrador.common.quest;

public class QuestObjectiveChain {

	private final QuestObjective[] objectives;
	private transient QuestPhase phase;
	private transient int index;

	public QuestObjectiveChain(QuestObjective[] objectives) {
		this.objectives = objectives;
	}

	public QuestObjective[] getObjectives() {
		return objectives;
	}

	public QuestPhase getPhase() {
		return phase;
	}

	void setPhase(QuestPhase phase) {
		this.phase = phase;
	}

	int getIndex() {
		return index;
	}

	void setIndex(int index) {
		this.index = index;
	}

}
