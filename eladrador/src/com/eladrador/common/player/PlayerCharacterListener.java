package com.eladrador.common.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

import com.eladrador.common.character.PlayerCharacterOLD;

public class PlayerCharacterListener implements Listener {

	@EventHandler
	private void onHotbarScroll(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		if (PlayerCharacterOLD.bukkitPlayerIsAttachedToPlayerCharacter(player)) {
			player.getInventory().setHeldItemSlot(0);
		}
	}

	@EventHandler
	private void onBukkitPlayerHealthLoss(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Player) {
			if (PlayerCharacterOLD.bukkitPlayerIsAttachedToPlayerCharacter((Player) entity)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void onBukkitPlayerHealthGain(EntityRegainHealthEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Player) {
			if (PlayerCharacterOLD.bukkitPlayerIsAttachedToPlayerCharacter((Player) entity)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void onBukkitPlayerHungerChange(FoodLevelChangeEvent event) {
		Player player = (Player) event.getEntity();
		if (PlayerCharacterOLD.bukkitPlayerIsAttachedToPlayerCharacter(player)) {
			event.setCancelled(true);
		}
	}

}
