package com.eladrador.common.collision;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.bukkit.ChatColor;

import com.eladrador.common.Debug;
import com.eladrador.common.scheduling.RepeatingTask;
import com.eladrador.common.utils.MathUtils;

/**
 * Represents an axis-aligned bounding box.
 */
public abstract class AABB {
	/**
	 * The {@code Particle} used to draw {@code AABB}s.
	 */
	public static final Particle DEFAULT_DRAW_PARTICLE = Particle.CRIT;
	/**
	 * The period by which {@code AABB}s will be drawn.
	 */
	public static final double DRAW_PERIOD = 0.1;
	/**
	 * How thick drawings of {@code AABB}s will be. The greater this value, the less
	 * space there is between particles used to draw AABBs.
	 */
	public static final double DRAW_THICKNESS = 4.0;

	/**
	 * Whether this {@code AABB} will collide and respond to other {@code AABB}s.
	 */
	private boolean active;
	/**
	 * The {@code World} that this {@code AABB} exists in.
	 */
	private World world;
	/**
	 * Represents the bounds of this {@code AABB}.
	 */
	private double xMin, xMax, yMin, yMax, zMin, zMax;
	/**
	 * The {@code Location} representing the point that exists at the center of this
	 * {@code AABB}.
	 */
	private Location center;
	/**
	 * Represents the dimensions (i.e. lengths of this {@code AABB}).
	 */
	private Vector dimensions;
	/**
	 * Stores any miscellaneous data pertaining to this {@code AABB} as
	 * {@code String}s.
	 */
	private ArrayList<String> tags;
	/**
	 * Whether this {@code AABB} should be "drawn" in its Minecraft world using
	 * particles to visualize its location and size.
	 */
	private boolean drawingEnabled;
	/**
	 * The type of pattern in which this {@code AABB} will be drawn when
	 * {@code drawingEnabled == true}. The default mode is
	 * {@code AABBDrawMode.WIREFRAME}.
	 */
	private AABBDrawMode drawMode;
	/**
	 * The {@code Particle} that will be used to draw this {@code AABB} if
	 * {@code drawingEnabled == true}.
	 */
	private Particle drawParticle;
	/**
	 * The {@code RepeatingTask} that is used to draw this {@code AABB}.
	 */
	private RepeatingTask drawTask;
	/**
	 * The {@code AABBBucket}s that this {@code AABB} occupies.
	 */
	private ArrayList<AABBBucket> occupiedBuckets;
	/**
	 * The {@code AABB}s that this {@code AABB} is currently colliding with.
	 */
	private ArrayList<AABB> collidingAABBs;

	/**
	 * Represents a type of pattern by which {@code AABB}s can be drawn.
	 */
	public static enum AABBDrawMode {
		/**
		 * Results in a wireframe visual representation of an {@code AABB} when an
		 * {@code AABB} is drawn using this mode.
		 */
		WIREFRAME,
		/**
		 * The entirety of {@code AABB} will be filled when it is drawn using this mode.
		 */
		FILL

	}

	/**
	 * Constructs a new axis-aligned bounding box. The {@code max} value on any axis
	 * must be greater than that axis's {@code min} value. Be sure to call
	 * setActive(boolean) to enable it after construction.
	 * 
	 * @param world the world this box will exist in
	 * @param xMin  the minimum x value that exists within this box
	 * @param xMax  the maximum x value that exists within this box
	 * @param yMin  the minimum y value that exists within this box
	 * @param yMax  the maximum y value that exists within this box
	 * @param zMin  the minimum z value that exists within this box
	 * @param zMax  the maximum z value that exists within this box
	 */
	public AABB(World world, double xMin, double xMax, double yMin, double yMax, double zMin, double zMax) {
		this.world = world;
		if (!(xMax > xMin)) {
			throw new IllegalArgumentException(
					"Impossible dimensions. xMax " + "(" + xMax + ") must be greater than xMin (" + xMin + ")");
		}
		if (!(yMax > yMin)) {
			throw new IllegalArgumentException(
					"Impossible dimensions. yMax " + "(" + yMax + ") must be greater than yMin (" + yMin + ")");
		}
		if (!(zMax > zMin)) {
			throw new IllegalArgumentException(
					"Impossible dimensions. zMax " + "(" + zMax + ") must be greater than zMin (" + zMin + ")");
		}
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		this.zMin = zMin;
		this.zMax = zMax;
		dimensions = new Vector();
		updateCenter();
		updateDimensions();
		initFields();
	}

	/**
	 * Constructs a new axis-aligned bounding box. Any length of the box must be
	 * greater than 0. Be sure to call setActive(boolean) to enable it after
	 * construction.
	 * 
	 * @param center  the location, including the world, at the center of this box
	 * @param lengthX the length of this box on the x-axis
	 * @param lengthY the length of this box on the y-axis
	 * @param lengthZ the length of this box on the z-axis
	 */
	public AABB(Location center, double lengthX, double lengthY, double lengthZ) {
		this.world = center.getWorld();
		this.center = center;
		if (lengthX <= 0) {
			throw new IllegalArgumentException(
					"Impossible dimensions. lengthX " + "(" + lengthX + ") must be greater than 0");
		}
		if (lengthY <= 0) {
			throw new IllegalArgumentException(
					"Impossible dimensions. lengthY " + "(" + lengthY + ") must be greater than 0");
		}
		if (lengthZ <= 0) {
			throw new IllegalArgumentException(
					"Impossible dimensions. lengthZ " + "(" + lengthZ + ") must be greater than 0");
		}
		dimensions = new Vector(lengthX, lengthY, lengthZ);
		updateBounds();
		initFields();
	}

	/**
	 * Initializes certain fields of this {@code AABB}. Eliminates redundancy in
	 * constructors.
	 */
	private void initFields() {
		active = false;
		tags = new ArrayList<String>();
		drawingEnabled = false;
		drawMode = AABBDrawMode.WIREFRAME;
		drawParticle = DEFAULT_DRAW_PARTICLE;
		drawTask = null;
		occupiedBuckets = new ArrayList<AABBBucket>();
		collidingAABBs = new ArrayList<AABB>();
	}

	public boolean getActive() {
		return active;
	}

	/**
	 * Sets whether this AABBs will interact with other AABBs.
	 */
	public void setActive(boolean active) {
		this.active = active;
		if (active) {
			updateOccupiedBuckets();
			checkForCollision();
		} else {
			for (int i = 0; i < collidingAABBs.size(); i++) {
				AABB collidingWith = collidingAABBs.get(i);
				onCollisionExit(collidingWith);
				collidingWith.onCollisionExit(this);
			}
		}

	}

	/**
	 * Returns the world this box exists in.
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Sets the world that this box will exist in.
	 * 
	 * @param world the world this box will exist in
	 */
	public void setWorld(World world) {
		this.world = world;
		center.setWorld(world);
		updateOccupiedBuckets();
		if (active) {
			checkForCollision();
		}
	}

	public double getXMin() {
		return xMin;
	}

	public double getXMax() {
		return xMax;
	}

	public double getYMin() {
		return yMin;
	}

	public double getYMax() {
		return yMax;
	}

	public double getZMin() {
		return zMin;
	}

	public double getZMax() {
		return zMax;
	}

	/**
	 * Returns the location of the point that exists at the center of this box.
	 * 
	 * @return the location of the point that exists at the center of this box
	 */
	public Location getCenter() {
		return center;
	}

	/**
	 * Sets the center of this box and updates all bounds accordingly.
	 * 
	 * @param center the new center of the box
	 */
	public void setCenter(Location center) {
		this.center = center;
		world = center.getWorld();
		updateBounds();
		updateOccupiedBuckets();
		if (active) {
			checkForCollision();
		}
	}

	/**
	 * Returns the dimensions of this box.
	 * 
	 * @return the dimensions of this box
	 */
	public Vector getDimensions() {
		return dimensions;
	}

	/**
	 * Sets the dimensions of the box.
	 * 
	 * @param dimensions the new dimensions of this box
	 */
	public void setDimensions(Vector dimensions) {
		this.dimensions = dimensions;
		updateBounds();
		updateOccupiedBuckets();
		if (active) {
			checkForCollision();
		}
	}

	/**
	 * Translates this box.
	 * 
	 * @param translate how much to translate this box
	 */
	public void translate(Vector translate) {
		double x = translate.getX();
		double y = translate.getY();
		double z = translate.getZ();
		xMin += x;
		xMax += x;
		yMin += y;
		yMax += y;
		zMin += z;
		zMax += z;
		updateCenter();
		updateOccupiedBuckets();
		if (active) {
			checkForCollision();
		}
	}

	public void addTag(String tag) {
		tags.add(tag);
	}

	public ArrayList<String> getTags() {
		return tags;
	}

	/**
	 * Returns whether this box encompasses a point (in other words, whether this
	 * point exists within the volume of this box).
	 * 
	 * @param point the point to be checked
	 * @return whether this box encompasses the point
	 */
	public boolean encompasses(Location point) {
		World world = point.getWorld();
		double x = point.getX();
		double y = point.getY();
		double z = point.getZ();
		return world.equals(this.world) && MathUtils.checkInInterval(x, xMin, true, xMax, true)
				&& MathUtils.checkInInterval(y, yMin, true, yMax, true)
				&& MathUtils.checkInInterval(z, zMin, true, zMax, true);
	}

	/**
	 * Enabling drawing will result in a visual representation of this AABB to be
	 * made in game using particles. Useful for debugging.
	 */
	public void setDrawingEnabled(boolean enabled) {
		boolean redundant = this.drawingEnabled == enabled;
		if (redundant) {
			return;
		}
		this.drawingEnabled = enabled;
		if (drawingEnabled) {
			if (drawTask == null) {
				setDrawTask();
			}
			drawTask.start();
			String centerDesc = ChatColor.YELLOW + "center = " + center.getWorld().getName() + ", " + center.getX()
					+ ", " + center.getY() + ", " + center.getZ();
			Debug.log(ChatColor.WHITE + "Drawing of AABB (" + ChatColor.WHITE + centerDesc + ChatColor.WHITE
					+ ") has been enabled.");
		} else {
			drawTask.stop();
		}
	}

	public void setDrawMode(AABBDrawMode mode) {
		drawMode = mode;
		if (drawingEnabled) {
			drawTask.stop();
		}
		setDrawTask();
		if (drawingEnabled) {
			drawTask.start();
		}
	}

	/**
	 * Changes the particle used to draw this AABB when drawing is enabled.
	 */
	public void setDrawParticle(Particle particle) {
		drawParticle = particle;
	}

	private void setDrawTask() {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				switch (drawMode) {
				case WIREFRAME:
					drawWireframe();
					break;
				case FILL:
					drawFill();
					break;
				default:
					break;
				}
			}

		};
		drawTask = new RepeatingTask(r, DRAW_PERIOD);
	}

	private void drawWireframe() {
		// distance between particles used to draw
		double spaceDistance = 1 / DRAW_THICKNESS;
		World world = getWorld();
		// represents whether xCount has reached reached its maximum
		boolean xFinished = false;
		for (double xCount = xMin; xCount <= xMax && !xFinished; xCount += spaceDistance) {
			// represents whether yCount has reached reached its maximum
			boolean yFinished = false;
			for (double yCount = yMin; yCount <= yMax && !yFinished; yCount += spaceDistance) {
				// represents whether zCount has reached reached its maximum
				boolean zFinished = false;
				for (double zCount = zMin; zCount <= zMax && !zFinished; zCount += spaceDistance) {
					int validCount = 0;
					if (xCount == xMin) {
						validCount++;
					}
					if (xCount > xMax - spaceDistance) {
						validCount++;
						xFinished = true;
					}
					if (yCount == yMin) {
						validCount++;
					}
					if (yCount > yMax - spaceDistance) {
						validCount++;
						yFinished = true;
					}
					if (zCount == zMin) {
						validCount++;
					}
					if (zCount > zMax - spaceDistance) {
						validCount++;
						zFinished = true;
					}
					boolean validPoint = validCount >= 2;
					if (validPoint) {
						Location point = new Location(world, xCount, yCount, zCount);
						world.spawnParticle(drawParticle, point, 0);
					}
				}
			}
		}
	}

	private void drawFill() {
		// distance between particles used to draw
		double spaceDistance = 1 / DRAW_THICKNESS;
		World world = getWorld();
		for (double xCount = xMin; xCount <= xMax; xCount += spaceDistance) {
			for (double yCount = yMin; yCount <= yMax; yCount += spaceDistance) {
				for (double zCount = zMin; zCount <= zMax; zCount += spaceDistance) {
					Location point = new Location(world, xCount, yCount, zCount);
					world.spawnParticle(drawParticle, point, 0);
				}
			}
		}
	}

	/**
	 * Center and dimensions must be current to update properly.
	 */
	private void updateBounds() {
		double xMid = center.getX();
		double halfLengthX = dimensions.getX() / 2;
		xMin = xMid - halfLengthX;
		xMax = xMid + halfLengthX;
		double yMid = center.getY();
		double halfLengthY = dimensions.getY() / 2;
		yMin = yMid - halfLengthY;
		yMax = yMid + halfLengthY;
		double zMid = center.getZ();
		double halfLengthZ = dimensions.getZ() / 2;
		zMin = zMid - halfLengthZ;
		zMax = zMid + halfLengthZ;
	}

	/**
	 * Bounds must be current to update properly.
	 */
	private void updateCenter() {
		double xMid = MathUtils.midpoint(xMin, xMax);
		double yMid = MathUtils.midpoint(yMin, yMax);
		double zMid = MathUtils.midpoint(zMin, zMax);
		center = new Location(world, xMid, yMid, zMid);
	}

	/**
	 * Up dates the dimensions (i.e. lengths) of this {@code AABB}. Bounds must be
	 * current to update properly.
	 */
	private void updateDimensions() {
		dimensions.setX(xMax - xMin);
		dimensions.setY(yMax - yMin);
		dimensions.setZ(zMax - zMin);
	}

	/**
	 * Determines what {@code AABBBucket}s this {@code AABB} should exist in to
	 * ensure efficient and accurate collision detection. Bounds must be current to
	 * update properly.
	 */
	private void updateOccupiedBuckets() {
		ArrayList<AABBBucket> occupiedBucketsOld = new ArrayList<AABBBucket>(occupiedBuckets);
		occupiedBuckets.clear();
		int bucketSize = AABBBucket.BUCKET_SIZE;

		int bucketXMin = (int) (xMin / bucketSize);
		int bucketYMin = (int) (yMin / bucketSize);
		int bucketZMin = (int) (zMin / bucketSize);

		int bucketXMax = (int) (xMax / bucketSize);
		int bucketYMax = (int) (yMax / bucketSize);
		int bucketZMax = (int) (zMax / bucketSize);

		for (int xCount = bucketXMin; xCount <= bucketXMax; xCount++) {
			for (int yCount = bucketYMin; yCount <= bucketYMax; yCount++) {
				for (int zCount = bucketZMin; zCount <= bucketZMax; zCount++) {
					Location bucketAddress = new Location(world, xCount, yCount, zCount);
					AABBBucket bucket = AABBBucket.bucketByAddress(bucketAddress);
					if (bucket == null) {
						bucket = AABBBucket.createNewBucket(bucketAddress);
					}
					boolean alreadyEncompassed = bucket.getEncompassedAABBs().contains(this);
					if (!alreadyEncompassed) {
						bucket.encompassAABB(this);
					}
					occupiedBuckets.add(bucket);
				}
			}
		}

		for (int i = 0; i < occupiedBucketsOld.size(); i++) {
			AABBBucket bucket = occupiedBucketsOld.get(i);
			if (!occupiedBuckets.contains(bucket)) {
				bucket.removeAABB(this);
				if (bucket.getEncompassedAABBs().isEmpty()) {
					Location address = bucket.getAddress();
					AABBBucket.deleteBucket(address);
				}
			}
		}
	}

	/**
	 * Detects the presence and and absense of collisions between this {@code AABB}
	 * and other {@code AABB}s and responds appropriately.
	 */
	private void checkForCollision() {
		for (int i = 0; i < occupiedBuckets.size(); i++) {
			AABBBucket bucket = occupiedBuckets.get(i);
			ArrayList<AABB> neighboringAABBs = bucket.getEncompassedAABBs();
			for (int j = 0; j < neighboringAABBs.size(); j++) {
				AABB neighboringAABB = neighboringAABBs.get(j);
				if (neighboringAABB != this && neighboringAABB.getActive() == true) {
					boolean collides = collides(neighboringAABB);
					if (collidingAABBs.contains(neighboringAABB)) {
						if (!collides) {
							handleCollisionExit(neighboringAABB);
						}
					} else {
						if (collides) {
							handleCollisionEnter(neighboringAABB);
						}
					}
				}
			}
		}
		outerloop: for (int i = 0; i < collidingAABBs.size(); i++) {
			AABB collidingAABB = collidingAABBs.get(i);
			for (int j = 0; j < occupiedBuckets.size(); j++) {
				AABBBucket bucket = occupiedBuckets.get(j);
				if (bucket.getEncompassedAABBs().contains(collidingAABB)) {
					continue outerloop;
				}
			}
			handleCollisionExit(collidingAABB);
		}
	}

	/**
	 * Responds to this {@code AABB} and another {@code AABB} colliding with each
	 * other. Called when two {@code AABB}s that were colliding no longer overlap
	 * each other. {@code onCollisionEnter(AABB)} is called from each of the
	 * {@code AABB}s.
	 * 
	 * @param other the other {@code AABB} in the collision
	 */
	private void handleCollisionEnter(AABB other) {
		this.collidingAABBs.add(other);
		other.collidingAABBs.add(this);
		this.onCollisionEnter(other);
		other.onCollisionEnter(this);
	}

	/**
	 * Responds to this {@code AABB} and another {@code AABB} retracting from a
	 * collision. Called when two {@code AABB}s that were colliding no longer
	 * overlap each other. {@code onCollisionExit(AABB)} is called from each of the
	 * {@code AABB}s.
	 * 
	 * @param other the other {@code AABB} in the collision
	 */
	private void handleCollisionExit(AABB other) {
		collidingAABBs.remove(other);
		other.collidingAABBs.remove(this);
		this.onCollisionExit(other);
		other.onCollisionExit(this);
	}

	/**
	 * Returns whether this AABB collides with another AABB.
	 * 
	 * @param other the other AABB
	 * @return whether the two AABBs are colliding
	 */
	private boolean collides(AABB other) {
		return (this.getXMin() <= other.getXMax() && this.getXMax() >= other.getXMin())
				&& (this.getYMin() <= other.getYMax() && this.getYMax() >= other.getYMin())
				&& (this.getZMin() <= other.getZMax() && this.getZMax() >= other.getZMin());
	}

	/**
	 * Called when this {@code AABB} enters a collision with another {@code AABB}.
	 * 
	 * @param other the other {@code AABB} in the collision
	 */
	protected abstract void onCollisionEnter(AABB other);

	/**
	 * Called when this {@code AABB} exits a collision with another {@code AABB}.
	 * 
	 * @param other the other {@code AABB} in the collision
	 */
	protected abstract void onCollisionExit(AABB other);

}
