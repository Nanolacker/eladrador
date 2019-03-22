package com.eladrador.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.World;

import com.eladrador.common.AbstractGameManager;
import com.eladrador.common.MMORPGPlugin;
import com.eladrador.common.scheduling.DelayedTask;
import com.eladrador.common.sound.SoundSequence;
import com.eladrador.common.sound.SoundSequencePlayer;
import com.eladrador.test.listeners.MenuTestListener;
import com.eladrador.test.songs.LostWoodsTestSong;
import com.google.gson.Gson;

public class TestGameManager extends AbstractGameManager {

	@Override
	public void onEnable() {
		registerWorld("world");

		// writeSoundSequence();

		World world = worldForName("world");
		Location loc = new Location(world, 46, 69, 236);
		SoundSequence sequence = soundSequenceFromJson();
		SoundSequencePlayer player = new SoundSequencePlayer(sequence);
		player.setLooping(true);
		new DelayedTask(2.0) {
			public void run() {
				player.play(loc);
			}
		}.start();
		MMORPGPlugin.registerEvents(new MenuTestListener());
	}

	@Override
	public void onDisable() {
	}

	private SoundSequence soundSequenceFromJson() {
		Gson gson = new Gson();
		BufferedReader json;
		try {
			json = new BufferedReader(new FileReader(
					"C:\\Users\\conno\\git\\eladrador\\eladrador\\src\\com\\eladrador\\test\\TestSong.json"));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		return gson.fromJson(json, SoundSequence.class);
	}

	private void writeSoundSequence() {
		try {
			Gson gson = new Gson();
			SoundSequence sequence = new LostWoodsTestSong();
			File file = new File(
					"C:\\Users\\conno\\git\\eladrador\\eladrador\\src\\com\\eladrador\\test\\TestSong.json");
			// file.createNewFile();
			BufferedWriter bf = new BufferedWriter(new FileWriter(file));
			String json = gson.toJson(sequence);
			bf.write(json);
			bf.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
