package com.eladrador.test.aabb;

import org.bukkit.Location;
import org.bukkit.Particle;

import com.eladrador.common.collision.AABB;

public class HitTargetAABB extends AABB {

	public HitTargetAABB(Location center, double lengthX, double lengthY, double lengthZ) {
		super(center, lengthX, lengthY, lengthZ);
		setDrawingEnabled(true);
		setDrawParticle(AABB.DEFAULT_DRAW_PARTICLE);
	}

	@Override
	protected void onCollisionEnter(AABB other) {
		setDrawParticle(Particle.DAMAGE_INDICATOR);
	}

	@Override
	protected void onCollisionExit(AABB other) {
		setDrawParticle(AABB.DEFAULT_DRAW_PARTICLE);
	}

}
