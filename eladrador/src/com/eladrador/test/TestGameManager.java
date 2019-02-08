package com.eladrador.test;

import com.eladrador.common.AbstractGameManager;
import com.eladrador.common.GPlugin;
import com.eladrador.test.listeners.MenuTestListener;

public class TestGameManager extends AbstractGameManager {

	@Override
	public void onEnable() {
		registerWorld("world");
		GPlugin.registerEvents(new MenuTestListener());
	}

	@Override
	public void onDisable() {
	}

}
