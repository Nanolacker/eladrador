package com.eladrador.common.scheduling;

import com.eladrador.common.GPlugin;

/**
 * Represents a task to be run.
 */
public abstract class AbstractTask {

	protected Runnable r;
	protected int taskID;
	boolean active;
	protected double exeTime;

	protected AbstractTask(Runnable r) {
		this.r = r;
		active = false;
		exeTime = -1;
	}

	public void start() {
		if (active) {
			stop();
		}
		active = true;
		exeTime = GClock.getTime() + (this instanceof DelayedTask ? ((DelayedTask) this).getDelay() : 0);
		scheduleBukkitTask();
	}

	protected abstract void scheduleBukkitTask();

	public void stop() {
		if (active) {
			active = false;
			exeTime = -1;
			GPlugin.getScheduler().cancelTask(taskID);
		} else {
			try {
				throw new Exception("Cannot cancel a task that is not active");
			} catch (Exception e) {
				e.printStackTrace();
			}
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
			try {
				throw new Exception("Cannot get execution time for a non-active task");
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
		}
	}

	/**
	 * Returns the time in seconds that must pass before the task is executed.
	 */
	public double getSecondsUntilExecution() {
		if (active) {
			return exeTime - GClock.getTime();
		} else {
			try {
				throw new Exception("Cannot get seconds until execution for a non-active task");
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
		}
	}

}
