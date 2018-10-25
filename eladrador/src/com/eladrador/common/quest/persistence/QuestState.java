package com.eladrador.common.quest.persistence;

import java.util.ArrayList;

import com.eladrador.common.quest.Quest;
import com.eladrador.common.quest.QuestObjectiveChain;
import com.eladrador.common.quest.QuestPhase;

public class QuestState {

	private QuestPhase currentPhase;
	private ArrayList<QuestObjectiveChainState> currentObjChainStates;

	public QuestState(Quest q) {
		currentPhase = q.getPhases().get(0);
		currentObjChainStates = new ArrayList<QuestObjectiveChainState>();
		renewObjChainStates();
	}

	private void renewObjChainStates() {
		currentObjChainStates.clear();
		ArrayList<QuestObjectiveChain> objChains = currentPhase.getObjChains();
		for (int i = 0; i < objChains.size(); i++) {
			QuestObjectiveChain objChain = objChains.get(i);
			currentObjChainStates.add(new QuestObjectiveChainState(objChain));
		}
	}

	public QuestPhase getActivePhase() {
		return currentPhase;
	}

	public ArrayList<QuestObjectiveChainState> getCurrentObjChainStates() {
		return currentObjChainStates;
	}

}
