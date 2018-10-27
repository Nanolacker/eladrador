package com.eladrador.common;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import com.eladrador.common.scheduling.GClock;
import com.eladrador.common.ui.TextPanel;
import com.eladrador.test.TestGameManager;

import net.md_5.bungee.api.ChatColor;

public final class GPlugin extends JavaPlugin {

	private static final Class<? extends AbstractGameManager> GAME_MANAGER_CLASS = TestGameManager.class;

	private static Plugin plugin;
	private static Server server;
	private static PluginManager pluginManager;
	private static BukkitScheduler scheduler;
	private static AbstractGameManager gameManager;

	/**
	 * All code from the plugin is run from here. Called when this Plugin is
	 * enabled.
	 */
	@Override
	public void onEnable() {
		plugin = this;
		server = getServer();
		pluginManager = server.getPluginManager();
		scheduler = server.getScheduler();
		try {
			gameManager = GAME_MANAGER_CLASS.getConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		gameManager.onEnable();
		GClock.start();
		enableReloadOnExport();
	}

	/**
	 * Called when this plugin is disabled.
	 */
	@Override
	public void onDisable() {
		TextPanel.removeAllTextPanelEntities();
		/*
		 * gameManager.onDisable(); OfflinePlayer[] players =
		 * server.getOfflinePlayers(); for (int i = 0; i < players.length; i++) {
		 * OfflinePlayer player = players[i]; if (player instanceof Player) { // if they
		 * are online ((Player) player).kickPlayer("Server is restarting."); } }
		 */
	}

	/**
	 * Reloads the server when project is exported for ease of development.
	 */
	private void enableReloadOnExport() {
		final long lastModified = getFile().lastModified();
		new BukkitRunnable() {

			@Override
			public void run() {
				if (getFile().lastModified() > lastModified) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "reload");
					cancel();
				}
			}

		}.runTaskTimer(this, 0, 20);
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
