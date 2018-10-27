package com.eladrador.test;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.eladrador.common.AbstractGameManager;
import com.eladrador.common.Debug;
import com.eladrador.common.GPlugin;
import com.eladrador.test.aabb.HitTargetAABB;
import com.eladrador.test.character.SimpleNPC;

public class TestGameManager extends AbstractGameManager {

	@Override
	public void onEnable() {
		registerWorld("world");
		World world = super.getWorlds().get(0);
		Location loc1 = new Location(world, 0, 100, 0);
		HitTargetAABB toHit = new HitTargetAABB(loc1, 5, 5, 5);
		toHit.setActive(true);

		GPlugin.registerEvents(new AABBHitListener());
		// GPlugin.registerEvents(new TextPanelListener());

		Player player = Debug.getFirstPlayerOnline();

		SimpleNPC npc = new SimpleNPC(player.getLocation(), 50);
	}

	@Override
	public void onDisable() {

	}

}
