package com.eladrador.common.character;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.eladrador.common.GPlugin;

import net.minecraft.server.v1_13_R2.Entity;

public class AbstractCharacter {

	protected String name;
	protected int level;
	protected Location loc;
	protected float maxHealth;
	protected float currentHealth;
	protected UUID id;

	protected ArrayList<org.bukkit.entity.Entity> bukkitEntities;
	protected ArrayList<Entity> nMSEntities;

	protected AbstractCharacter(String name, int level) {
		this.name = name;
		this.level = level;
		id = UUID.randomUUID();
		bukkitEntities = new ArrayList<org.bukkit.entity.Entity>();
		nMSEntities = new ArrayList<Entity>();
		GPlugin.getGameManager().registerChara(this);
	}

	public String getName() {
		return name;
	}

	public int getLevel() {
		return level;
	}

	public UUID getId() {
		return id;
	}

	/**
	 * Moves the character.
	 * 
	 * @param movement     the movement this character will undergo
	 * @param moveEntities whether to move any entities associated with this
	 *                     character as well
	 */
	public void move(Vector movement) {
		loc.add(movement);
		for (Entity e : nMSEntities) {
			e.move(null, movement.getX(), movement.getY(), movement.getZ());
		}
		for (org.bukkit.entity.Entity e : bukkitEntities) {
			e.setVelocity(movement);
		}
	}

	protected void registerEntity(Entity entity) {
		nMSEntities.add(entity);
		entity.addScoreboardTag(id.toString());
	}

	protected void registerEntity(org.bukkit.entity.Entity entity) {
		bukkitEntities.add(entity);
		entity.addScoreboardTag(id.toString());
	}

	protected void unregisterEntity(Entity entity) {
		nMSEntities.remove(entity);
		entity.removeScoreboardTag(id.toString());
	}

	protected void unregisterEntity(org.bukkit.entity.Entity entity) {
		bukkitEntities.remove(entity);
		entity.removeScoreboardTag(id.toString());
	}

}
