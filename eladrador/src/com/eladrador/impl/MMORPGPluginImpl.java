package com.eladrador.impl;

import org.bukkit.Location;
import org.bukkit.World;

import com.eladrador.common.GameManager;
import com.eladrador.common.MMORPGPlugin;
import com.eladrador.common.character.NonPlayerCharacter;
import com.eladrador.test.character.Farmer;

public class MMORPGPluginImpl extends MMORPGPlugin {

	@Override
	protected void onMMORPGStart() {
		GameManager.registerWorld("world");
		registerEvents(new PlayerCharacterManageListener());

		// A farmer who asks the player to slay 3 monsters (there's 1 that respawns)
		// and then return. The farmer grants the player a new weapon upon return in
		// addition to currency and exp.

		World world = GameManager.worldForName("world");

		Location farmerLoc = new Location(world, 46, 69, 236);
		NonPlayerCharacter farmer = new Farmer(farmerLoc);
		farmer.setSpawning(true);
	}

	@Override
	protected void onMMORPGStop() {
		// TODO Auto-generated method stub

	}

}
