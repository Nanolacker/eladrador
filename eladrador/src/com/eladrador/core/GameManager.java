package com.eladrador.core;

import com.eladrador.common.AbstractGameManager;
import com.eladrador.common.GPlugin;
import com.eladrador.core.playerCharacterManagement.PlayerCharacterManageListener;

public class GameManager extends AbstractGameManager {

	@Override
	public void onEnable() {
		registerWorld("world");
		PlayerCharacterManageListener.instance = new PlayerCharacterManageListener();
		GPlugin.registerEvents(PlayerCharacterManageListener.instance);
	}

	@Override
	public void onDisable() {
		// save data, reset worlds...
	}

}
