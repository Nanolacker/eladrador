package com.eladrador.test.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.eladrador.common.player.PlayerCharacter;
import com.eladrador.common.scheduling.DelayedTask;

public class PlayerCharacterManagerTest implements Listener {

	@EventHandler
	private void onJoin(PlayerLoginEvent e) {
		new DelayedTask(1) {

			public void run() {
				Player p = e.getPlayer();
				int slot = (int) (Math.random() * 4) + 1;
//				PlayerCharacter pc = PlayerCharacter.createNew(p, slot, null, null);
//				pc.setBukkitPlayer(p);
			}

		}.start();
	}

	@EventHandler
	private void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		PlayerCharacter pc = PlayerCharacter.forBukkitPlayer(p);
		pc.saveData();
	}

}
