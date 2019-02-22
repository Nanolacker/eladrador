package com.eladrador.common;

import org.bukkit.entity.Player;

import com.eladrador.common.scheduling.DelayedTask;

import net.md_5.bungee.api.ChatColor;

/**
 * Static-only class used for debugging and testing.
 */
public final class Debug {

	private Debug() {
		// not to be instantiated
	}

	/**
	 * Broadcasts a message to the console and all online players for debugging. If
	 * a message needs to broadcasted as the server is starting up, invoke
	 * {@link Debug#logStartupSafe(String)} instead.
	 * 
	 * @param message the message to broadcast
	 */
	public static void log(String message) {
		logImpl(message, false);
	}

	public static void log(Object message) {
		logImpl(String.valueOf(message), false);
	}

	public static void log(boolean message) {
		logImpl(String.valueOf(message), false);
	}

	public static void log(int message) {
		logImpl(String.valueOf(message), false);
	}

	public static void log(double message) {
		logImpl(String.valueOf(message), false);
	}

	/**
	 * Broadcasts a message to the console and all online players for debugging.
	 * Invoke this if a message needs to broadcasted as the server is starting up.
	 * 
	 * @param message the message to broadcast
	 */
	public static void logStartupSafe(String message) {
		logImpl(message, true);
	}

	public static void logStartupSafe(Object message) {
		logImpl(String.valueOf(message), true);
	}

	public static void logStartupSafe(boolean message) {
		logImpl(String.valueOf(message), true);
	}

	public static void logStartupSafe(int message) {
		logImpl(String.valueOf(message), true);
	}

	public static void logStartupSafe(double message) {
		logImpl(String.valueOf(message), true);
	}

	private static void logImpl(String message, boolean delayed) {
		Thread thread = Thread.currentThread();
		StackTraceElement stackTractElement = thread.getStackTrace()[3];
		String debugMessage = debugMessage(message, stackTractElement);
		if (delayed) {
			new DelayedTask(0.0) {

				@Override
				protected void run() {
					GPlugin.getBukkitServer().broadcastMessage(debugMessage);
				}

			}.start();
		} else {
			GPlugin.getBukkitServer().broadcastMessage(debugMessage);
		}
	}

	private static String debugMessage(String message, StackTraceElement caller) {
		String extendedCallerDesc = caller.toString();
		int start = extendedCallerDesc.indexOf("(") + 1;
		int end = extendedCallerDesc.length() - 1;
		String simpleCallerDesc = extendedCallerDesc.substring(start, end);
		return ChatColor.BLUE + "[DEBUG] " + ChatColor.RESET + message + ChatColor.RESET + "\n" + simpleCallerDesc;
	}

	/**
	 * Returns the first {@code Player} on the server. Returns {@code null} if no
	 * players are online. Useful for quickly grabbing a player to debug and test
	 * with.
	 */
	public static Player getFirstPlayerOnline() {
		Player[] onlinePlayers = GPlugin.getBukkitServer().getOnlinePlayers().toArray(new Player[1]);
		Player player = onlinePlayers[0];
		return player;
	}

}
