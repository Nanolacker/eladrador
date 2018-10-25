package com.eladrador.test.testQuest.phases;

import com.eladrador.common.quest.QuestPhase;
import com.eladrador.test.testQuest.chains.TestQuestPhase1Chain1;

public class TestQuestPhase1 extends QuestPhase {

	public static TestQuestPhase1 inst = new TestQuestPhase1();

	private TestQuestPhase1() {
		registerObjChain(TestQuestPhase1Chain1.inst);
	}

}
