package com.eladrador.common.scheduling;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.eladrador.common.MMORPGPlugin;

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
		long periodInMilis = (long) (period * 20);
		Runnable runnable = () -> RepeatingTask.this.run();
		BukkitScheduler scheduler = Bukkit.getScheduler();
		Plugin plugin = MMORPGPlugin.getInstance();
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
			BukkitScheduler scheduler = Bukkit.getScheduler();
			scheduler.cancelTask(taskID);
			double serverTime = Clock.getTime();
			double executionTime = exeTime;
			while (!(executionTime > serverTime)) {
				executionTime += period;
			}
			long timeUntilExecutionInMilis = (long) ((executionTime - serverTime) * 20);
			long periodInMilis = (long) (period * 20);
			Runnable runnable = () -> RepeatingTask.this.run();
			Plugin plugin = MMORPGPlugin.getInstance();
			taskID = scheduler.scheduleSyncRepeatingTask(plugin, runnable, timeUntilExecutionInMilis, periodInMilis);
		}
	}

}
