package com.eladrador.test.character;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;

import com.eladrador.common.collision.AABB;
import com.eladrador.common.collision.AABB.AABBDrawMode;
import com.eladrador.common.ui.TextPanel;


public class SimpleNPC {

	private double health;
	private double maxHealth;
	private AABB aabb;
	private TextPanel nameplate;

	public SimpleNPC(Location loc, double maxHealth) {
		health = maxHealth;
		this.maxHealth = maxHealth;
		aabb = new AABB(loc, 5, 5, 5) {

			protected void onCollisionEnter(AABB other) {
				damage(5);
				setDrawParticle(Particle.DAMAGE_INDICATOR);
			}

			protected void onCollisionExit(AABB other) {
				setDrawParticle(AABB.DEFAULT_DRAW_PARTICLE);
			}
		};
		aabb.setDrawMode(AABBDrawMode.FILL);
		aabb.setDrawingEnabled(true);
		aabb.setActive(true);
		nameplate = new TextPanel("", 25, new Location(loc.getWorld(), loc.getX(), loc.getY() + 1.5, loc.getZ()));
		nameplate.setEnabled(true);
		updateNameplate();
	}

	public void damage(double dmg) {
		health -= dmg;
		if (health <= 0) {
			die();
		} else {
			updateNameplate();
		}
	}

	private void updateNameplate() {
		nameplate.setText("The Big Bad Box\n" + ChatColor.RED + "Health: " + health + "/" + maxHealth);
	}

	private void die() {
		aabb.setActive(false);
		aabb.setDrawingEnabled(false);
		nameplate.setText(ChatColor.GRAY + "*DEAD*");
	}

}
