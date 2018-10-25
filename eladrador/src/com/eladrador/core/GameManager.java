package com.eladrador.core;

import com.eladrador.common.AbstractGameManager;
import com.eladrador.common.GPlugin;

public class GameManager extends AbstractGameManager {

	@Override
	public void onEnable() {
		PcManageListener.instance = new PcManageListener();
		GPlugin.registerEvents(PcManageListener.instance);
	}

	@Override
	public void onDisable() {
		// save data, reset worlds...
	}

}
