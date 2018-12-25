package com.eladrador.common;

import org.bukkit.entity.Player;

import com.eladrador.common.scheduling.DelayedTask;

import net.md_5.bungee.api.ChatColor;

/**
 * Static-only class used for debugging and testing.
 */
public final class Debug {

	/**
	 * Hides constructor.
	 */
	private Debug() {
		// not to be instantiated
	}

	/**
	 * Broadcasts a message to the console and all online players for debugging. If
	 * a message needs to broadcasted as the server is starting up, invoke
	 * {@link Debug#logDelayed(String)} instead.
	 * 
	 * @param message the message to broadcast
	 */
	public static void log(String message) {
		GPlugin.getBukkitServer()
				.broadcastMessage(ChatColor.WHITE + "" + ChatColor.ITALIC + "[" + ChatColor.BLUE + "" + ChatColor.ITALIC
						+ "DEBUG" + ChatColor.WHITE + ChatColor.ITALIC + ":" + ChatColor.RESET + " " + message
						+ ChatColor.WHITE + "" + ChatColor.ITALIC + "]");
	}

	public static void log(boolean b) {
		log(b + "");
	}

	public static void log(double d) {
		log(d + "");
	}

	/**
	 * Broadcasts a message to the console and all online players for debugging at a
	 * very short delay. Invoke this if a message needs to broadcasted as the server
	 * is starting up.
	 * 
	 * @param message the message to broadcast
	 */
	public static void logDelayed(String message) {
		/*
		 * If a message is broadcasted immediately as the server is starting, a bug
		 * prevents the message from printing. As such, a DelayedTask is used to make
		 * printing debug messages on startup possible.
		 */
		DelayedTask task = new DelayedTask(0.0) {

			@Override
			protected void run() {
				log(message);
			}

		};
		task.start();
	}

	public static void logDelayed(boolean b) {
		log(b + "");
	}

	public static void logDelayed(double d) {
		log(d + "");
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
