package com.eladrador.common.quest.persistence;

import java.util.ArrayList;

import com.eladrador.common.quest.QuestObjective;
import com.eladrador.common.quest.QuestObjectiveChain;

public class QuestObjectiveChainState {

	private ArrayList<QuestObjectiveState> objStates;

	QuestObjectiveChainState(QuestObjectiveChain objChain) {
		objStates = new ArrayList<QuestObjectiveState>();
		ArrayList<QuestObjective> objs = objChain.getObjs();
		for (int i = 0; i < objs.size(); i++) {
			objStates.add(new QuestObjectiveState());
		}
	}

	public ArrayList<QuestObjectiveState> getObjStates() {
		return objStates;
	}

}
