package com.eladrador.common.scheduling;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.eladrador.common.GPlugin;

/**
 * A task that runs repeatedly.
 */
public class RepeatingTask extends AbstractTask {

	/**
	 * In seconds.
	 */
	private double period;

	/**
	 * @param period the period in seconds of the task
	 */
	public RepeatingTask(Runnable r, double period) {
		super(r);
		this.period = period;
	}

	@Override
	protected void scheduleBukkitTask() {
		BukkitScheduler scheduler = GPlugin.getScheduler();
		Plugin plugin = GPlugin.getPlugin();
		long periodInMilis = (long) (period * 20);
		taskID = scheduler.scheduleSyncRepeatingTask(plugin, r, 0, periodInMilis);
	}

	public double getPeriod() {
		return period;
	}

	public void setPeriod(double period) {
		this.period = period;
		if (active) {
			BukkitScheduler scheduler = GPlugin.getScheduler();
			scheduler.cancelTask(taskID);
			double serverTime = GClock.getTime();
			double executionTime = exeTime;
			while (!(executionTime > serverTime)) {
				executionTime += period;
			}
			long timeUntilExecutionInMilis = (long) ((executionTime - serverTime) * 20);
			long periodInMilis = (long) (period * 20);
			taskID = scheduler.scheduleSyncRepeatingTask(GPlugin.getPlugin(), r, timeUntilExecutionInMilis,
					periodInMilis);
		}
	}

}
