package com.eladrador.common.sound;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Represents a sound that can be played ambiently at a location or to a
 * specific player.
 */
public class Noise {

	private Sound type;
	private float volume;
	private float pitch;
	private SoundCategory soundCategory;

	public Noise(Sound type) {
		this.type = type;
		volume = 1.0f;
		pitch = 1.0f;
		// MASTER is the default in Bukkit
		soundCategory = SoundCategory.MASTER;
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public SoundCategory getSoundCategory() {
		return soundCategory;
	}

	public void setSoundCategory(SoundCategory soundCategory) {
		this.soundCategory = soundCategory;
	}

	public void play(Location source) {
		World world = source.getWorld();
		world.playSound(source, type, soundCategory, volume, pitch);
	}

	/**
	 * Plays to a specific player only.
	 */
	public void play(Player player) {
		player.playSound(player.getLocation(), type, soundCategory, volume, pitch);
	}

	/**
	 * Plays to a specific player only.
	 */
	public void play(Player player, Location source) {
		player.playSound(source, type, soundCategory, volume, pitch);
	}

}
