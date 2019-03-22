package com.eladrador.test.aabb;

import org.bukkit.Location;
import org.bukkit.Particle;

import com.eladrador.common.physics.Collider;

public class HitTargetAABB extends Collider {

	public HitTargetAABB(Location center, double lengthX, double lengthY, double lengthZ) {
		super(center, lengthX, lengthY, lengthZ);
		setDrawingEnabled(true);
		setDrawParticle(Particle.CRIT);
	}

	@Override
	protected void onCollisionEnter(Collider other) {
		setDrawParticle(Particle.DAMAGE_INDICATOR);
	}

	@Override
	protected void onCollisionExit(Collider other) {
		setDrawParticle(Particle.CRIT);
	}

}
