package com.eladrador.common.quest.persistence;

public class QuestObjectiveState {

	private int statusVal;

	 QuestObjectiveState() {
		statusVal = 0;
	}

	public int getStatusValue() {
		return statusVal;
	}

	public void setStatusValue(int val) {
		statusVal = val;
	}

}
