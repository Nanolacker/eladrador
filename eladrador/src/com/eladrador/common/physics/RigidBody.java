package com.eladrador.common.physics;

import org.bukkit.util.Vector;

/**
 * Represents an axis-aligned rigid body that is subject to the laws of physics.
 */
public interface RigidBody {

	public double getMass();

	public void setMass(double mass);

	public Vector getVelocity();

	public void setVelocity(Vector velocity);

	void applyForce(Vector force);

}
