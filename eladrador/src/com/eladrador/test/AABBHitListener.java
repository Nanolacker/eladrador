package com.eladrador.test;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eladrador.common.collision.AABB;
import com.eladrador.common.scheduling.DelayedTask;

public class AABBHitListener implements Listener {

	@EventHandler
	private void onHit(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		Location center = p.getLocation().add(0, 1.5, 2.5);
		AABB hitter = new AABB(center, 1, 2, 1) {

			public void onCollisionEnter(AABB other) {

			}

			public void onCollisionExit(AABB other) {

			}

		};
		hitter.setActive(true);
		hitter.setDrawingEnabled(true);
		DelayedTask dt = new DelayedTask(new Runnable() {

			public void run() {
				hitter.setActive(false);
				hitter.setDrawingEnabled(false);
			}

		}, 0.2);
		dt.start();
	}

}
