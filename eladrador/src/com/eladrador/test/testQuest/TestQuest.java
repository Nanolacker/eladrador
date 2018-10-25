package com.eladrador.test.testQuest;

import com.eladrador.common.quest.Quest;
import com.eladrador.test.testQuest.phases.TestQuestPhase1;

public class TestQuest extends Quest {

	public static TestQuest inst = new TestQuest();

	private TestQuest() {
		super("Test", 1, 0);
		registerPhase(TestQuestPhase1.inst);
	}

}
