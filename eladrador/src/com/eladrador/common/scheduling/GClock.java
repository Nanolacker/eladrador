package com.eladrador.common.scheduling;

/**
 * Stores the time that has passed since the starting of the server.
 */
public class GClock {

	public static final double UPDATE_PERIOD = 0.05;

	private static double time = 0.0;

	public static void start() {
		RepeatingTask clock = new RepeatingTask(UPDATE_PERIOD) {

			@Override
			protected void run() {
				time += UPDATE_PERIOD;
			}

		};
		clock.start();
	}

	/**
	 * Returns the time in seconds that has passed since the starting of the server.
	 */
	public static double getTime() {
		return time;
	}

}
