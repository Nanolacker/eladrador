package com.eladrador.common.physics;

import org.bukkit.Location;

public final class RaycastInfo {

	private final Collider collider;
	private final Location intersection;
	private final double distance;

	RaycastInfo(Collider collider, Location intersection) {
		this.collider = collider;
		this.intersection = intersection;
		distance = collider.getCenter().distance(intersection);
	}

	/**
	 * Returns the Collider that the Ray intersected.
	 */
	public Collider getCollider() {
		return collider;
	}

	/**
	 * Returns the Location at which the ray intersected a Collider.
	 */
	public Location getIntersection() {
		return intersection;
	}

	/**
	 * Returns the distance between the origin of the ray and the hit location.
	 */
	public double getDistance() {
		return distance;
	}

}
