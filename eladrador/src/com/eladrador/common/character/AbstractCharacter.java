package com.eladrador.common.character;

import java.util.UUID;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import com.eladrador.common.GPlugin;
import com.eladrador.common.ui.TextPanel;

public abstract class AbstractCharacter implements Damager {

	/**
	 * Used to identify this {@code AbstractCharacter}.
	 */
	protected UUID id;
	/**
	 * The name of this {@code AbstractCharacter}, including any {@code ChatColor}
	 * components.
	 */
	protected String name;
	/**
	 * A marker of difficulty.
	 */
	protected int level;
	/**
	 * The max health.
	 */
	protected double maxHealth;
	/**
	 * The current health.
	 */
	protected double currentHealth;
	/**
	 * The location of this {@code AbstractCharacter}.
	 */
	protected Location location;
	/**
	 * Whether this {@code AbstractCharacter} is spawned.
	 */
	protected boolean spawned;
	/**
	 * {@code TextPanel} used to display name, level, and health.
	 */
	protected TextPanel nameplate;

	protected AbstractCharacter(String name, int level, double maxHealth, Location location) {
		id = UUID.randomUUID();
		this.name = name;
		this.level = level;
		this.maxHealth = maxHealth;
		currentHealth = maxHealth;
		this.location = location;
		spawned = false;
		nameplate = new TextPanel(22, location);
		GPlugin.getGameManager().registerChara(this);
	}

	/**
	 * Returns the ID of this {@code AbstractCharacter}.
	 */
	public final UUID getId() {
		return id;
	}

	/**
	 * Returns the name of this {@code AbstractCharacter}, including its
	 * {@code ChatColor} component if there is one.
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Returns the level of this {@code AbstractCharacter}, a marker of its
	 * strength.
	 */
	public final int getLevel() {
		return level;
	}

	public double getMaxHealth() {
		return maxHealth;
	}

	public double getCurrentHealth() {
		return currentHealth;
	}

	/**
	 * Returns the location of this character.
	 */
	public final Location getLocation() {
		return location;
	}

	/**
	 * Sets the location of this character.
	 * 
	 * @param location the location of this character
	 */
	@OverridingMethodsMustInvokeSuper
	public void setLocation(Location location) {
		this.location = location;
		updateNameplateLocation();
	}

	/**
	 * Override to return the location that this character's nameplate should exist
	 * at.
	 */
	protected abstract Location getNameplateLocation();

	protected final void updateNameplateText() {
		nameplate.setText(nameplateText());
	}

	private final void updateNameplateLocation() {
		nameplate.setLocation(getNameplateLocation());
	}

	private final String nameplateText() {
		int numBars = 20;
		StringBuilder text = new StringBuilder();
		text.append(ChatColor.WHITE + "[" + ChatColor.GOLD + "Lv. " + level + ChatColor.WHITE + "] " + ChatColor.RESET
				+ name + '\n');
		text.append(ChatColor.WHITE + "[");
		double currentToMaxHealthRatio = currentHealth / maxHealth;
		int numRedBars = (int) (numBars * currentToMaxHealthRatio);
		text.append(ChatColor.RED.toString());
		for (int i = 0; i < numRedBars; i++) {
			text.append('|');
		}
		text.append(ChatColor.GRAY.toString());
		for (int i = numRedBars; i < numBars; i++) {
			text.append('|');
		}
		text.append(ChatColor.WHITE + "]");
		return text.toString();
	}

	/**
	 * Reduces this {@code AbstractCharacter}'s health by an amount. If health
	 * reaches 0, they die.
	 * 
	 * @param damage the amount
	 * @param source the source of the damage
	 */
	@OverridingMethodsMustInvokeSuper
	public void damage(double damage, Damager source) {
		currentHealth -= damage;
		if (currentHealth <= 0.0) {
			die(source);
		} else {
			updateNameplateText();
		}
	}

	@OverridingMethodsMustInvokeSuper
	public void spawn() {
		if (spawned) {
			throw new IllegalStateException("Cannot spawn a character that is already spawned.");
		}
		currentHealth = maxHealth;
		updateNameplateLocation();
		updateNameplateText();
		nameplate.setVisible(true);
		spawned = true;
	}

	/**
	 * Despawns this {@code AbstractCharacter}, thus not allowing it to interact
	 * with the world.
	 */
	@OverridingMethodsMustInvokeSuper
	public void despawn() {
		spawned = false;
	}

	/**
	 * Kills this {@code AbstractCharacter}.
	 */
	@OverridingMethodsMustInvokeSuper
	public void die(Damager killer) {
		spawned = false;
		currentHealth = 0.0;
		nameplate.setVisible(false);
	}

}
