package com.eladrador.common.quest;

public class QuestObjectiveChainStatus {

	private QuestObjectiveStatus[] objectiveStatuses;

	QuestObjectiveChainStatus(QuestObjectiveChain objectiveChain) {
		QuestObjective[] objectives = objectiveChain.getObjectives();
		objectiveStatuses = new QuestObjectiveStatus[objectives.length];
		for (int i = 0; i < objectives.length; i++) {
			objectiveStatuses[i] = new QuestObjectiveStatus();
		}
	}

	public QuestObjectiveStatus[] getObjectiveStatuses() {
		return objectiveStatuses;
	}

}
