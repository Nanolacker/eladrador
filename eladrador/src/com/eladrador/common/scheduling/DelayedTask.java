package com.eladrador.common.scheduling;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.eladrador.common.GPlugin;

/**
 * A task that runs after a given amount of time has passed.
 */
public class DelayedTask extends AbstractTask {

	private double delay;

	public DelayedTask(Runnable r, double delay) {
		super(r);
		this.delay = delay;
	}

	@Override
	protected void scheduleBukkitTask() {
		BukkitScheduler scheduler = GPlugin.getScheduler();
		Plugin plugin = GPlugin.getPlugin();
		long delayInMilis = (long) (getDelay() * 20);
		taskID = scheduler.scheduleSyncDelayedTask(plugin, r, delayInMilis);
	}

	public double getDelay() {
		return delay;
	}

}
