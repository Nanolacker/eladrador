package com.eladrador.common.quest;

import java.util.ArrayList;

public abstract class QuestObjectiveChain {

	QuestPhase parent;
	int index;
	private ArrayList<QuestObjective> objs;
	private int objCount;

	protected QuestObjectiveChain() {
		objs = new ArrayList<QuestObjective>();
		objCount = 0;
	}

	public ArrayList<QuestObjective> getObjs() {
		return objs;
	}

	protected void registerObj(QuestObjective obj) {
		obj.index = objCount;
		objCount++;
		objs.add(obj);
	}

}
