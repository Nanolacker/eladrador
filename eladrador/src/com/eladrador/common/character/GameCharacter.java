package com.eladrador.common.character;

import org.bukkit.Location;

import com.eladrador.common.character.DamageType;
import com.eladrador.common.character.Damager;
import com.eladrador.common.character.Resistances;

public interface GameCharacter {

	public String getName();

	public int getLevel();

	public Location getLocation();

	public void setLocation(Location location);

	public boolean isAlive();

	public double getCurrentHealth();

	public void setCurrentHealth(double currentHealth);

	public double getMaxHealth();

	public void setMaxHealth(double maxHealth);

	public Resistances getResistances();

	public void damage(double damage, DamageType damageType, Damager source);

	void heal(double amount);

	public void spawn();

	public void die(Damager killer);

}
