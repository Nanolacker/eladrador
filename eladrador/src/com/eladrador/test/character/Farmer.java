package com.eladrador.test.character;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.util.Vector;

import com.eladrador.common.Debug;
import com.eladrador.common.character.CharacterCollider;
import com.eladrador.common.character.CharacterEntityMovementSynchronizer;
import com.eladrador.common.character.CharacterEntityMovementSynchronizer.MovementSynchronizeMode;
import com.eladrador.common.character.DamageType;
import com.eladrador.common.character.Damager;
import com.eladrador.common.character.GameCharacter;
import com.eladrador.common.character.NonPlayerCharacter;
import com.eladrador.common.character.PlayerCharacter;
import com.eladrador.common.physics.Physics;
import com.eladrador.common.physics.Ray;
import com.eladrador.common.physics.RaycastInfo;
import com.eladrador.common.player.PlayerCharacterInteractable;
import com.eladrador.common.scheduling.RepeatingTask;

import net.md_5.bungee.api.ChatColor;

public class Farmer extends NonPlayerCharacter {

	private static final Vector HITBOX_CENTER_OFFSET = new Vector(0.0, 1.0, 0.0);

	private Villager entity;
	private CharacterCollider hitbox;

	public Farmer(Location location) {
		super("Farmer", 1, location, Integer.MAX_VALUE);
		setNameplateLocationOffset(new Vector(0.0, 2.0, 0.0));
		Location hitboxCenter = location.clone().add(HITBOX_CENTER_OFFSET);
		hitbox = new FarmerHitbox(this, hitboxCenter, 1.0, 2.0, 1.0);
		hitbox.setActive(true);
		hitbox.setDrawingEnabled(true);
	}

	@Override
	public void setLocation(Location location) {
		super.setLocation(location);
		hitbox.setCenter(location.clone().add(HITBOX_CENTER_OFFSET));
	}

	@Override
	public void spawn() {
		super.spawn();
		Location location = getLocation();
		World world = location.getWorld();
		entity = (Villager) world.spawnEntity(location, EntityType.VILLAGER);
		entity.setInvulnerable(true);
		entity.setAI(false);
		CharacterEntityMovementSynchronizer syncer = new CharacterEntityMovementSynchronizer(this, entity,
				MovementSynchronizeMode.CHARACTER_FOLLOWS_ENTITY);
		syncer.setEnabled(true);
	}

	@Override
	public void damage(double damage, DamageType damageType, Damager source) {
		super.damage(damage, damageType, source);
		entity.setVelocity(new Vector(0, 0.1, 0));
	}

	@Override
	public void despawn() {
		super.despawn();
		entity.remove();
	}

	public static class FarmerHitbox extends CharacterCollider implements PlayerCharacterInteractable {

		public FarmerHitbox(GameCharacter character, Location center, double lengthX, double lengthY, double lengthZ) {
			super(character, center, lengthX, lengthY, lengthZ);
		}

		@Override
		public void onInteract(PlayerCharacter pc) {
			pc.sendMessage(ChatColor.GREEN + "Farmer: Greetings adventurer!");
		}
	}

}
