package com.eladrador.test.aabb;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eladrador.common.physics.Collider;
import com.eladrador.common.physics.Collider.ColliderDrawMode;
import com.eladrador.common.scheduling.DelayedTask;
import com.eladrador.common.scheduling.RepeatingTask;

public class AABBHitListener implements Listener {

	@EventHandler
	private void onHit(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		Location pLoc = p.getLocation();
		Location center = pLoc.add(pLoc.getDirection().multiply(10)).add(0, 1.5, 0);
		World world = pLoc.getWorld();
		RepeatingTask lightning = new RepeatingTask(0.3) {
			public void run() {
				world.strikeLightning(center);
			}
		};
		//lightning.start();
		DelayedTask stop = new DelayedTask(2) {
			public void run() {
				lightning.stop();
			}
		};
		//stop.start();
		Collider hitter = new Collider(center, 1, 1, 1) {

			public void onCollisionEnter(Collider other) {

			}

			public void onCollisionExit(Collider other) {

			}

		};
		hitter.setDrawParticle(Particle.FLAME);
		hitter.setDrawMode(ColliderDrawMode.FILL);
		hitter.setActive(true);
		hitter.setDrawingEnabled(true);

		DelayedTask dt = new DelayedTask(0.25) {

			public void run() {
				hitter.setActive(false);
				hitter.setDrawingEnabled(false);
			}

		};
		dt.start();
	}

}
