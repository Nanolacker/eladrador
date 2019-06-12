package com.eladrador.test;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Listener;

import com.eladrador.common.Debug;
import com.eladrador.common.GameManager;
import com.eladrador.common.character.NonPlayerCharacter;
import com.eladrador.common.quest.Quest;
import com.eladrador.common.quest.QuestObjective;
import com.eladrador.common.quest.QuestObjectiveChain;
import com.eladrador.common.quest.QuestPhase;
import com.eladrador.test.character.Farmer;
import com.google.gson.Gson;

public class TestQuestListener implements Listener {

	private NonPlayerCharacter farmer;
	private static World world = GameManager.worldForName("world");
	private static final Location FARMER_SPAWN_LOC = new Location(world, 46, 69, 236);
	private static final Location MONSTER_SPAWN_LOC = new Location(world, 46, 69, 245);

	public TestQuestListener() {
		Quest quest = quest();
		quest.initialize();

		// A farmer who asks the player to slay 3 monsters (there's 1 that respawns)
		// and then return. The farmer grants the player a new weapon upon return in
		// addition to currency and exp.
		farmer = new Farmer(FARMER_SPAWN_LOC);
		farmer.setSpawning(true);
	}

	private Quest quest() {
		QuestObjective phase0Chain0Objective0 = new QuestObjective("Speak to the farmer", 1, FARMER_SPAWN_LOC);
		QuestObjective[] phase0Chain0Objectives = { phase0Chain0Objective0 };
		QuestObjectiveChain phase0Chain0 = new QuestObjectiveChain(phase0Chain0Objectives);
		QuestObjectiveChain[] phase0Chains = { phase0Chain0 };
		QuestPhase phase0 = new QuestPhase(phase0Chains);

		QuestObjective phase1Chain0Objective0 = new QuestObjective("Slay the monsters", 3, MONSTER_SPAWN_LOC);
		QuestObjective[] phase1Chain0Objectives = { phase1Chain0Objective0 };
		QuestObjectiveChain phase1Chain0 = new QuestObjectiveChain(phase1Chain0Objectives);
		QuestObjectiveChain[] phase1Chains = { phase1Chain0 };
		QuestPhase phase1 = new QuestPhase(phase1Chains);

		QuestObjective phase2Chain0Objective0 = new QuestObjective("Return to the farmer", 1, FARMER_SPAWN_LOC);
		QuestObjective[] phase2Chain0Objectives = { phase2Chain0Objective0 };
		QuestObjectiveChain phase2Chain0 = new QuestObjectiveChain(phase2Chain0Objectives);
		QuestObjectiveChain[] phase2Chains = { phase2Chain0 };
		QuestPhase phase2 = new QuestPhase(phase2Chains);

		QuestPhase[] phases = { phase0, phase1, phase2 };
		Quest quest = new Quest("Test Quest", 1, phases);

		return quest;
	}

}
