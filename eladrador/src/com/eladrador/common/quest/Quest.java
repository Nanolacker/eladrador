package com.eladrador.common.quest;

import java.util.ArrayList;
import java.util.HashMap;

import com.eladrador.common.character.PlayerCharacter;
import com.eladrador.common.quest.persistence.QuestState;

public abstract class Quest {

	private String name;
	private int id;
	private ArrayList<QuestPhase> phases;
	private int minLvl;

	protected Quest(String name, int id, int minLvl) {
		this.name = name;
		this.id = id;
		this.minLvl = minLvl;
		phases = new ArrayList<QuestPhase>();
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public ArrayList<QuestPhase> getPhases() {
		return phases;
	}

	public int getMinLevel() {
		return minLvl;
	}

	protected void registerPhase(QuestPhase phase) {
		phase.index = phases.size();
		phases.add(phase);
	}

	public QuestStatus getStatusFor(PlayerCharacter pc) {
		HashMap<Integer, QuestState> questStateMap = pc.getQuestStateMap();
		if (!questStateMap.containsKey(id)) {
			return QuestStatus.NOT_AVAILABLE;
		}
		QuestState state = questStateMap.get(id);
		QuestPhase activePhase = state.getActivePhase();
		if (activePhase == null) {
			return QuestStatus.COMPLETE;
		}
		if (activePhase.index == 0) {
			return QuestStatus.NOT_STARTED;
		}
		return QuestStatus.ACTIVE;
	}

}
