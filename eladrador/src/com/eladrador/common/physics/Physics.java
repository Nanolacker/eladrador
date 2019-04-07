package com.eladrador.common.physics;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import com.eladrador.common.Debug;

public final class Physics {

	private Physics() {
	};

	/**
	 * This only works properly with fairly short rays. A different algorithm will
	 * need to be implemented in the future to resolve this issue.
	 * 
	 * @param ray the ray used for the raycast
	 */
	public static RaycastInfo raycast(Ray ray) {
		return raycast(ray, Collider.class);
	}

	/**
	 * This only works properly with fairly short rays. A different algorithm will
	 * need to be implemented in the future to resolve this issue.
	 * 
	 * @param ray                 the ray used for the raycast
	 * @param targetColliderTypes the type of collider (including its subclasses)
	 *                            that will be tested in this raycast (any other
	 *                            type of collider will be ignored)
	 */
	public static RaycastInfo raycast(Ray ray, Class<?> targetColliderType) {
		Location origin = ray.getOrigin();
		World world = origin.getWorld();
		Vector dirAndLength = ray.getDirectionAndLength();
		Location end = origin.clone().add(dirAndLength);

		int colliderBucketXMin = (int) (origin.getX() / ColliderBucket.BUCKET_SIZE);
		int colliderBucketYMin = (int) (origin.getY() / ColliderBucket.BUCKET_SIZE);
		int colliderBucketZMin = (int) (origin.getZ() / ColliderBucket.BUCKET_SIZE);

		int colliderBucketXMax = (int) (end.getX() / ColliderBucket.BUCKET_SIZE);
		int colliderBucketYMax = (int) (end.getY() / ColliderBucket.BUCKET_SIZE);
		int colliderBucketZMax = (int) (end.getZ() / ColliderBucket.BUCKET_SIZE);

		Collider hitCollider = null;
		Location hitIntersection = null;
		double prevDistanceBetweenOriginAndIntersection = Double.MAX_VALUE;
		for (int xCount = colliderBucketXMin; xCount <= colliderBucketXMax; xCount++) {
			for (int yCount = colliderBucketYMin; yCount <= colliderBucketYMax; yCount++) {
				for (int zCount = colliderBucketZMin; zCount <= colliderBucketZMax; zCount++) {
					Location bucketAddress = new Location(world, xCount, yCount, zCount);
					ColliderBucket bucket = ColliderBucket.bucketByAddress(bucketAddress);
					if (bucket != null) {
						ArrayList<Collider> colliders = bucket.getEncompassedColliders();
						for (Collider collider : colliders) {
							if (targetColliderType.isInstance(collider)) {
								Location intersection = ray.intersection(collider);
								if (intersection != null) {
									double distanceBetweenOriginAndIntersection = collider.getCenter()
											.distance(ray.getOrigin());
									if (distanceBetweenOriginAndIntersection < prevDistanceBetweenOriginAndIntersection) {
										hitCollider = collider;
										hitIntersection = intersection;
										prevDistanceBetweenOriginAndIntersection = distanceBetweenOriginAndIntersection;
									}
								}
							}
						}
					}
				}
			}
		}
		if (hitCollider != null) {
			return new RaycastInfo(hitCollider, hitIntersection);
		}
		return null;
	}

}
