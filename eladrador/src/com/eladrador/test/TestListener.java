package com.eladrador.test;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.eladrador.common.sound.SoundSequence;
import com.eladrador.test.songs.LostWoodsTestSong;

public class TestListener implements Listener {

	@EventHandler
	private void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		SoundSequence song = new LostWoodsTestSong();
		song.play(p, true);
	}

}
