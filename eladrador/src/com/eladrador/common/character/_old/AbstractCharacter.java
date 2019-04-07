package com.eladrador.common.character._old;

import java.util.UUID;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import com.eladrador.common.character.Damager;
import com.eladrador.common.ui.TextPanel;

public abstract class AbstractCharacter implements Damager {

	/**
	 * Used to identify this {@code AbstractCharacter}.
	 */
	private UUID id;
	/**
	 * The name of this {@code AbstractCharacter}, including any {@code ChatColor}
	 * components.
	 */
	private String name;
	/**
	 * A marker of difficulty.
	 */
	private int level;
	/**
	 * The current health.
	 */
	private double currentHealth;
	/**
	 * The max health.
	 */
	private double maxHealth;
	/**
	 * The location of this {@code AbstractCharacter}.
	 */
	private Location location;
	/**
	 * Whether this {@code AbstractCharacter} is alive.
	 */
	private boolean isAlive;
	/**
	 * {@code TextPanel} used to display name, level, and health.
	 */
	private TextPanel nameplate;

	protected AbstractCharacter(String name, int level, double maxHealth, Location location) {
		id = UUID.randomUUID();
		this.name = name;
		this.level = level;
		currentHealth = maxHealth;
		this.maxHealth = maxHealth;
		this.location = location;
		isAlive = false;
		nameplate = new TextPanel(location);
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

	public double getCurrentHealth() {
		return currentHealth;
	}

	protected void setCurrentHealth(double currentHealth) {
		if (currentHealth < 0.0) {
			currentHealth = 0.0;
		}
		this.currentHealth = currentHealth;
		updateNameplateText();
	}

	public void setMaxHealth(double maxHealth) {
		this.maxHealth = maxHealth;
		updateNameplateText();
	}

	public double getMaxHealth() {
		return maxHealth;
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
		double newHealth = currentHealth - damage;
		setCurrentHealth(newHealth);
		if (newHealth <= 0.0) {
			die(source);
		}
	}

	@OverridingMethodsMustInvokeSuper
	public void spawn() {
		if (isAlive) {
			throw new IllegalStateException("Cannot spawn a character that is already spawned.");
		}
		currentHealth = maxHealth;
		updateNameplateLocation();
		updateNameplateText();
		nameplate.setVisible(true);
		isAlive = true;
	}

	/**
	 * Despawns this {@code AbstractCharacter}, thus not allowing it to interact
	 * with the world.
	 */
	@OverridingMethodsMustInvokeSuper
	public void despawn() {
		isAlive = false;
	}

	/**
	 * Kills this {@code AbstractCharacter}.
	 */
	@OverridingMethodsMustInvokeSuper
	public void die(Damager killer) {
		isAlive = false;
		currentHealth = 0.0;
		nameplate.setVisible(false);
	}

}
