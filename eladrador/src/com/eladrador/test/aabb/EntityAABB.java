package com.eladrador.test.aabb;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.eladrador.common.GPlugin;
import com.eladrador.common.collision.AABB;
import com.eladrador.common.scheduling.RepeatingTask;
import com.eladrador.test.songs.LostWoodsTestSong;
import com.eladrador.test.songs.TestSong;

public class EntityAABB extends AABB {

	private EntityType entityType;
	private Vector dir;
	private ArrayList<Entity> spawnedEntities;
	boolean hit;

	public EntityAABB(EntityType entityType, Vector dir, Location center, double lengthX, double lengthY,
			double lengthZ) {
		super(center, lengthX, lengthY, lengthZ);
		this.entityType = entityType;
		this.dir = dir;
		spawnedEntities = new ArrayList<Entity>();
		hit = false;
		Runnable r = new Runnable() {

			@Override
			public void run() {
				blockStuff();
			}

		};
		RepeatingTask task = new RepeatingTask(r, 1);
		task.start();
	}

	private void blockStuff() {
		clear();
		World world = getCenter().getWorld();
		for (double xCount = getXMin(); xCount <= getXMax(); xCount++) {
			for (double yCount = getYMin(); yCount <= getYMax(); yCount++) {
				for (double zCount = getZMin(); zCount <= getZMax(); zCount++) {
					Location loc = new Location(world, xCount, yCount, zCount);
					if (hit == true) {
						world.spawnParticle(Particle.EXPLOSION_HUGE, loc, 1);
					} else {
						// spawnedEntities.add(world.spawnEntity(loc, entityType));
					}
				}
			}
		}
		translate(dir);
	}

	@Override
	protected void onCollisionEnter(AABB other) {
		setDrawParticle(Particle.HEART);
	}

	@Override
	protected void onCollisionExit(AABB other) {
		setDrawParticle(Particle.DOLPHIN);
	}

	public void clear() {
		for (int i = 0; i < spawnedEntities.size(); i++) {
			Entity entity = spawnedEntities.get(i);
			entity.remove();
		}
		spawnedEntities.clear();
	}

}
