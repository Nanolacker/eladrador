package com.eladrador.common;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.eladrador.common.character.NonPlayerCharacter;
import com.eladrador.common.scheduling.Clock;
import com.eladrador.common.scheduling.DelayedTask;
import com.eladrador.common.scheduling.RepeatingTask;
import com.eladrador.common.ui.ItemInteractionListener;
import com.eladrador.common.ui.TextPanel;

public abstract class MMORPGPlugin extends JavaPlugin {

	private static final double CHECK_FOR_UPDATE_PERIOD_SECONDS = 1.0;
	private static final double RESTART_DELAY_SECONDS = 1.0;

	private static MMORPGPlugin instance;

	@Override
	public void onEnable() {
		instance = this;
		registerEvents(new ItemInteractionListener());
		Clock.start();
		onMMORPGStart();
		enableReloadOnProjectExport();
	}

	@Override
	public void onDisable() {
		onMMORPGStop();
		NonPlayerCharacter.despawnAll();
		TextPanel.removeAllTextPanelEntities();
		// kickAllOnlinePlayers();
	}

	/**
	 * Handle implementation-specific startup code here.
	 */
	protected abstract void onMMORPGStart();

	/**
	 * Handle implementation-specific shutdown code here.
	 */
	protected abstract void onMMORPGStop();

	/**
	 * Invoke when disabling.
	 */
	private void kickAllOnlinePlayers() {
		Server server = getServer();
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
	private void enableReloadOnProjectExport() {
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

	public static MMORPGPlugin getInstance() {
		return instance;
	}

	/**
	 * Convenience method for registering events.
	 */
	public static void registerEvents(Listener listener) {
		Bukkit.getPluginManager().registerEvents(listener, instance);
	}

}
