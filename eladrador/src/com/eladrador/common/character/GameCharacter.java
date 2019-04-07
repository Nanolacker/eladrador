package com.eladrador.common.character;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.eladrador.common.Debug;
import com.eladrador.common.ui.TextPanel;
import com.eladrador.common.utils.MathUtils;

public class GameCharacter {

	private final String name;
	private int level;
	private Location location;
	private boolean isAlive;
	private double currentHealth;
	private double maxHealth;
	private final Resistances resistances;
	private final TextPanel nameplate;
	private Vector nameplateLocationOffset;

	protected GameCharacter(String name, int level, Location location, double maxHealth) {
		this.name = name;
		this.level = level;
		this.location = location;
		isAlive = false;
		currentHealth = maxHealth;
		this.maxHealth = maxHealth;
		resistances = new Resistances();
		nameplate = new TextPanel(location);
		nameplateLocationOffset = new Vector();
		updateNameplateText();
	}

	public final String getName() {
		return name;
	}

	public final int getLevel() {
		return level;
	}

	@OverridingMethodsMustInvokeSuper
	protected void setLevel(int level) {
		this.level = level;
	}

	public final Location getLocation() {
		return location;
	}

	@OverridingMethodsMustInvokeSuper
	public void setLocation(Location location) {
		this.location = location;
		updateNameplateLocation();
	}

	public final boolean isAlive() {
		return isAlive;
	}

	protected final void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public final double getCurrentHealth() {
		return currentHealth;
	}

	@OverridingMethodsMustInvokeSuper
	public void setCurrentHealth(double currentHealth) {
		currentHealth = MathUtils.clamp(currentHealth, 0.0, maxHealth);
		this.currentHealth = currentHealth;
		updateNameplateText();
	}

	public final double getMaxHealth() {
		return maxHealth;
	}

	@OverridingMethodsMustInvokeSuper
	public void setMaxHealth(double maxHealth) {
		this.maxHealth = maxHealth;
		updateNameplateText();
	}

	public final Resistances getResistances() {
		return resistances;
	}

	@OverridingMethodsMustInvokeSuper
	public void damage(double damage, DamageType damageType, Damager source) {
		double resistance = resistances.getResistance(damageType);
		double finalDamage = calculateFinalDamage(damage, resistance);
		double newCurrentHealth = currentHealth - finalDamage;
		if (newCurrentHealth <= 0.0) {
			die(source);
		} else {
			setCurrentHealth(newCurrentHealth);
		}
	}

	@OverridingMethodsMustInvokeSuper
	public void heal(double amount) {
		setCurrentHealth(currentHealth + amount);
	}

	private static final double calculateFinalDamage(double damage, double resistance) {
		double damageMultiplier = damage / (damage + resistance);
		return damage * damageMultiplier;
	}

	@OverridingMethodsMustInvokeSuper
	public void die(Damager killer) {
		setCurrentHealth(0.0);
		isAlive = false;
		nameplate.setVisible(false);
	}

	protected final TextPanel getNameplate() {
		return nameplate;
	}

	private final void updateNameplateText() {
		nameplate.setText(nameplateText());
	}

	private final String nameplateText() {
		int numBars = 16;
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

	private final void updateNameplateLocation() {
		nameplate.setLocation(location.clone().add(nameplateLocationOffset));
	}

	protected final void setNameplateLocationOffset(Vector offset) {
		nameplateLocationOffset = offset;
		updateNameplateLocation();
	}

}
