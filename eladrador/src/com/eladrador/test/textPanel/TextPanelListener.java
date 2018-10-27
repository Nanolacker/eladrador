package com.eladrador.test.textPanel;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.eladrador.common.Debug;
import com.eladrador.common.ui.TextPanel;

public class TextPanelListener implements Listener {

	@EventHandler
	private void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Location loc = player.getLocation();
		TextPanel tp = new TextPanel("This is a test", 5, loc);
		tp.setEnabled(true);
	}

}
