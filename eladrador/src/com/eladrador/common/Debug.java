package com.eladrador.common;

import org.bukkit.Bukkit;
import org.bukkit.Server;
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
	 * Prints a message to the console and to all players.
	 * 
	 * @param message the message to print
	 */
	public static void log(String message) {
		/*
		 * If a message is broadcasted immediately as the server is starting, the
		 * message will not print visibly. As such, a DelayedTask is used to make
		 * printing debug messages on startup possible.
		 */
		Runnable r = new Runnable() {

			@Override
			public void run() {
				GPlugin.getBukkitServer().broadcastMessage(ChatColor.WHITE + "" + ChatColor.ITALIC + "[" + ChatColor.BLUE + ""
						+ ChatColor.ITALIC + "DEBUG" + ChatColor.WHITE + ChatColor.ITALIC + ":" + ChatColor.RESET + " "
						+ message + ChatColor.WHITE + "" + ChatColor.ITALIC + "]");
			}

		};
		DelayedTask task = new DelayedTask(r, 0.0);
		task.start();
	}

	/**
	 * Returns the first Player on the server. Useful for quickly grabbing a player
	 * to debug and test with.
	 */
	public static Player getFirstPlayerOnline() {
		Player[] onlinePlayers = GPlugin.getBukkitServer().getOnlinePlayers().toArray(new Player[1]);
		if (onlinePlayers.length == 0) {
			throw new IllegalStateException("No players are currently online");
		}
		Player player = onlinePlayers[0];
		return player;
	}

}
