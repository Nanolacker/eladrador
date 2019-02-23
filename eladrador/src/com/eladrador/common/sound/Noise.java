package com.eladrador.common.sound;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Represents a sound that can be played ambiently at a location or to a
 * specific player.
 */
public final class Noise {

	private final Sound type;
	private final float volume;
	private final float pitch;

	/**
	 * The volume and pitch of this noise will be set to 1.
	 */
	public Noise(Sound type) {
		this(type, 1.0f, 1.0f);
	}

	public Noise(Sound type, float volume, float pitch) {
		this.type = type;
		this.volume = volume;
		this.pitch = pitch;
	}

	public float getVolume() {
		return volume;
	}

	public float getPitch() {
		return pitch;
	}

	public void play(Location source) {
		World world = source.getWorld();
		world.playSound(source, type, volume, pitch);
	}

	/**
	 * Plays to a specific player only.
	 */
	public void play(Player player) {
		player.playSound(player.getLocation(), type, volume, pitch);
	}

	/**
	 * Plays to a specific player only.
	 */
	public void play(Player player, Location source) {
		player.playSound(source, type, volume, pitch);
	}

}
