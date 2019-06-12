package com.eladrador.common.quest;

import org.bukkit.Location;
import com.eladrador.common.character.PlayerCharacter;
import com.eladrador.common.utils.JsonSafeLocation;

public class QuestObjective {

	private final String description;
	private final int goal;
	private final JsonSafeLocation location;
	private transient QuestObjectiveChain chain;
	private transient int index;

	/**
	 * @param location can be {@code null} if there is no location that represents
	 *                 this objective.
	 */
	public QuestObjective(String description, int goal, Location location) {
		this.description = description;
		this.goal = goal;
		this.location = new JsonSafeLocation(location);
	}

	public String getDescription() {
		return description;
	}

	public int getGoal() {
		return goal;
	}

	public Location getLocation() {
		return location.toLocation();
	}

	public int getStatusValueFor(PlayerCharacter pc) {
		QuestPhase targetPhase = chain.getPhase();
		Quest quest = targetPhase.getQuest();
		QuestStatus status = pc.getQuestStatus(quest);
		if (status == null) {
			return 0;
		}
		QuestPhase activePhase = status.getActivePhase();
		int activePhaseIndex = activePhase.getIndex();
		int targetPhaseIndex = targetPhase.getIndex();
		if (activePhaseIndex < targetPhaseIndex) {
			return 0;
		}
		if (activePhaseIndex > targetPhaseIndex) {
			return goal;
		}
		QuestObjectiveChainStatus activeChainState = status.getObjectiveChainStatuses()[chain.getIndex()];
		QuestObjectiveStatus objState = activeChainState.getObjectiveStatuses()[index];
		return objState.getStatusValue();
	}

	public void setStatusValueFor(PlayerCharacter pc, int val) {
		QuestPhase targetPhase = chain.getPhase();
		Quest quest = targetPhase.getQuest();
		QuestStatus status = pc.getQuestStatus(quest);
		if (status == null) {
			throwInactiveObjectiveException();
		} else {
			QuestPhase activePhase = status.getActivePhase();
			int activePhaseIndex = activePhase.getIndex();
			int targetPhaseIndex = targetPhase.getIndex();
			if (activePhaseIndex < targetPhaseIndex) {
				throwInactiveObjectiveException();
			} else if (activePhaseIndex > targetPhaseIndex) {
				throwInactiveObjectiveException();
			} else {
				QuestObjectiveChainStatus activeChainState = status.getObjectiveChainStatuses()[chain.getIndex()];
				QuestObjectiveStatus objState = activeChainState.getObjectiveStatuses()[index];
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

	public QuestObjectiveChain getChain() {
		return chain;
	}

	void setChain(QuestObjectiveChain chain) {
		this.chain = chain;
	}

	int getIndex() {
		return index;
	}

	void setIndex(int index) {
		this.index = index;
	}

}
