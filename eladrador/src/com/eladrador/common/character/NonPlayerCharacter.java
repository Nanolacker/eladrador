package com.eladrador.common.character;

import java.util.ArrayList;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import org.bukkit.Location;
import com.eladrador.common.player.Players;
import com.eladrador.common.scheduling.RepeatingTask;
import com.eladrador.common.ui.TextPanel;

/**
 * Expands upon {@link GameCharacter} by adding functionality for spawning and
 * despawning.
 */
public class NonPlayerCharacter extends GameCharacter {

	private static final ArrayList<NonPlayerCharacter> npcs = new ArrayList<>();
	private static final double DEFAULT_SPAWN_RADIUS = 50.0;
	private static final double SPAWN_TASK_PERIOD = 0.5;

	private boolean spawned;
	private boolean tryingToSpawn;
	private double spawnRadius;

	static {
		startSpawnTask();
	}

	private static final void startSpawnTask() {
		RepeatingTask spawnTask = new RepeatingTask(SPAWN_TASK_PERIOD) {
			@Override
			public void run() {
				for (int i = 0; i < npcs.size(); i++) {
					NonPlayerCharacter npc = npcs.get(i);
					if (npc.tryingToSpawn) {
						Location location = npc.getLocation();
						boolean playerNearby = Players.playerNearby(location, npc.spawnRadius, npc.spawnRadius,
								npc.spawnRadius);
						if (npc.spawned) {
							if (!playerNearby) {
								npc.despawn();
							}
						} else {
							if (playerNearby) {
								npc.spawn();
							}
						}
					}
				}
			}
		};
		spawnTask.start();
	}

	public static final void despawnAll() {
		for (int i = 0; i < npcs.size(); i++) {
			NonPlayerCharacter npc = npcs.get(i);
			if (npc.spawned) {
				npc.despawn();
			}
		}
	}

	protected NonPlayerCharacter(String name, int level, Location location, double maxHealth) {
		super(name, level, location, maxHealth);
		spawned = false;
		tryingToSpawn = false;
		spawnRadius = DEFAULT_SPAWN_RADIUS;
		npcs.add(this);
	}

	@Override
	public void die(Damager killer) {
		super.die(killer);
		tryingToSpawn = false;
		despawn();
	}

	public boolean isSpawned() {
		return spawned;
	}

	/**
	 * Sets whether this npc should spawn when a player is nearby.
	 * 
	 * @param spawning
	 */
	public void setSpawning(boolean spawning) {
		tryingToSpawn = spawning;
	}

	/**
	 * Invoked when this npc spawns.
	 */
	@OverridingMethodsMustInvokeSuper
	protected void spawn() {
		setAlive(true);
		spawned = true;
		TextPanel nameplate = getNameplate();
		nameplate.setVisible(true);
	}

	/**
	 * Invoked when this npc despawns.
	 */
	@OverridingMethodsMustInvokeSuper
	protected void despawn() {
		spawned = false;
	}

	protected final void setSpawnRadius(double radius) {
		spawnRadius = radius;
	}

}
