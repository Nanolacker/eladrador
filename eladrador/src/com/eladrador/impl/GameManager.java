package com.eladrador.impl;

import com.eladrador.common.AbstractGameManager;
import com.eladrador.common.MMORPGPlugin;

public class GameManager extends AbstractGameManager {

	@Override
	public void onEnable() {
		registerWorld("world");
		MMORPGPlugin.registerEvents(new PlayerCharacterManageListener());
	}

	@Override
	public void onDisable() {
		// save data, reset worlds...
	}

}
