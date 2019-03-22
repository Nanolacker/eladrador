package com.eladrador.test.character;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

import com.eladrador.common.character.AbstractCharacter;
import com.eladrador.common.character.Damager;
import com.eladrador.common.physics.Collider;
import com.eladrador.common.physics.Collider.ColliderDrawMode;
import com.eladrador.common.scheduling.RepeatingTask;
import com.eladrador.common.sound.Noise;

public class SimpleNPC extends AbstractCharacter implements Cloneable {

	private Collider aabb;
	private Particle drawParticle = Particle.CRIT;

	public static double speed = 0;

	public SimpleNPC(Location loc) {
		super(ChatColor.RED + "The Big Bad Bounding Box", 1, 25, loc);
		aabb = new Collider(loc, 4, 4, 4) {

			protected void onCollisionEnter(Collider other) {
				damage(5, null);
				setDrawParticle(Particle.DAMAGE_INDICATOR);
				Noise painSound = new Noise(Sound.BLOCK_ANVIL_PLACE);
				painSound.play(location);
			}

			protected void onCollisionExit(Collider other) {
				setDrawParticle(drawParticle);
			}

		};
		move();
	}

	private void move() {
		RepeatingTask mover = new RepeatingTask(0.1) {

			int i = 0;

			@Override
			protected void run() {
				setLocation(getLocation().add(0, 0, speed));
				if (i % 50 == 0) {
					speed = -speed;
				}
				i++;
			}

		};
		mover.start();
	}

	@Override
	public void setLocation(Location location) {
		super.setLocation(location);
		aabb.setCenter(location);
	}

	@Override
	public void spawn() {
		super.spawn();
		aabb.setDrawParticle(drawParticle);
		aabb.setDrawMode(ColliderDrawMode.WIREFRAME);
		aabb.setDrawingEnabled(true);
		aabb.setActive(true);
	}

	@Override
	public void die(Damager killer) {
		super.die(killer);
		aabb.setActive(false);
		aabb.setDrawingEnabled(false);
		location.getWorld().createExplosion(location, 25);
		// new SimpleNPC(location.clone().add(0, 0, 5)).spawn();
		// spawn();
	}

	@Override
	protected Location getNameplateLocation() {
		return location.clone().add(0, 2.5, 0);
	}

}
