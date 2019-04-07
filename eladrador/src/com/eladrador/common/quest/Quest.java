package com.eladrador.common.quest;

import java.util.ArrayList;
import java.util.HashMap;

import com.eladrador.common.character.PlayerCharacter;
import com.eladrador.common.quest.persistence.QuestStatus;

public abstract class Quest {

	/**
	 * Keys are the quest IDs.
	 */
	private static final HashMap<Integer, Quest> QUEST_MAP = new HashMap<Integer, Quest>();

	private final String name;
	private ArrayList<QuestPhase> phases;
	private final int minLvl;

	protected Quest(String name, int id, int minLvl) {
		this.name = name;
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

	public QuestStatusType getStatusType(PlayerCharacter pc) {
		QuestStatus status = pc.getQuestStatus(this);
		if (status == null) {
			return QuestStatusType.NOT_AVAILABLE;
		}
		QuestPhase activePhase = status.getActivePhase();
		if (activePhase == null) {
			return QuestStatusType.COMPLETE;
		}
		if (activePhase.index == 0) {
			return QuestStatusType.NOT_STARTED;
		}
		return QuestStatusType.ACTIVE;
	}

}
