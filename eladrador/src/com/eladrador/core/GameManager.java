package com.eladrador.core;

import com.eladrador.common.AbstractGameManager;
import com.eladrador.common.GPlugin;

public class GameManager extends AbstractGameManager {

	@Override
	public void onEnable() {
		registerWorld("world");
		PCManageListener.instance = new PCManageListener();
		GPlugin.registerEvents(PCManageListener.instance);
	}

	@Override
	public void onDisable() {
		// save data, reset worlds...
	}

}
