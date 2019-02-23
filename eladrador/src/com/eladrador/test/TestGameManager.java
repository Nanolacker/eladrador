package com.eladrador.test;

import org.bukkit.Location;
import org.bukkit.World;

import com.eladrador.common.AbstractGameManager;
import com.eladrador.common.GPlugin;
import com.eladrador.common.sound.SoundSequence;
import com.eladrador.common.sound.SoundSequencePlayer;
import com.eladrador.test.listeners.MenuTestListener;
import com.eladrador.test.songs.LostWoodsTestSong;
import com.google.gson.Gson;

public class TestGameManager extends AbstractGameManager {

	@Override
	public void onEnable() {
		registerWorld("world");

		World world = worldForName("world");
		Location loc = new Location(world, 46, 69, 236);
		SoundSequence sequence = sequenceFromJson();
		SoundSequencePlayer player = new SoundSequencePlayer(sequence);
		player.play(loc);

		GPlugin.registerEvents(new MenuTestListener());
	}

	@Override
	public void onDisable() {
	}

	private SoundSequence soundSequenceFromJson() {
		Gson gson = new Gson();
		String json = "";
		return gson.fromJson(json, SoundSequence.class);
	}

}
