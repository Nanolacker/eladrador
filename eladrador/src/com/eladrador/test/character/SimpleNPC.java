package com.eladrador.test.character;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;

import com.eladrador.common.character.AbstractCharacter;
import com.eladrador.common.character.Damager;
import com.eladrador.common.collision.AABB;
import com.eladrador.common.collision.AABB.AABBDrawMode;
import com.eladrador.common.scheduling.RepeatingTask;

public class SimpleNPC extends AbstractCharacter {

	private AABB aabb;
	private Particle drawParticle = Particle.BUBBLE_POP;

	public static final double SPEED = 1;

	public SimpleNPC(Location loc, double maxHealth) {
		super(ChatColor.RED + "The Big Bad Box", 1, maxHealth, loc);
		aabb = new AABB(loc, 5, 5, 5) {

			protected void onCollisionEnter(AABB other) {
				damage(5, null);
				setDrawParticle(Particle.DAMAGE_INDICATOR);
			}

			protected void onCollisionExit(AABB other) {
				setDrawParticle(drawParticle);
			}

		};
		move();
	}

	private void move() {
		RepeatingTask mover = new RepeatingTask(0.1) {

			@Override
			protected void run() {
				setLocation(getLocation().add(0, 0, SPEED));
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
		aabb.setDrawMode(AABBDrawMode.WIREFRAME);
		aabb.setDrawingEnabled(true);
		aabb.setActive(true);
	}

	@Override
	public void die(Damager killer) {
		super.die(killer);
		aabb.setActive(false);
		aabb.setDrawingEnabled(false);
	}

	@Override
	protected void updateNameplatePosition() {
		nameplate.setLocation(location.clone().add(0, 3, 0));
	}

}
