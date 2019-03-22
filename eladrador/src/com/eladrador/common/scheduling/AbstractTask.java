package com.eladrador.common.scheduling;

import org.bukkit.scheduler.BukkitRunnable;

import com.eladrador.common.MMORPGPlugin;

/**
 * Represents a task to be run.
 */
public abstract class AbstractTask {

	protected int taskID;
	boolean active;
	protected double exeTime;

	protected AbstractTask() {
		BukkitRunnable b = null;
		active = false;
		exeTime = -1;
	}

	public void start() {
		if (active) {
			stop();
		}
		active = true;
		double delay = this instanceof DelayedTask ? ((DelayedTask) this).getDelay() : 0;
		exeTime = Clock.getTime() + delay;
		scheduleBukkitTask();
	}

	protected abstract void scheduleBukkitTask();

	public void stop() {
		if (active) {
			active = false;
			exeTime = -1;
			MMORPGPlugin.getScheduler().cancelTask(taskID);
		} else {
			throw new IllegalStateException("Cannot cancel a task that is not active");
		}
	}

	/**
	 * Returns whether the task will still execute.
	 */
	public boolean getActive() {
		return active;
	}

	/**
	 * Returns the time in seconds since the starting of the server that this task
	 * executed or will execute, which is after its delay timer expires. Returns -1
	 * if start() has not been called yet.
	 */
	public double getExeTime() {
		if (active) {
			return exeTime;
		} else {
			throw new IllegalStateException("Cannot get execution time for a non-active task");
		}
	}

	/**
	 * Returns the time in seconds that must pass before the task is executed.
	 */
	public double getSecondsUntilExecution() {
		if (active) {
			return exeTime - Clock.getTime();
		} else {
			throw new IllegalStateException("Cannot get seconds until execution for a non-active task");
		}
	}

	/**
	 * Invoked when this task runs.
	 */
	protected abstract void run();

}
