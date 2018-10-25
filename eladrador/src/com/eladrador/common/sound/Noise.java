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
	private Location source;
	private float volume;
	private float pitch;
	private SoundCategory category;

	/**
	 * Only usable for playing to individual player.
	 * 
	 */
	public Noise(Sound type) {
		this.type = type;
		source = null;
		volume = 1;
		pitch = 1;
		category = null;
	}

	public Noise(Sound type, Location source) {
		this.type = type;
		this.source = source;
		volume = 1;
		pitch = 1;
		category = null;
	}

	/**
	 * Only usable for playing to individual player.
	 * 
	 */
	public Noise(Sound type, float volume, float pitch) {
		this.type = type;
		source = null;
		this.volume = volume;
		this.pitch = pitch;
		category = null;
	}

	public Noise(Sound type, Location source, float volume, float pitch) {
		this.type = type;
		this.source = source;
		this.volume = volume;
		this.pitch = pitch;
		category = null;
	}

	/**
	 * Only usable for playing to individual player.
	 * 
	 */
	public Noise(Sound type, SoundCategory category) {
		this.type = type;
		source = null;
		volume = 1;
		pitch = 1;
		this.category = category;
	}

	public Noise(Sound type, Location source, SoundCategory category) {
		this.type = type;
		this.source = source;
		volume = 1;
		pitch = 1;
		this.category = category;
	}

	/**
	 * Only usable for playing to individual player.
	 * 
	 */
	public Noise(Sound type, float volume, float pitch, SoundCategory category) {
		this.type = type;
		source = null;
		this.volume = volume;
		this.pitch = pitch;
		this.category = category;
	}

	public Noise(Sound type, Location source, float volume, float pitch, SoundCategory category) {
		this.type = type;
		this.source = source;
		this.volume = volume;
		this.pitch = pitch;
		this.category = category;
	}

	/**
	 * Only usable if source is set.
	 */
	public void play() {
		if (source == null) {
			try {
				throw new Exception(
						"Can only use play() when the source is set. Use play(Player) instead or use a different constructor to set source");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		World world = source.getWorld();
		if (category == null) {
			world.playSound(source, type, volume, pitch);
		} else {
			world.playSound(source, type, category, volume, pitch);
		}
	}

	/**
	 * Plays to a specific player only.
	 * 
	 * @param p
	 *            the player
	 */
	public void play(Player p) {
		if (category == null) {
			p.playSound(source == null ? p.getLocation() : source, type, volume, pitch);
		} else {
			p.playSound(source == null ? p.getLocation() : source, type, category, volume, pitch);
		}
	}

}
