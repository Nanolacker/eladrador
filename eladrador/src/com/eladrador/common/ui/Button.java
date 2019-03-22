package com.eladrador.common.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Button extends InteractableItem {

	private boolean toggleableByHold;

	public Button(String id, ItemStack itemStack) {
		super(id, itemStack);
		toggleableByHold = false;
	}

	public boolean getToggleableByHold() {
		return toggleableByHold;
	}

	public void setToggleableByHold(boolean toggleableByHold) {
		this.toggleableByHold = toggleableByHold;
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
	public void onClickInHand(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		onToggle(player);
	}

	@Override
	public final void onDrop(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onHoldInHand(PlayerItemHeldEvent event) {
		if (toggleableByHold) {
			Player player = event.getPlayer();
			onToggle(player);
		}
	}

	protected abstract void onToggle(Player player);

}
