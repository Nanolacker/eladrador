package com.eladrador.common.physics;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.eladrador.common.Debug;
import com.eladrador.common.scheduling.RepeatingTask;

public final class Ray {

	private static final Particle DEFAULT_DRAW_PARTICLE = Particle.CRIT;
	/**
	 * The period by which rays will be drawn.
	 */
	private static final double DRAW_PERIOD = 0.1;
	/**
	 * How thick layers of particles used to draw rays will be. The greater this
	 * value, the less space there is between particles.
	 */
	private static final double DRAW_THICKNESS = 4.0;

	private final Location origin;
	private final Vector directionAndLength;
	private boolean drawingEnabled;
	/**
	 * The particle that will be used to draw this ray if {@code drawingEnabled} is
	 * true.
	 */
	private Particle drawParticle;
	/**
	 * The repeating task that is used to draw this ray.
	 */
	private RepeatingTask drawTask;

	public Ray(Location origin, Vector directionAndLength) {
		this.origin = origin;
		this.directionAndLength = directionAndLength;
		drawingEnabled = false;
		drawParticle = DEFAULT_DRAW_PARTICLE;
	}

	public Location getOrigin() {
		return origin;
	}

	public Vector getDirectionAndLength() {
		return directionAndLength;
	}

	public Location intersection(Collider collider) {
		BoundingBox bb = new BoundingBox(collider.getXMin(), collider.getYMin(), collider.getZMin(), collider.getXMax(),
				collider.getYMax(), collider.getZMax());
		RayTraceResult result = bb.rayTrace(origin.toVector(), directionAndLength.clone().normalize(),
				directionAndLength.length());
		Location hitLoc = result == null ? null : result.getHitPosition().toLocation(collider.getWorld());
		return hitLoc;
	}

	/**
	 * Enabling drawing will result in a visual representation of this ray to
	 * be rendered in game using particles. As creating so many particles is very
	 * costly, this should only be invoked for debugging purposes.
	 */
	public void setDrawingEnabled(boolean enabled) {
		boolean redundant = this.drawingEnabled == enabled;
		if (redundant) {
			return;
		}
		this.drawingEnabled = enabled;
		if (drawingEnabled) {
			if (drawTask == null) {
				assignDrawTask();
			}
			drawTask.start();
			String originDesc = String.format(ChatColor.YELLOW + "origin = (%.1f, %.1f, %.1f)", origin.getX(),
					origin.getY(), origin.getZ());
			Debug.log(ChatColor.WHITE + "Drawing of ray has been enabled. (" + originDesc + ChatColor.WHITE + ")");
		} else {
			drawTask.stop();
		}
	}

	private void assignDrawTask() {
		drawTask = new RepeatingTask(DRAW_PERIOD) {
			@Override
			protected void run() {
				draw();
			}
		};
	}

	private void draw() {
		double distanceBetweenParticles = 1 / DRAW_THICKNESS;
		Vector normalizedDirAndLength = directionAndLength.clone().normalize();
		World world = origin.getWorld();
		for (double i = 0.0; i <= directionAndLength.length(); i += distanceBetweenParticles) {
			double x = origin.getX() + i * normalizedDirAndLength.getX();
			double y = origin.getY() + i * normalizedDirAndLength.getY();
			double z = origin.getZ() + i * normalizedDirAndLength.getZ();
			Location point = new Location(world, x, y, z);
			world.spawnParticle(drawParticle, point, 0);
		}
	}

	public Particle getDrawParticle() {
		return drawParticle;
	}

	public void setDrawParticle(Particle particle) {
		drawParticle = particle;
	}

}
