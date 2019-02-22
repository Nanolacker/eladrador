package com.eladrador.common.scheduling;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.eladrador.common.GPlugin;

/**
 * A task that runs after a given amount of time has passed.
 */
public abstract class DelayedTask extends AbstractTask {

	private double delay;

	/**
	 * The delay will be initialized as {@code 0.0).
	 */
	public DelayedTask() {
		this(0.0);
	}

	public DelayedTask(double delay) {
		this.delay = delay;
	}

	@Override
	protected void scheduleBukkitTask() {
		BukkitScheduler scheduler = GPlugin.getScheduler();
		Plugin plugin = GPlugin.getPlugin();
		long delayInMilis = (long) (getDelay() * 20);
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				DelayedTask.this.run();
				active = false;
			}

		};
		taskID = scheduler.scheduleSyncDelayedTask(plugin, runnable, delayInMilis);
	}

	public double getDelay() {
		return delay;
	}

}
