package com.eladrador.test;

import org.bukkit.Location;
import org.bukkit.World;

import com.eladrador.common.AbstractGameManager;
import com.eladrador.common.Debug;
import com.eladrador.common.GPlugin;
import com.eladrador.common.scheduling.DelayedTask;
import com.eladrador.common.scheduling.RepeatingTask;
import com.eladrador.test.aabb.HitTargetAABB;

public class TestGameManager extends AbstractGameManager {

	@Override
	public void onEnable() {
		registerWorld("world");
		World world = super.getWorlds().get(0);
		Location loc1 = new Location(world, 0, 100, 0);
		HitTargetAABB toHit = new HitTargetAABB(loc1, 5, 5, 5);
		toHit.setActive(true);

		GPlugin.registerEvents(new AABBHitListener());

		RepeatingTask testTask = new RepeatingTask(new Runnable() {
			public void run() {
				Debug.log("asdf");
			}
		}, 1);
		testTask.start();

		DelayedTask stop = new DelayedTask(new Runnable() {
			public void run() {
				testTask.stop();
			}
		}, 4.3553);
		stop.start();
	}

	@Override
	public void onDisable() {

	}

}
