package com.eladrador.test.testQuest.chains;

import com.eladrador.common.quest.QuestObjectiveChain;
import com.eladrador.test.testQuest.objs.TestQuestPhase1Chain1Obj1;

public class TestQuestPhase1Chain1 extends QuestObjectiveChain {

	public static TestQuestPhase1Chain1 inst = new TestQuestPhase1Chain1();

	private TestQuestPhase1Chain1() {
		registerObj(TestQuestPhase1Chain1Obj1.inst);
	}

}
