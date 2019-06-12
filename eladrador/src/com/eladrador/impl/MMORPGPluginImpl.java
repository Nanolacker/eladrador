package com.eladrador.impl;

import com.eladrador.common.GameManager;
import com.eladrador.common.MMORPGPlugin;
import com.eladrador.test.TestQuestListener;

public class MMORPGPluginImpl extends MMORPGPlugin {

	@Override
	protected void onMMORPGStart() {
		GameManager.registerWorld("world");
		registerEvents(new PlayerCharacterManageListener());
		registerEvents(new TestQuestListener());
	}

	@Override
	protected void onMMORPGStop() {
		// TODO Auto-generated method stub
	}

}
