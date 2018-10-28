package com.eladrador.common.scheduling;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.eladrador.common.GPlugin;

/**
 * A task that runs repeatedly at a mutable period.
 */
public abstract class RepeatingTask extends AbstractTask {

	/**
	 * The period of this {@code RepeatingTask}, in seconds.
	 */
	private double period;

	/**
	 * @param period the period of this {@code RepeatingTask}, in seconds
	 */
	public RepeatingTask(double period) {
		this.period = period;
	}

	@Override
	protected void scheduleBukkitTask() {
		BukkitScheduler scheduler = GPlugin.getScheduler();
		Plugin plugin = GPlugin.getPlugin();
		long periodInMilis = (long) (period * 20);
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				RepeatingTask.this.run();
			}

		};
		taskID = scheduler.scheduleSyncRepeatingTask(plugin, runnable, 0, periodInMilis);
	}

	/**
	 * Returns the period of this {@code RepeatingTask}.
	 */
	public double getPeriod() {
		return period;
	}

	/**
	 * Sets the period of this {@code RepeatingTask}, making it run at a different
	 * rate than previously.
	 * 
	 * @param period the new period of this {@code RepeatingTask}
	 */
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
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					RepeatingTask.this.run();
				}

			};
			taskID = scheduler.scheduleSyncRepeatingTask(GPlugin.getPlugin(), runnable, timeUntilExecutionInMilis,
					periodInMilis);
		}
	}

}
