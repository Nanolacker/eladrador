package com.eladrador.common;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.eladrador.common.character.PlayerCharacterOLD;
import com.eladrador.common.scheduling.Clock;
import com.eladrador.common.scheduling.DelayedTask;
import com.eladrador.common.scheduling.RepeatingTask;
import com.eladrador.common.ui.ItemInteractionListener;
import com.eladrador.common.ui.TextPanel;
import com.eladrador.impl.GameManager;

public final class MMORPGPlugin extends JavaPlugin {

	private static final Class<? extends AbstractGameManager> GAME_MANAGER_CLASS = GameManager.class;
	private static final double CHECK_FOR_UPDATE_PERIOD_SECONDS = 1.0;
	private static final double RESTART_DELAY_SECONDS = 1.0;

	private static Plugin plugin;
	private static Server server;
	private static PluginManager pluginManager;
	private static BukkitScheduler scheduler;
	private static AbstractGameManager gameManager;

	@Override
	public void onEnable() {
		plugin = this;
		server = getServer();
		pluginManager = server.getPluginManager();
		scheduler = server.getScheduler();
		registerEvents(new ItemInteractionListener());
		try {
			gameManager = GAME_MANAGER_CLASS.getConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		gameManager.onEnable();
		Clock.start();
		enableReloadOnExport();
	}

	@Override
	public void onDisable() {
		TextPanel.removeAllTextPanelEntities();
		gameManager.onDisable();
		// kickAllOnlinePlayers();
	}

	/**
	 * Invoke when disabling.
	 */
	private void kickAllOnlinePlayers() {
		OfflinePlayer[] players = server.getOfflinePlayers();
		for (int i = 0; i < players.length; i++) {
			OfflinePlayer player = players[i];
			if (player instanceof Player) { // if they are online
				((Player) player).kickPlayer("Server is restarting.");
			}
		}
	}

	/**
	 * Reloads the server when project is exported for ease of development.
	 */
	private void enableReloadOnExport() {
		long lastModified = getFile().lastModified();
		RepeatingTask checkForUpdate = new RepeatingTask(CHECK_FOR_UPDATE_PERIOD_SECONDS) {
			@Override
			protected void run() {
				if (getFile().lastModified() > lastModified) {
					// delayed to prevent certain errors that result from reloading too quickly
					// after exporting
					DelayedTask reload = new DelayedTask(RESTART_DELAY_SECONDS) {
						@Override
						protected void run() {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "reload");
							stop();
						}
					};
					reload.start();
				}
			}
		};
		checkForUpdate.start();
	}

	public static Plugin getPlugin() {
		return plugin;
	}

	public static Server getBukkitServer() {
		return server;
	}

	public static PluginManager getPluginManager() {
		return pluginManager;
	}

	public static BukkitScheduler getScheduler() {
		return scheduler;
	}

	public static AbstractGameManager getGameManager() {
		return gameManager;
	}

	public static void registerEvents(Listener listener) {
		pluginManager.registerEvents(listener, plugin);
	}

}
