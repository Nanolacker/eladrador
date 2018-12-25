package com.eladrador.common.quest;

import java.util.HashMap;

import com.eladrador.common.player.PlayerCharacter;
import com.eladrador.common.quest.persistence.QuestObjectiveChainState;
import com.eladrador.common.quest.persistence.QuestObjectiveState;
import com.eladrador.common.quest.persistence.QuestState;

public abstract class QuestObjective {

	QuestObjectiveChain parent;
	private String desc;
	private int goal;
	int index;

	protected QuestObjective(String description, int goal) {
		desc = description;
		this.goal = goal;
	}

	public String getDescription() {
		return desc;
	}

	public int getGoal() {
		return goal;
	}

	public int getStatusValueFor(PlayerCharacter pc) {
		HashMap<Integer, QuestState> stateMap = pc.getQuestStateMap();
		QuestPhase targetPhase = parent.parent;
		Quest q = targetPhase.parent;
		if (!stateMap.containsKey(q)) {
			return 0;
		}
		QuestState state = stateMap.get(q);
		QuestPhase activePhase = state.getActivePhase();
		int activePhaseIndex = activePhase.index;
		int targetPhaseIndex = targetPhase.index;
		if (activePhaseIndex < targetPhaseIndex) {
			return 0;
		}
		if (activePhaseIndex > targetPhaseIndex) {
			return goal;
		}
		QuestObjectiveChainState activeChainState = state.getCurrentObjChainStates().get(parent.index);
		QuestObjectiveState objState = activeChainState.getObjStates().get(index);
		return objState.getStatusValue();
	}

	public void setStatusValueFor(PlayerCharacter pc, int val) {
		HashMap<Integer, QuestState> stateMap = pc.getQuestStateMap();
		QuestPhase targetPhase = parent.parent;
		Quest q = targetPhase.parent;
		if (!stateMap.containsKey(q)) {
			throwInactiveObjectiveException();
		} else {
			QuestState state = stateMap.get(q);
			QuestPhase activePhase = state.getActivePhase();
			int activePhaseIndex = activePhase.index;
			int targetPhaseIndex = targetPhase.index;
			if (activePhaseIndex < targetPhaseIndex) {
				throwInactiveObjectiveException();
			} else if (activePhaseIndex > targetPhaseIndex) {
				throwInactiveObjectiveException();
			} else {
				QuestObjectiveChainState activeChainState = state.getCurrentObjChainStates().get(parent.index);
				QuestObjectiveState objState = activeChainState.getObjStates().get(index);
				objState.setStatusValue(val);
			}
		}
	}

	private void throwInactiveObjectiveException() {
		throw new RuntimeException("This objective must be active for the player to set its status");
	}

	public boolean isFulfilled(PlayerCharacter pc) {
		return getStatusValueFor(pc) == goal;
	}

}
