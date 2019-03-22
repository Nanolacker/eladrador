package com.eladrador.common.quest;

import java.util.ArrayList;
import java.util.HashMap;

import com.eladrador.common.character.PlayerCharacterOLD;
import com.eladrador.common.quest.persistence.QuestStatus;

public abstract class Quest {

	/**
	 * Keys are the quest IDs.
	 */
	private static final HashMap<Integer, Quest> QUEST_MAP = new HashMap<Integer, Quest>();

	private String name;
	private int id;
	private ArrayList<QuestPhase> phases;
	private int minLvl;

	protected Quest(String name, int id, int minLvl) {
		this.name = name;
		this.id = id;
		this.minLvl = minLvl;
		phases = new ArrayList<QuestPhase>();
		QUEST_MAP.put(id, this);
	}

	public static Quest forID(int id) {
		return QUEST_MAP.get(id);
	}

	public String getName() {
		return name;
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

	public QuestStatus getStatusFor(PlayerCharacterOLD pc) {
		HashMap<Integer, QuestStatus> questStateMap = pc.getQuestStateMap();
		if (!questStateMap.containsKey(id)) {
			return QuestStatus.NOT_AVAILABLE;
		}
		QuestStatus state = questStateMap.get(this);
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
