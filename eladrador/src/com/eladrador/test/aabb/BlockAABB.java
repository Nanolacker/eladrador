package com.eladrador.test.aabb;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

import com.eladrador.common.collision.AABB;
import com.eladrador.common.scheduling.RepeatingTask;

public class BlockAABB extends AABB {

	private Material blockMat;
	private Vector dir;
	private ArrayList<Location> placedBlockLocs;

	public BlockAABB(Material blockMat, Vector dir, Location center, double lengthX, double lengthY, double lengthZ) {
		super(center, lengthX, lengthY, lengthZ);
		this.blockMat = blockMat;
		this.dir = dir;
		placedBlockLocs = new ArrayList<Location>();
		Runnable r = new Runnable() {

			@Override
			public void run() {
				blockStuff();
			}

		};
		RepeatingTask task = new RepeatingTask(r, 0.1);
		task.start();
	}

	@Override
	protected void onCollisionEnter(AABB other) {
		blockMat = Material.DIAMOND_BLOCK;
	}

	private void blockStuff() {
		clear();
		World world = getCenter().getWorld();
		for (double xCount = getXMin(); xCount <= getXMax(); xCount++) {
			for (double yCount = getYMin(); yCount <= getYMax(); yCount++) {
				for (double zCount = getZMin(); zCount <= getZMax(); zCount++) {
					Location loc = new Location(world, Math.floor(xCount), Math.floor(yCount), Math.floor(zCount));
					loc.getBlock().setType(blockMat);
					placedBlockLocs.add(loc);
				}
			}
		}
		translate(dir);
	}

	public void clear() {
		for (int i = 0; i < placedBlockLocs.size(); i++) {
			Location loc = placedBlockLocs.get(i);
			loc.getBlock().setType(Material.AIR);
		}
		placedBlockLocs.clear();
	}

	@Override
	protected void onCollisionExit(AABB other) {
		// TODO Auto-generated method stub
		
	}

}
