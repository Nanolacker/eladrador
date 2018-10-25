package com.eladrador.common.quest;

import java.util.ArrayList;

public abstract class QuestPhase {

	Quest parent;
	int index;
	private ArrayList<QuestObjectiveChain> objChains;
	private int objChainCount;

	protected QuestPhase() {
		objChains = new ArrayList<QuestObjectiveChain>();
		objChainCount = 0;
	}

	public ArrayList<QuestObjectiveChain> getObjChains() {
		return objChains;
	}

	protected void registerObjChain(QuestObjectiveChain objChain) {
		objChain.index = objChainCount;
		objChainCount++;
		objChains.add(objChain);
	}

}
