package com.eladrador.common.quest;

import org.bukkit.event.Listener;

public abstract class QuestListener implements Listener {

	Quest quest;

	protected QuestListener(Quest quest) {
		this.quest = quest;
	}

	public Quest getQuest() {
		return quest;
	}

}
