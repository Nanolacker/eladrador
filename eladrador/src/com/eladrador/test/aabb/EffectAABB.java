package com.eladrador.test.aabb;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.eladrador.common.GPlugin;
import com.eladrador.common.collision.AABB;
import com.eladrador.common.scheduling.RepeatingTask;
import com.eladrador.test.songs.LostWoodsTestSong;

public class EffectAABB extends AABB {

	private Particle particle;
	private Vector dir;

	public EffectAABB(Particle particle, Vector dir, Location center, double lengthX, double lengthY, double lengthZ) {
		super(center, lengthX, lengthY, lengthZ);
		this.particle = particle;
		this.dir = dir;
		Runnable r = new Runnable() {

			@Override
			public void run() {
				effectStuff();
			}

		};
		RepeatingTask task = new RepeatingTask(r, 0.01);
		task.start();
	}

	@Override
	protected void onCollisionEnter(AABB other) {
		particle = Particle.EXPLOSION_HUGE;
		dir = new Vector(0, 0, 0);
		setDimensions(new Vector(10, 10, 10));
		Player p = GPlugin.getBukkitServer().getPlayer("nanolacker");
		new LostWoodsTestSong().play(p, false);
	}

	private void effectStuff() {
		World world = getCenter().getWorld();
		for (double xCount = getXMin(); xCount <= getXMax(); xCount++) {
			for (double yCount = getYMin(); yCount <= getYMax(); yCount++) {
				for (double zCount = getZMin(); zCount <= getZMax(); zCount++) {
					Location loc = new Location(world, xCount, yCount, zCount);
					world.spawnParticle(particle, loc, 1);
				}
			}
		}
		translate(dir);
	}

	@Override
	protected void onCollisionExit(AABB other) {
		// TODO Auto-generated method stub
		
	}

}
