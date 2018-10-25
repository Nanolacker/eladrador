package com.eladrador.common.collision;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;

/**
 * Stores axis-aligned bounding boxes organized based on location to increase
 * collision detection efficiency and reduce the number of unnecessary
 * calculations. AABBBuckets are represented as cubes lined up face to face,
 * which each cover a portion of 3-D space.
 */
final class AABBBucket {

	/**
	 * The length that all AABBBuckets will have on all axes.
	 */
	static final int BUCKET_SIZE = 25;

	/**
	 * A map of Locations to AABBBuckets. The Location keys are not Locations in the
	 * traditional sense. Instead they function as addresses that don't exactly
	 * represent world space like a standard Location. Their coordinate values are
	 * all integers and represent the number of AABBBuckets, whose size is equal to
	 * BUCKET_SIZE, that this AABBBucket is away from the origin are on an axis.
	 */
	private static HashMap<Location, AABBBucket> bucketMap;

	static {
		bucketMap = new HashMap<Location, AABBBucket>();
	}

	/**
	 * The axis-aligned bounding boxes that are encompassed entirely or partially by
	 * this bucket.
	 */
	private ArrayList<AABB> encompassedAABBs;

	private Location address;

	private AABBBucket(Location address) {
		this.address = address;
		encompassedAABBs = new ArrayList<AABB>();
	}

	static AABBBucket bucketByAddress(Location address) {
		return bucketMap.get(address);
	}

	static AABBBucket createNewBucket(Location bucketAddress) {
		AABBBucket bucket = new AABBBucket(bucketAddress);
		bucketMap.put(bucketAddress, bucket);
		return bucket;
	}

	static void deleteBucket(Location address) {
		bucketMap.remove(address);
	}

	Location getAddress() {
		return address;
	}

	void encompassAABB(AABB aabb) {
		encompassedAABBs.add(aabb);
	}

	void removeAABB(AABB aabb) {
		encompassedAABBs.remove(aabb);
	}

	ArrayList<AABB> getEncompassedAABBs() {
		return encompassedAABBs;
	}

}
