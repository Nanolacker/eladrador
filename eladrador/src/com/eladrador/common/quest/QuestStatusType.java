package com.eladrador.common.quest;

public enum QuestStatus {

	/**
	 * Player does not fulfill the requirements to begin this quest.
	 */
	NOT_AVAILABLE,

	/**
	 * Player has quest available but has not interacted with it.
	 */
	NOT_STARTED,

	/**
	 * Player has quest available and has advanced to the first phase. Quest is not
	 * completed.
	 */
	ACTIVE,

	/**
	 * Player has completed quest.
	 */
	COMPLETE
}
