package com.eladrador.test;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eladrador.common.collision.AABB;
import com.eladrador.common.collision.AABB.AABBDrawMode;
import com.eladrador.common.scheduling.DelayedTask;

public class AABBHitListener implements Listener {

	@EventHandler
	private void onHit(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		Location center = p.getLocation().add(0, 1.5, 7);
		AABB hitter = new AABB(center, 0.5, 0.5, 0.5) {

			public void onCollisionEnter(AABB other) {
				
			}

			public void onCollisionExit(AABB other) {

			}

		};
		hitter.setDrawParticle(Particle.LAVA);
		hitter.setDrawMode(AABBDrawMode.FILL);
		hitter.setActive(true);
		hitter.setDrawingEnabled(true);
		DelayedTask dt = new DelayedTask(new Runnable() {

			public void run() {
				hitter.setActive(false);
				hitter.setDrawingEnabled(false);
			}

		}, 0.25);
		dt.start();
	}

}
