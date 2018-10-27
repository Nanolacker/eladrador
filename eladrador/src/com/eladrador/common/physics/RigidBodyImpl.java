package com.eladrador.common.physics;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Represents an axis-aligned rigid body that is subject to the laws of physics.
 */
public class RigidBodyImpl implements RigidBody {

	/**
	 * The {@code Location} of this {@code RigidBody}.
	 */
	private Location location;
	/**
	 * The mass of this {@code RigidBody}.
	 */
	private double mass;
	/**
	 * The speed at which this {@code RigidBody} travels when it will no longer be
	 * accelerated by gravity.
	 */
	private double terminalSpeed;
	/**
	 * The velocity of this {@code RigidBody}.
	 */
	private Vector velocity;

	public RigidBodyImpl(Location location, double mass) {
		this.location = location;
		this.mass = mass;
	}

	/**
	 * Applies a force to this {@code RigidBody}. Each component of {@code force} is
	 * treated as if it were in newtons.
	 */
	@Override
	public void applyForce(Vector force) {
		
	}

	@Override
	public double getMass() {
		return mass;
	}

	@Override
	public void setMass(double mass) {
		this.mass = mass;
	}

	@Override
	public Vector getVelocity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setVelocity(Vector velocity) {
		// TODO Auto-generated method stub

	}

}
