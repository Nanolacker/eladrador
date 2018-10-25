package com.eladrador.test.aabb;

import org.bukkit.Location;
import org.bukkit.Particle;

import com.eladrador.common.collision.AABB;

public class HitTargetAABB extends AABB {

	public HitTargetAABB(Location center, double lengthX, double lengthY, double lengthZ) {
		super(center, lengthX, lengthY, lengthZ);
		setDrawMode(AABBDrawMode.FILL);
		setDrawingEnabled(true);
		setDrawParticle(Particle.SPELL_WITCH);
	}

	@Override
	protected void onCollisionEnter(AABB other) {
		setDrawParticle(Particle.DAMAGE_INDICATOR);
		setDrawMode(AABBDrawMode.FILL);
	}

	@Override
	protected void onCollisionExit(AABB other) {
		setDrawParticle(Particle.SPELL_WITCH);
		setDrawMode(AABBDrawMode.WIREFRAME);
	}

}
