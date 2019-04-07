package com.eladrador.common.player;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.util.Vector;

import com.eladrador.common.Debug;
import com.eladrador.common.character.PlayerCharacter;
import com.eladrador.common.physics.Collider;
import com.eladrador.common.physics.Physics;
import com.eladrador.common.physics.Ray;
import com.eladrador.common.physics.RaycastInfo;

public class PlayerCharacterListener implements Listener {

	/**
	 * The distance from which a player can interact with an object by
	 * right-clicking.
	 */
	private static final double PLAYER_INTERACT_DISTANCE = 4.0;

	@EventHandler
	private void onHotbarScroll(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		if (PlayerCharacter.bukkitPlayerIsAttachedToPlayerCharacter(player)) {
			player.getInventory().setHeldItemSlot(0);
		}
	}

	@EventHandler
	private void onBukkitPlayerHealthLoss(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Player) {
			if (PlayerCharacter.bukkitPlayerIsAttachedToPlayerCharacter((Player) entity)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void onBukkitPlayerHealthGain(EntityRegainHealthEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Player) {
			if (PlayerCharacter.bukkitPlayerIsAttachedToPlayerCharacter((Player) entity)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void onBukkitPlayerHungerChange(FoodLevelChangeEvent event) {
		Player player = (Player) event.getEntity();
		if (PlayerCharacter.bukkitPlayerIsAttachedToPlayerCharacter(player)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	private void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			return;
		}
		if (PlayerCharacter.bukkitPlayerIsAttachedToPlayerCharacter(player)) {
			event.setCancelled(true);
			interact(player);
		}
	}

	@EventHandler
	private void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		if (PlayerCharacter.bukkitPlayerIsAttachedToPlayerCharacter(player)) {
			event.setCancelled(true);
			interact(player);
		}
	}

	private void interact(Player player) {
		PlayerCharacter pc = PlayerCharacter.forBukkitPlayer(player);

		Location loc = player.getLocation().add(0.0, player.getEyeHeight(), 0.0);
		Vector dir = loc.getDirection();
		dir.multiply(PLAYER_INTERACT_DISTANCE);
		Ray ray = new Ray(loc, dir);

		RaycastInfo raycastInfo = Physics.raycast(ray, PlayerCharacterInteractable.class);
		if (raycastInfo == null) {
			return;
		}
		Collider collider = raycastInfo.getCollider();
		if (collider instanceof PlayerCharacterInteractable) {
			PlayerCharacterInteractable interactable = (PlayerCharacterInteractable) collider;
			interactable.onInteract(pc);
		}
	}

}
