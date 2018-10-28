package com.eladrador.test;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.eladrador.common.Debug;

public class LookListener implements Listener {

	@EventHandler
	private void onLook(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location newLoc = player.getLocation();
		Debug.log("yaw: " + newLoc.getYaw() + ", pitch: " + newLoc.getPitch());
	}

}
