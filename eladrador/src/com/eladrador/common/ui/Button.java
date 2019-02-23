package com.eladrador.common.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Button extends InteractableItem {

	public Button(String id, ItemStack itemStack) {
		super(id, itemStack);
	}

	@Override
	public final void onClickInInventory(InventoryClickEvent event) {
		event.setCancelled(true);
		Player player = (Player) event.getWhoClicked();
		onToggle(player);
	}

	@Override
	public final void onClickOnCursor(InventoryClickEvent event) {
		event.setCancelled(true);
	}

	@Override
	public final void onDragOnCursor(InventoryDragEvent event) {
		event.setCancelled(true);
	}

	@Override
	public final void onDrop(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}

	public abstract void onToggle(Player player);

}
