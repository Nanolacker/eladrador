package com.eladrador.common.character;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import org.bukkit.Location;

import com.eladrador.common.ui.TextPanel;
import com.eladrador.common.utils.MathUtils;
import com.eladrador.common.utils.StrUtils;

import character.IGameCharacter;
import io.netty.util.internal.MathUtil;

public class GameCharacterImpl implements IGameCharacter {

	private final String name;
	private int level;
	private Location location;
	private boolean isAlive;
	private double currentHealth;
	private double maxHealth;
	private final Resistances resistances;
	private final TextPanel nameplate;

	protected GameCharacterImpl(String name, int level, Location location, double maxHealth) {
		this.name = name;
		this.level = level;
		this.location = location;
		isAlive = false;
		currentHealth = maxHealth;
		this.maxHealth = maxHealth;
		resistances = new Resistances();
		nameplate = new TextPanel(location);
	}

	@Override
	public final String getName() {
		return name;
	}

	@Override
	public final int getLevel() {
		return level;
	}

	@OverridingMethodsMustInvokeSuper
	protected void setLevel(int level) {
		this.level = level;
	}

	@Override
	public final Location getLocation() {
		return location;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public final boolean isAlive() {
		return isAlive;
	}

	@Override
	public final double getCurrentHealth() {
		return currentHealth;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void setCurrentHealth(double currentHealth) {
		currentHealth = MathUtils.clamp(currentHealth, 0.0, maxHealth);
		this.currentHealth = currentHealth;
	}

	@Override
	public final double getMaxHealth() {
		return maxHealth;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void setMaxHealth(double maxHealth) {
		this.maxHealth = maxHealth;
	}

	@Override
	public final Resistances getResistances() {
		return resistances;
	}

	@Override
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

	@Override
	@OverridingMethodsMustInvokeSuper
	public void heal(double amount) {
		setCurrentHealth(currentHealth + amount);
	}

	private static final double calculateFinalDamage(double damage, double resistance) {
		double damageMultiplier = damage / (damage + resistance);
		return damage * damageMultiplier;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void spawn() {
		isAlive = true;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void die(Damager killer) {
		isAlive = false;
		setCurrentHealth(0.0);
	}

	protected final TextPanel getNameplate() {
		return nameplate;
	}

}
